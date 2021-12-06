package com.cuupa.classificator.ui.controller

import com.cuupa.classificator.domain.Metadata
import com.cuupa.classificator.domain.Regex
import com.cuupa.classificator.engine.Classificator
import com.cuupa.classificator.engine.KnowledgeManager
import com.cuupa.classificator.engine.services.TextExtractor
import com.cuupa.classificator.trainer.services.Trainer
import com.cuupa.classificator.ui.TrainerProcess
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@Controller
class TrainerKnowledgebaseController(
    classificator: Classificator,
    manager: KnowledgeManager,
    trainer: Trainer,
    textExtractor: TextExtractor
) : TrainerController(classificator, manager, trainer, textExtractor) {

    @GetMapping(value = ["/trainer/topics"])
    fun topics(model: Model): String {
        val trainerProcess = TrainerProcess(
            version = manager.getVersion(),
            topics = manager.getTopics().distinctBy { it.name }.sortedBy { it.name })
        model.addAttribute("trainerProcess", trainerProcess)
        return "trainer/knowledgebase/trainer_topics"
    }

    @GetMapping(value = ["/trainer/topics/{id}"])
    fun topicsDetail(model: Model, @PathVariable id: String): String {
        val topics = manager.getTopics().distinctBy { it.name }.sortedBy { it.name }
        val trainerProcess = TrainerProcess(
            version = manager.getVersion(),
            topics = topics,
            selected = topics.first { it.name == id })
        model.addAttribute("trainerProcess", trainerProcess)
        return "trainer/knowledgebase/trainer_topics"
    }

    @GetMapping(value = ["/trainer/sender"])
    fun sender(model: Model): String {
        val trainerProcess = TrainerProcess(
            version = manager.getVersion(),
            sender = manager.getSender().distinctBy { it.name }.sortedBy { it.name })
        model.addAttribute("trainerProcess", trainerProcess)
        return "trainer/knowledgebase/trainer_sender"
    }

    @GetMapping(value = ["/trainer/sender/{id}"])
    fun senderDetail(model: Model, @PathVariable id: String): String {
        val sender = manager.getSender().distinctBy { it.name }.sortedBy { it.name }
        val trainerProcess =
            TrainerProcess(version = manager.getVersion(),
                sender = sender,
                selected = sender.first { it.name == id })
        model.addAttribute("trainerProcess", trainerProcess)
        return "trainer/knowledgebase/trainer_sender"
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
        return "trainer/knowledgebase/trainer_metadata"
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
        return "trainer/knowledgebase/trainer_metadata"
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
        return "trainer/knowledgebase/trainer_regex"
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
        return "$directory/trainer_regex"
    }

    companion object{
        const val directory = "trainer/knowledgebase/"
    }
}