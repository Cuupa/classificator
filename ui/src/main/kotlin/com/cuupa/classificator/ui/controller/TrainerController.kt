package com.cuupa.classificator.ui.controller

import com.cuupa.classificator.domain.Metadata
import com.cuupa.classificator.domain.Regex
import com.cuupa.classificator.domain.Sender
import com.cuupa.classificator.domain.Topic
import com.cuupa.classificator.engine.ClassificatorImplementation
import com.cuupa.classificator.engine.KnowledgeManager
import com.cuupa.classificator.engine.services.token.MetaDataToken
import com.cuupa.classificator.trainer.service.Document
import com.cuupa.classificator.trainer.service.Trainer
import com.cuupa.classificator.ui.TrainerClassifyProcess
import com.cuupa.classificator.ui.TrainerProcess
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.util.*

@Controller
class TrainerController(
    val classificator: ClassificatorImplementation,
    val manager: KnowledgeManager,
    val trainer: Trainer
) {

    @GetMapping(value = ["/trainer"])
    fun trainer(model: Model): String {
        if (!model.containsAttribute("trainerProcess")) {
            model.addAttribute("trainerProcess", TrainerProcess())
        }
        return "redirect:/trainer/classify"
    }

    @GetMapping(value = ["/trainer/add"])
    fun trainerAdd(model: Model): String {
        if (!model.containsAttribute("trainerProcess")) {
            model.addAttribute("trainerProcess", TrainerProcess())
        }
        return "trainer_add"
    }

    @PostMapping(value = ["/trainer/add"])
    fun uploadData(@RequestParam file: MultipartFile?, redirectAttributes: RedirectAttributes): String {
        trainer.persist(file?.contentType ?: "application/octet-stream", file?.bytes ?: ByteArray(0))
        return "redirect:/trainer/classifiy"
    }

    @GetMapping(value = ["/trainer/classify"])
    fun classifyUploadedData(model: Model): String {
        val process = getModel(null, manager.getTopics(), manager.getSender(), manager.getMetadata().toMetadata())
        model.addAttribute("trainerClassifyProcess", process)
        return if (process.ids.isNotEmpty()) {
            "redirect:/trainer/classify/${process.ids.first()}"
        } else {
            "trainer_classify"
        }
    }

    @GetMapping(value = ["/trainer/classify/{id}"])
    fun classifyUploadedData(model: Model, @PathVariable id: String): String {
        val process = getModel(id, manager.getTopics(), manager.getSender(), manager.getMetadata().toMetadata())
        val encode = String(Base64.getEncoder().encode(process.selected.content))
        val result = classificator.classify(contentType = process.selected.contentType, content = encode)
        process.selectedResult = result
        model.addAttribute("trainerClassifyProcess", process)
        return "trainer_classify"
    }

    private fun getModel(
        id: String?,
        topics: List<Topic>,
        sender: List<Sender>,
        metadata: List<Metadata>
    ): TrainerClassifyProcess {
        return TrainerClassifyProcess(
            ids = trainer.getOpenDocuments().map { it.id.toString() },
            selected = if (id.isNullOrEmpty()) Document() else trainer.getDocument(id),
            topics = topics.distinctBy { it.name }.sortedBy { it.name },
            sender = sender.distinctBy { it.name }.sortedBy { it.name },
            metadata = metadata.distinctBy { it.name }.sortedBy { it.name }
        )
    }

    @GetMapping(value = ["/trainer/topics"])
    fun topics(model: Model): String {
        val trainerProcess = TrainerProcess(
            version = manager.getVersion(),
            topics = manager.getTopics().distinctBy { it.name }.sortedBy { it.name })
        model.addAttribute("trainerProcess", trainerProcess)
        return "trainer_topics"
    }

    @GetMapping(value = ["/trainer/topics/{id}"])
    fun topicsDetail(model: Model, @PathVariable id: String): String {
        val topics = manager.getTopics().distinctBy { it.name }.sortedBy { it.name }
        val trainerProcess = TrainerProcess(
            version = manager.getVersion(),
            topics = topics,
            selected = topics.first { it.name == id })
        model.addAttribute("trainerProcess", trainerProcess)
        return "trainer_topics"
    }


    @GetMapping(value = ["/trainer/sender"])
    fun sender(model: Model): String {
        val trainerProcess = TrainerProcess(
            version = manager.getVersion(),
            sender = manager.getSender().distinctBy { it.name }.sortedBy { it.name })
        model.addAttribute("trainerProcess", trainerProcess)
        return "trainer_sender"
    }

    @GetMapping(value = ["/trainer/sender/{id}"])
    fun senderDetail(model: Model, @PathVariable id: String): String {
        val sender = manager.getSender().distinctBy { it.name }.sortedBy { it.name }
        val trainerProcess =
            TrainerProcess(version = manager.getVersion(),
                sender = sender,
                selected = sender.first { it.name == id })
        model.addAttribute("trainerProcess", trainerProcess)
        return "trainer_sender"
    }

    @GetMapping(value = ["/trainer/metadata"])
    fun metadata(model: Model): String {
        val trainerProcess = TrainerProcess(version = manager.getVersion(), metadata = manager.getMetadata().map {
            Metadata().apply {
                name = it.name
                tokenList = it.tokenList
                regexContent = it.regexContent
            }
        }.distinctBy { it.name }.sortedBy { it.name })
        model.addAttribute("trainerProcess", trainerProcess)
        return "trainer_metadata"
    }

    @GetMapping(value = ["/trainer/metadata/{id}"])
    fun metadataDetail(model: Model, @PathVariable id: String): String {
        val metadata = manager.getMetadata().toMetadata().distinctBy { it.name }.sortedBy { it.name }
        val trainerProcess =
            TrainerProcess(
                version = manager.getVersion(),
                metadata = metadata,
                selected = metadata.first { it.name == id })
        model.addAttribute("trainerProcess", trainerProcess)
        return "trainer_metadata"
    }

    @GetMapping(value = ["/trainer/regex"])
    fun regex(model: Model): String {
        val trainerProcess =
            TrainerProcess(version = manager.getVersion(), regex = manager.getMetadata().map {
                Regex().apply {
                    name = it.regexContent.firstOrNull()?.first ?: ""
                    regexContent = it.regexContent
                }
            }.distinctBy { it.name }.sortedBy { it.name }, selected = Regex())
        model.addAttribute("trainerProcess", trainerProcess)
        return "trainer_regex"
    }

    @GetMapping(value = ["/trainer/regex/{id}"])
    fun regexDetail(model: Model, @PathVariable id: String): String {
        val regex = manager.getMetadata().map {
            Regex().apply {
                name = it.regexContent.firstOrNull()?.first ?: ""
                regexContent = it.regexContent
            }
        }.distinctBy { it.name }.sortedBy { it.name }

        val trainerProcess =
            TrainerProcess(
                version = manager.getVersion(),
                regex = regex,
                selected = regex.first { it.name == id })
        model.addAttribute("trainerProcess", trainerProcess)
        return "trainer_regex"
    }
}

fun List<MetaDataToken>.toMetadata(): List<Metadata> {
    return this.map {
        Metadata().apply {
            name = it.name
            tokenList = it.tokenList
            regexContent = it.regexContent
        }
    }
}