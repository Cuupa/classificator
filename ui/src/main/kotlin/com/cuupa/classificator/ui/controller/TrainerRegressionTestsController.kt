package com.cuupa.classificator.ui.controller

import com.cuupa.classificator.domain.Metadata
import com.cuupa.classificator.domain.Sender
import com.cuupa.classificator.domain.Topic
import com.cuupa.classificator.engine.Classificator
import com.cuupa.classificator.engine.KnowledgeManager
import com.cuupa.classificator.engine.extensions.Extension.isLast
import com.cuupa.classificator.engine.extensions.Extension.toMetadata
import com.cuupa.classificator.engine.services.TextExtractor
import com.cuupa.classificator.engine.services.application.InfoService
import com.cuupa.classificator.trainer.services.Document
import com.cuupa.classificator.trainer.services.Trainer
import com.cuupa.classificator.ui.TrainerClassifyProcess
import com.cuupa.classificator.ui.csv.CsvFile
import org.apache.commons.logging.LogFactory
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.text.DecimalFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@Controller
class TrainerRegressionTestsController(
    private val classificator: Classificator,
    private val manager: KnowledgeManager,
    private val trainer: Trainer,
    private val textExtractor: TextExtractor,
    private val infoService: InfoService
) {

    @GetMapping(value = ["/trainer"])
    fun trainer(): ModelAndView {
        val topics = manager.getTopics()
        val trainerMeasures = trainer.getMeasures(topics)

        val sender = manager.getSender()
        val senderMeasures = trainer.getMeasures(sender)

        val metadata = manager.getMetadata().toMetadata()
        val metadataMeasures = trainer.getMeasures(metadata)

        val modelAndView = ModelAndView("trainer/trainer").apply {
            addObject("precisionTopics", decimalFormat.format(trainerMeasures.precision))
            addObject("recallTopics", decimalFormat.format(trainerMeasures.recall))
            addObject("fScoreTopics", decimalFormat.format(trainerMeasures.fScore))
            addObject("numberOfDocuments_topic", trainerMeasures.numberOfDocuments)
            addObject("correct_topic", trainerMeasures.correct)
            addObject("incorrect_topic", trainerMeasures.incorrect)

            addObject("precisionSender", decimalFormat.format(senderMeasures.precision))
            addObject("recallSender", decimalFormat.format(senderMeasures.recall))
            addObject("fScoreSender", decimalFormat.format(senderMeasures.fScore))
            addObject("numberOfDocuments_sender", senderMeasures.numberOfDocuments)
            addObject("correct_sender", senderMeasures.correct)
            addObject("incorrect_sender", senderMeasures.incorrect)

            addObject("precisionMetadata", decimalFormat.format(metadataMeasures.precision))
            addObject("recallMetadata", decimalFormat.format(metadataMeasures.recall))
            addObject("fScoreMetadata", decimalFormat.format(metadataMeasures.fScore))
            addObject("numberOfDocuments_metadata", metadataMeasures.numberOfDocuments)
            addObject("correct_metadata", metadataMeasures.correct)
            addObject("incorrect_metadata", metadataMeasures.incorrect)

            addObject("kb_version", manager.getVersion())
            addObject("application_version", infoService.getVersion())
        }
        return modelAndView
    }

    @GetMapping(value = ["/trainer/add"])
    fun trainerAdd(): ModelAndView {
        return ModelAndView("trainer/regression/trainer_add").apply {
            addObject("kb_version", manager.getVersion())
            addObject("uuid", "text-${UUID.randomUUID()}")
            addObject("application_version", infoService.getVersion())
        }
    }

    @PostMapping(value = ["/trainer/add"])
    fun uploadData(
        @RequestParam batchName: String?,
        @RequestParam file: MultipartFile?,
        redirectAttributes: RedirectAttributes
    ): ModelAndView {

        val model = ModelAndView("trainer/regression/trainer_add").apply {
            addObject("kb_version", manager.getVersion())
            addObject("application_version", infoService.getVersion())
        }

        val success = try {

            val contentType = file?.contentType ?: MediaType.APPLICATION_OCTET_STREAM_VALUE
            val content = file?.bytes ?: ByteArray(0)

            if (content.isEmpty()) {
                model.addObject("message", "The document is empty")
                false
            } else if (isUnsupportedContentType(contentType)) {
                model.addObject("message", "The uploaded document '${file?.originalFilename}' is not a CSV file")
                false
            } else {
                val csvFile = CsvFile(content)
                val timestamp = Instant.now().toEpochMilli()
                val documents = csvFile.lines.map {
                    Document(
                        content = it.content.toByteArray(),
                        contentType = it.contentType,
                        plainText = it.content,
                        batchName = batchName ?: "Unnamed",
                        timestamp = timestamp,
                        expectedTopics = it.topics,
                        expectedSenders = it.senders,
                        expectedMetadata = it.metadata
                    )
                }
                trainer.persist(documents)

                for (entry in csvFile.lines) {
                    val document =
                        documents.find { it.plainText == entry.content && it.contentType == entry.contentType }
                    document?.let {
                        if (isPreclassified(csvFile, it)) {
                            trainer.complete(it)
                        }
                    }
                }

                model.addObject(
                    "message",
                    "Successfully imported ${csvFile.size} documents with batch name '$batchName' from ${file?.originalFilename}"
                )
                model.addObject("uuid", "text-${UUID.randomUUID()}")
                true
            }
        } catch (e: Exception) {
            model.addObject("message", "failed to upload testdata ${file?.originalFilename}")
            log.error("failed to upload testdata ${file?.originalFilename}", e)
            false
        }
        log.info(model.modelMap["message"])

        if (!success) {
            model.addObject("uuid", batchName)
        }

        model.addObject("success", success)
        return model
    }

    private fun isPreclassified(csvFile: CsvFile, document: Document): Boolean {
        csvFile.lines.forEach {
            if ((it.senders.isNotEmpty()
                    .or(it.topics.isNotEmpty())
                    .or(it.metadata.isNotEmpty()))
                && document.plainText == it.content
                && document.contentType == it.contentType
            ) {
                return true
            }
        }
        return false
    }

    private fun isUnsupportedContentType(contentType: String) = !supportedContentTypes.contains(contentType)

    @GetMapping(value = ["/trainer/classify/batch/{batchId}"])
    fun classifyBatch(@PathVariable batchId: String?): String {
        if (batchId.isNullOrEmpty()) {
            return "redirect:/trainer/classify/open"
        }
        val document = trainer.getBatch(batchId).first()
        return "redirect:/trainer/classify/batch/$batchId/${document.id}"
    }

    @GetMapping(value = ["/trainer/classify/batch/{batchId}/{documentId}"])
    fun classifyBatch(@PathVariable batchId: String?, @PathVariable documentId: String?): ModelAndView {
        val modelAndView = ModelAndView("trainer/regression/trainer_classify_batch").apply {
            addObject("kb_version", manager.getVersion())
            addObject("application_version", infoService.getVersion())
        }

        if (batchId.isNullOrEmpty()) {
            modelAndView.addObject("message", "No ID provided")
            return modelAndView
        }

        val batch = trainer.getBatch(batchId)
        val documentIds = batch.map { it.id }
        val document = batch.find { it.id == documentId }
        if (document == null) {
            modelAndView.addObject("message", "Document $documentId does not exist in Batch $batchId")
        } else {
            modelAndView.addObject("selectedDocument", document)
            modelAndView.addObject("next", getNext(documentIds, documentId))
            modelAndView.addObject("topics", manager.getTopics())
            modelAndView.addObject("sender", manager.getSender())
            modelAndView.addObject("metadata", manager.getMetadata())
        }
        return modelAndView
    }

    private fun getNext(documentIds: List<String>, documentId: String?): String {
        if (documentIds.size < documentIds.indexOf(documentId) + 1) {
            return documentIds[documentIds.indexOf(documentId) + 1]
        }
        return ""
    }

    @GetMapping(value = ["/trainer/batch/delete/{id}"])
    fun deleteBatch(@PathVariable id: String?): ModelAndView {
        val muv = ModelAndView("trainer/regression/trainer_classify").apply {
            addObject("kb_version", manager.getVersion())
            addObject("application_version", infoService.getVersion())
        }

        if (id.isNullOrEmpty()) {
            muv.addObject("success", false)
            muv.addObject("message", "Invalid ID provided")
            return muv
        }

        val batch = trainer.getBatch(id)
        if (batch.isEmpty()) {
            muv.addObject("success", false)
            muv.addObject("message", "No batch for ID $id found")
            return muv
        }

        trainer.removeBatch(id)
        muv.addObject("success", true)
        muv.addObject("message", "Batch '$id' with ${batch.size} documents successfully removed")
        muv.addObject("batchNames", trainer.getBatchNames())
        muv.addObject("batchContent", listOf<Document>())
        return muv
    }

    @PostMapping(value = ["/trainer/classify/document"])
    fun classifyDocument(
        @RequestParam("payload-topic") topics: String?,
        @RequestParam("payload-sender") sender: String?,
        @RequestParam("payload-metadata") metadata: String?,
        @RequestParam("documentId") documentId: String?
    ): String {
        val configuredTopics = topics?.split(";")?.filter { it.isNotEmpty() } ?: listOf()
        val configuredSender = sender?.split(";")?.filter { it.isNotEmpty() } ?: listOf()
        val configuredMetadata = metadata?.split(";")?.filter { it.isNotEmpty() } ?: listOf()

        val document = trainer.getDocument(documentId)
        trainer.complete(document.apply {
            this.expectedTopics = configuredTopics
            this.expectedSenders = configuredSender
            this.expectedMetadata = configuredMetadata
        })

        val batch = trainer.getBatch(document.batchName)
        val currentIndex = batch.map { it.id }.indexOf(documentId)

        if (batch.isLast(currentIndex)) {
            return "redirect:/trainer"
        }

        val nextDocument = batch[currentIndex + 1].id
        return "redirect:/trainer/classify/batch/${document.batchName}/$nextDocument"
    }

    @GetMapping(value = ["/trainer/classify/open"])
    fun classifyOpen() = ModelAndView("trainer/regression/trainer_classify").apply {
        addObject("batchNames", trainer.getBatchNames())
        addObject("batchContent", listOf<Document>())
        addObject("kb_version", manager.getVersion())
        addObject("application_version", infoService.getVersion())
    }

    @GetMapping(value = ["/trainer/classify/open/{id}"])
    fun batchDetails(@PathVariable id: String?): ModelAndView {
        val modelAndView = ModelAndView("trainer/regression/trainer_classify").apply {
            addObject("kb_version", manager.getVersion())
            addObject("application_version", infoService.getVersion())
        }
        if (id.isNullOrEmpty()) {
            modelAndView.addObject("success", false)
            modelAndView.addObject("message", "No batch ID provided")
        } else {
            modelAndView.addObject("batchNames", trainer.getBatchNames())
            modelAndView.addObject("selectedBatchName", id)
            val batch = trainer.getBatch(id)
            modelAndView.addObject("batchContent", batch)
            modelAndView.addObject("uploadTime", getUploadTime(batch))
        }
        return modelAndView
    }

    @GetMapping(value = ["/trainer/classify/{id}"])
    fun classifyOpen(@PathVariable id: String): ModelAndView {
        val process = getModel(id, manager.getTopics(), manager.getSender(), manager.getMetadata().toMetadata())
        val encode = String(Base64.getEncoder().encode(process.selected.content))
        val result = classificator.classify(contentType = process.selected.contentType, content = encode)
        process.selectedResult = result
        return ModelAndView("trainer/regression/trainer_classify_batch").apply {
            addObject("trainerClassifyProcess", process)
            addObject("kb_version", manager.getVersion())
            addObject("application_version", infoService.getVersion())
        }
    }

    @GetMapping(value = ["/trainer/process"])
    fun process() = ModelAndView("trainer/regression/trainer_process").apply {
        addObject("batchNames", trainer.getBatchNames())
        addObject("batchContent", listOf<Document>())
        addObject("kb_version", manager.getVersion())
        addObject("application_version", infoService.getVersion())
    }

    @GetMapping(value = ["/trainer/process/{id}"])
    fun process(@PathVariable id: String?): ModelAndView {
        val modelAndView = ModelAndView("trainer/regression/trainer_process").apply {
            addObject("kb_version", manager.getVersion())
        }
        if (id.isNullOrEmpty()) {
            modelAndView.addObject("success", false)
            modelAndView.addObject("message", "No batch ID provided")
        } else {
            val batch = trainer.getBatch(id)
            modelAndView.addObject("batchNames", trainer.getBatchNames())
                .addObject("selectedBatchName", id)
                .addObject("batchContent", batch)
                .addObject("uploadTime", getUploadTime(batch))
        }
        return modelAndView
    }

    @GetMapping(value = ["/trainer/process/details/{id}"])
    fun processDetails(@PathVariable id: String?): ModelAndView {
        val modelAndView = ModelAndView("trainer/regression/trainer_process_details").apply {
            addObject("kb_version", manager.getVersion())
            addObject("application_version", infoService.getVersion())
        }
        if (id.isNullOrEmpty()) {
            modelAndView.addObject("success", false)
            modelAndView.addObject("message", "No batch ID provided")
        } else {
            val batch = trainer.getBatch(id)
            modelAndView.addObject("batchNames", trainer.getBatchNames())
                .addObject("selectedBatchName", id)
                .addObject("batchContent", batch)
                .addObject("uploadTime", getUploadTime(batch))
                .addObject("documents", batch)
                .addObject("selected", "1")
        }
        return modelAndView
    }

    @GetMapping(value = ["/trainer/process/doprocess/{id}"])
    fun doprocess(@PathVariable id: String?): ModelAndView {
        val modelAndView = ModelAndView("trainer/regression/trainer_process_details").apply {
            addObject("kb_version", manager.getVersion())
            addObject("application_version", infoService.getVersion())
        }
        if (id.isNullOrEmpty()) {
            modelAndView.addObject("success", false)
            modelAndView.addObject("message", "No batch ID provided")
        } else {
            val batch = trainer.getBatch(id)
            batch.forEach { document ->
                val result = classificator.classify(document.contentType, document.plainText)
                document.actualTopics = result.second
                    .map { it.topic }
                    .filter { it != "OTHER" }
                    .filter { it.isNotEmpty() }

                document.actualSenders = result.second
                    .map { it.sender }
                    .filter { it != "UNKNOWN" }
                    .filter { it.isNotEmpty() }

                document.actualMetadata = result.second
                    .map { it.metadata }
                    .map { it.joinToString { entry -> "${entry.name}:${entry.value}" } }
                    .filter { it.isNotEmpty() }
                    .filter { it != ":" }

                trainer.complete(document)
            }

            modelAndView.addObject("batchNames", trainer.getBatchNames())
                .addObject("selectedBatchName", id)
                .addObject("batchContent", batch)
                .addObject("uploadTime", getUploadTime(batch))
                .addObject("documents", batch)
                .addObject("selected", "1")
        }
        return modelAndView
    }

    private fun getModel(
        id: String?,
        topics: List<Topic>,
        sender: List<Sender>,
        metadata: List<Metadata>
    ): TrainerClassifyProcess {
        return TrainerClassifyProcess(
            ids = trainer.getOpenDocuments().map { it.id },
            selected = trainer.getDocument(id!!),
            topics = topics.distinctBy { it.name }.sortedBy { it.name },
            sender = sender.distinctBy { it.name }.sortedBy { it.name },
            metadata = metadata.distinctBy { it.name }.sortedBy { it.name }
        )
    }

    private fun getUploadTime(batch: List<Document>) = formatter.format(
        LocalDateTime.ofInstant(
            Instant.ofEpochMilli(batch.first().timestamp),
            OffsetDateTime.now().offset
        )
    )

    companion object {
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")
        val decimalFormat = DecimalFormat("0.00")
        val log = LogFactory.getLog(TrainerRegressionTestsController::class.java)
        val supportedContentTypes = listOf(
            "text/csv",
            // if office is installed on windows machines, a CSV file will have application/vnd.ms-excel contenttype
            "application/vnd.ms-excel"
        )
    }
}