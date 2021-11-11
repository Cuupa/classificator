package com.cuupa.classificator.ui.controller

import com.cuupa.classificator.domain.Metadata
import com.cuupa.classificator.domain.Sender
import com.cuupa.classificator.domain.Topic
import com.cuupa.classificator.engine.ClassificatorImplementation
import com.cuupa.classificator.engine.KnowledgeManager
import com.cuupa.classificator.engine.services.TextExtractor
import com.cuupa.classificator.trainer.services.Document
import com.cuupa.classificator.trainer.services.Trainer
import com.cuupa.classificator.ui.TrainerClassifyProcess
import com.cuupa.classificator.ui.TrainerProcess
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
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
    classificator: ClassificatorImplementation,
    manager: KnowledgeManager,
    trainer: Trainer,
    textExtractor: TextExtractor
) : TrainerController(classificator, manager, trainer, textExtractor) {

    @GetMapping(value = ["/trainer"])
    fun trainer(): ModelAndView {
        val topics = manager.getTopics()
        val precisionTopics = trainer.getPrecision(topics)
        val recallTopics = trainer.getRecall(topics)
        val fScoreTopics = trainer.getFScore(precisionTopics, recallTopics)

        val sender = manager.getSender()
        val precisionSender = trainer.getPrecision(sender)
        val recallSender = trainer.getRecall(sender)
        val fScoreSender = trainer.getFScore(precisionSender, recallSender)

        val metadata = manager.getMetadata()
        val precisionMetadata = trainer.getPrecision(metadata.toMetadata())
        val recallMetadata = trainer.getRecall(metadata.toMetadata())
        val fScoreMetadata = trainer.getFScore(precisionMetadata, recallMetadata)

        val modelAndView = ModelAndView("trainer").apply {
            addObject("precisionTopics", decimalFormat.format(precisionTopics))
            addObject("recallTopics", decimalFormat.format(recallTopics))
            addObject("fScoreTopics", decimalFormat.format(fScoreTopics))

            addObject("precisionSender", decimalFormat.format(precisionSender))
            addObject("recallSender", decimalFormat.format(recallSender))
            addObject("fScoreSender", decimalFormat.format(fScoreSender))

            addObject("precisionMetadata", decimalFormat.format(precisionMetadata))
            addObject("recallMetadata", decimalFormat.format(recallMetadata))
            addObject("fScoreMetadata", decimalFormat.format(fScoreMetadata))
        }
        return modelAndView
    }

    @GetMapping(value = ["/trainer/add"])
    fun trainerAdd(model: Model): String {
        if (!model.containsAttribute("trainerProcess")) {
            model.addAttribute("trainerProcess", TrainerProcess())
        }
        return "trainer_add"
    }

    @PostMapping(value = ["/trainer/add"])
    fun uploadData(
        @RequestParam batchName: String?,
        @RequestParam file: MultipartFile?,
        redirectAttributes: RedirectAttributes
    ): ModelAndView {

        val model = ModelAndView().apply { viewName = "trainer_add" }

        val success = try {

            val contentType = file?.contentType ?: MediaType.APPLICATION_OCTET_STREAM_VALUE
            val content = file?.bytes ?: ByteArray(0)
            val textExtract = textExtractor.extractText(contentType, content)

            trainer.persist(
                contentType,
                content,
                textExtract.content,
                batchName ?: "",
                Instant.now().toEpochMilli()
            )
            model.addObject(
                "message",
                "Successfully imported 1 documents with batch name '$batchName'."
            )
            true
        } catch (e: Exception) {
            false
        }
        model.addObject("success", success)
        return model
    }

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
        val modelAndView = ModelAndView().apply { viewName = "trainer_classify_batch" }
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
            modelAndView.addObject("next", documentIds[documentIds.indexOf(documentId) + 1])
            modelAndView.addObject("topics", manager.getTopics())
            modelAndView.addObject("sender", manager.getSender())
            modelAndView.addObject("metadata", manager.getMetadata())
        }
        return modelAndView
    }

    @GetMapping(value = ["/trainer/batch/delete/{id}"])
    fun deleteBatch(@PathVariable id: String?): ModelAndView {
        val muv = ModelAndView("trainer_classify")

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
        muv.addObject("message", "Batch $id with ${batch.size} documents successfully removed")
        return muv
    }

    @PostMapping(value = ["/trainer/classify/document"])
    fun classifyDocument(
        @RequestParam("payload-topic") topics: String?,
        @RequestParam("payload-sender") sender: String?,
        @RequestParam("payload-metadata") metadata: String?,
        @RequestParam("documentId") documentId: String?
    ): String {
        val configuredTopics = topics?.split(";")?.filter { !it.isNullOrEmpty() } ?: listOf()
        val configuredSender = sender?.split(";")?.filter { !it.isNullOrEmpty() } ?: listOf()
        val configuredMetadata = metadata?.split(";")?.filter { !it.isNullOrEmpty() } ?: listOf()

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
    fun classifyOpen() = ModelAndView().apply {
        addObject("batchNames", trainer.getBatchNames())
        addObject("batchContent", listOf<Document>())
        viewName = "trainer_classify"
    }

    @GetMapping(value = ["/trainer/classify/open/{id}"])
    fun batchDetails(@PathVariable id: String?): ModelAndView {
        val modelAndView = ModelAndView().apply { viewName = "trainer_classify" }
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
    fun classifyOpen(model: Model, @PathVariable id: String): String {
        val process = getModel(id, manager.getTopics(), manager.getSender(), manager.getMetadata().toMetadata())
        val encode = String(Base64.getEncoder().encode(process.selected.content))
        val result = classificator.classify(contentType = process.selected.contentType, content = encode)
        process.selectedResult = result
        model.addAttribute("trainerClassifyProcess", process)
        return "trainer_classify_batch"
    }

    private fun getModel(
        id: String?,
        topics: List<Topic>,
        sender: List<Sender>,
        metadata: List<Metadata>
    ): TrainerClassifyProcess {
        return TrainerClassifyProcess(
            ids = trainer.getOpenDocuments().map { it.id.toString() },
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
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")
        val decimalFormat = DecimalFormat("0.00")
    }
}

private fun List<Document>.isLast(index: Int) = this.size == index + 1