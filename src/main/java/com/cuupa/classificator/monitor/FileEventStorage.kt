package com.cuupa.classificator.monitor

import org.apache.juli.logging.LogFactory
import org.springframework.beans.factory.annotation.Value
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.stream.Collectors

class FileEventStorage : EventStorage() {

    @Value("\${classificator.monitor.fileeventstorage.path}")
    var path: String? = null

    override fun write(event: Event) {
        val path = Paths.get(getFilename(event))
        if (!Files.exists(path)) {
            Files.writeString(path, getFileHeader())
        }
        Files.writeString(path, getEventData(event), StandardOpenOption.APPEND)
        if (log.isDebugEnabled) {
            log.debug("Wrote event $event to ${path.toFile().absolutePath}")
        }
    }

    override fun get(start: LocalDateTime?, end: LocalDateTime?): List<Event> {
        val listOfAllFiles = Files.list(Path.of(getDirectoryOfFiles())).filter { inBetween(it, start, end) }
        return listOfAllFiles.map { Files.readAllLines(it) }
                .collect(Collectors.toList())
                .flatten()
                .map { maptToEvent(it) }
    }

    private fun inBetween(path: Path, start: LocalDateTime?, end: LocalDateTime?): Boolean {
        val filename = path.toFile().name.split(".").first()
        val dateTimeOfLog = LocalDateTime.parse(filename, formatter)
        val startLocal = start ?: LocalDateTime.MIN
        val endLocal = end ?: LocalDateTime.MAX
        return dateTimeOfLog.isAfter(startLocal).and(dateTimeOfLog.isBefore(endLocal))
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
        return fields[statisticalFields.indexOf(fieldName)].split(",")
    }

    private fun toString(fields: List<String>, fieldName: String): String {
        return fields[statisticalFields.indexOf(fieldName)]
    }

    private fun toLocalDateTime(fields: List<String>, fieldName: String) = LocalDateTime.parse(fields[statisticalFields.indexOf(
        fieldName)])

    private fun getFileHeader(): String {
        return statisticalFields.joinToString(semicolon, "", newLine())
    }

    private fun getEventData(event: Event): String {
        return StringBuilder().append(event.start)
                .append(semicolon)
                .append(event.end)
                .append(semicolon)
                .append(event.text ?: "")
                .append(semicolon)
                .append(event.results.joinToString(",", "", ""))
                .append(semicolon)
                .append(event.senders.joinToString(",", "", ""))
                .append(semicolon)
                .append(event.metadata.joinToString(",", "", ""))
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
