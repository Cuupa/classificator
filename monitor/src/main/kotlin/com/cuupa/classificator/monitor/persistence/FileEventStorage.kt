package com.cuupa.classificator.monitor.persistence

import com.cuupa.classificator.monitor.service.Event
import org.apache.commons.logging.LogFactory
import org.springframework.beans.factory.annotation.Value
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.stream.Collectors

class FileEventStorage : EventStorage() {

    @Value("\${classificator.monitor.fileeventstorage.path}")
    var path: String? = null

    private var lastModifiedEventStorage = 0L

    private var cachedEventList = listOf<Event>()

    override fun write(event: Event) {
        val path = Paths.get(getFilename(event))
        if (!directoryExists(path)) {
            createDirectories(path)
        }

        if (!Files.exists(path)) {
            Files.writeString(path, Event.headlines())
        }

        Files.writeString(path, event.toCsvString(), StandardOpenOption.APPEND)
        if (log.isDebugEnabled) {
            log.debug("Wrote event $event to ${path.toFile().absolutePath}")
        }
    }

    private fun createDirectories(path: Path) = path.toFile().parentFile.mkdirs()

    private fun directoryExists(path: Path) = path.toFile().parentFile.exists()

    override fun get(start: LocalDate?, end: LocalDate?): List<Event> {
        val directoryOfFiles = Paths.get(getDirectoryOfFiles())

        if (!Files.exists(directoryOfFiles)) {
            return listOf()
        }

        val files = Files.list(directoryOfFiles)
            .filter { isCsv(it) }
            .filter { isDatabase(it) }
            .collect(Collectors.toList())
            .filterNotNull()

        val updatedFiles = files.filter { lastModifiedEventStorage < it.toFile().lastModified() }

        if (updatedFiles.isEmpty()) {
            return cachedEventList
        }
        lastModifiedEventStorage = updatedFiles.maxOf { it.toFile().lastModified() }
        cachedEventList = files.asSequence()
            .filter { inBetween(it, start, end) }
            .map { Files.readAllLines(it) }
            .flatten()
            .filter { isNotHeadline(it) }
            .mapNotNull { maptToEvent(it) }.toList()
        return cachedEventList
    }

    private fun isDatabase(it: Path): Boolean {
        return try {
            formatter.parse(it.toFile().name.removeSuffix(".csv"))
            true
        } catch (e: Exception) {
            false
        }
    }

    private fun isNotHeadline(line: String) = line != Event.headlines().replace("\n", "").replace("\r", "")

    private fun isCsv(path: Path) = path.toFile().name.endsWith(".csv")

    private fun inBetween(path: Path, start: LocalDate?, end: LocalDate?): Boolean {
        val filename = path.toFile().name.split(".").first()
        val dateTimeOfLog = LocalDate.parse(filename, formatter)
        val startLocal = start ?: LocalDate.MIN
        val endLocal = end ?: LocalDate.MAX
        return dateTimeOfLog.isAfter(startLocal)
            .and(dateTimeOfLog.isBefore(endLocal)) || dateTimeOfLog == startLocal || dateTimeOfLog == endLocal
    }

    private fun maptToEvent(data: String): Event? {
        val fields = data.split(";")
        return try {
            Event(
                toString(fields, "KB-VERSION"),
                toString(fields, "TEXT"),
                toStringList(fields, "TOPICS"),
                toStringList(fields, "SENDER"),
                toStringList(fields, "METADATA"),
                toLocalDateTime(fields, "RECEIVED"),
                toLocalDateTime(fields, "PROCESSED")
            )
        } catch (e: Exception) {
            log.error(data, e)
            null
        }
    }

    private fun toStringList(fields: List<String>, fieldName: String): List<String> {
        val field = fields[Event.statisticalFields.indexOf(fieldName)]
        return if (field.contains(",")) {
            field.split(",")
        } else {
            listOf(field)
        }
    }

    private fun toString(fields: List<String>, fieldName: String) = fields[Event.statisticalFields.indexOf(fieldName)]

    private fun toLocalDateTime(fields: List<String>, fieldName: String) = LocalDateTime.parse(
        fields[Event.statisticalFields.indexOf(
            fieldName
        )]
    )

    private fun getFilename(event: Event): String {
        val filename = formatter.format(event.start) + ".csv"
        if (!path.isNullOrBlank()) {
            return getDirectoryOfFiles() + filename
        }
        return filename
    }

    private fun getDirectoryOfFiles() = if (path!!.endsWith(File.separator)) path!! else (path + File.separator)

    companion object {
        private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        private val log = LogFactory.getLog(FileEventStorage::class.java)
    }
}
