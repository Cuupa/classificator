package com.cuupa.classificator.monitor

import org.apache.juli.logging.LogFactory
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
            Files.writeString(path, getFileHeader())
        }

        Files.writeString(path, getEventData(event), StandardOpenOption.APPEND)
        if (log.isDebugEnabled) {
            log.debug("Wrote event $event to ${path.toFile().absolutePath}")
        }
    }

    private fun createDirectories(path: Path) {
        path.toFile().parentFile.mkdirs()
    }

    private fun directoryExists(path: Path) = path.toFile().parentFile.exists()

    override fun get(start: LocalDate?, end: LocalDate?): List<Event> {
        val directoryOfFiles = Paths.get(getDirectoryOfFiles())

        if (!Files.exists(directoryOfFiles)) {
            return listOf()
        }

        val lastModified = directoryOfFiles.toFile()
                .lastModified()
       // if (lastModifiedEventStorage >= lastModified) {
       //     return cachedEventList
        //}
        lastModifiedEventStorage = lastModified
        val listOfAllFiles = Files.list(directoryOfFiles)
                .collect(Collectors.toList())
                .filterNotNull()
                .filter { isCsv(it) }
                .filter { inBetween(it, start, end) }
        cachedEventList = listOfAllFiles.map { Files.readAllLines(it) }
                .flatten()
                .filter { isNotHeadline(it) }
                .map { maptToEvent(it) }
        return cachedEventList
    }

    private fun isNotHeadline(line: String) = line != getFileHeader().replace("\n", "").replace("\r", "")

    private fun isCsv(path: Path) = path.toFile().name.endsWith(".csv")

    private fun inBetween(path: Path, start: LocalDate?, end: LocalDate?): Boolean {
        val filename = path.toFile().name.split(".")
                .first()
        val dateTimeOfLog = LocalDate.parse(filename, formatter)
        val startLocal = start ?: LocalDate.MIN
        val endLocal = end ?: LocalDate.MAX
        return dateTimeOfLog.isAfter(startLocal)
                .and(dateTimeOfLog.isBefore(endLocal))
    }

    private fun maptToEvent(data: String): Event {
        val fields = data.split(";")
        return Event(toString(fields, "TEXT"),
                     toStringList(fields, "TOPICS"),
                     toStringList(fields, "SENDER"),
                     toStringList(fields, "METADATA"),
                     toLocalDateTime(fields, "RECEIVED"),
                     toLocalDateTime(fields, "PROCESSED"))
    }

    private fun toStringList(fields: List<String>, fieldName: String): List<String> {
        val field = fields[statisticalFields.indexOf(fieldName)]
        return if (field.contains(",")) {
            field.split(",")
        } else {
            listOf(field)
        }
    }

    private fun toString(fields: List<String>, fieldName: String): String {
        return fields[statisticalFields.indexOf(fieldName)]
    }

    private fun toLocalDateTime(fields: List<String>, fieldName: String) = LocalDateTime.parse(fields[statisticalFields.indexOf(
        fieldName)])

    private fun getFileHeader(): String {
        return statisticalFields.joinToString(semicolon, "", newLine())
    }

    /**
     * "RECEIVED", "PROCESSED", "TOPICS", "SENDER", "METADATA", "TEXT"
     */
    private fun getEventData(event: Event): String {
        return StringBuilder()
            .append(event.start)
            .append(semicolon)
            .append(event.end)
            .append(semicolon)
            .append(event.results.joinToString(",", "", ""))
            .append(semicolon)
            .append(event.senders.joinToString(",", "", ""))
            .append(semicolon)
            .append(event.metadata.joinToString(",", "", ""))
            .append(semicolon)
            .append(event.text?.replace(";", " ")?.replace("\n", " ")?.replace("\r", "") ?: "")
            .append(semicolon)
            .append(newLine())
            .toString()
    }

    private val semicolon = ";"

    private fun newLine() = System.getProperty("line.separator")

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
