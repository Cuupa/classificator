package com.cuupa.classificator.ui.controller

import com.cuupa.classificator.domain.Metadata
import com.cuupa.classificator.domain.Regex
import com.cuupa.classificator.engine.Classificator
import com.cuupa.classificator.engine.KnowledgeManager
import com.cuupa.classificator.engine.extensions.Extension.toMetadata
import com.cuupa.classificator.engine.services.TextExtractor
import com.cuupa.classificator.engine.services.application.InfoService
import com.cuupa.classificator.trainer.services.Trainer
import com.cuupa.classificator.ui.TrainerProcess
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.servlet.ModelAndView

@Controller
class TrainerKnowledgebaseController(
    private val classificator: Classificator,
    private val manager: KnowledgeManager,
    private val trainer: Trainer,
    private val textExtractor: TextExtractor,
    private val infoService: InfoService
) {

    @GetMapping(value = ["/trainer/topics"])
    fun topics(): ModelAndView {
        val trainerProcess = TrainerProcess(
            version = manager.getVersion(),
            topics = manager.getTopics().distinctBy { it.name }.sortedBy { it.name })
        return ModelAndView("${directory}trainer_topics").apply {
            addObject("trainerProcess", trainerProcess)
            addObject("kb_version", manager.getVersion())
            addObject("application_version", infoService.getVersion())
        }
    }

    @GetMapping(value = ["/trainer/topics/{id}"])
    fun topicsDetail(@PathVariable id: String): ModelAndView {
        val topics = manager.getTopics().distinctBy { it.name }.sortedBy { it.name }
        val trainerProcess = TrainerProcess(
            version = manager.getVersion(),
            topics = topics,
            selected = topics.first { it.name == id })
        return ModelAndView("${directory}trainer_topics").apply {
            addObject("trainerProcess", trainerProcess)
            addObject("kb_version", manager.getVersion())
            addObject("application_version", infoService.getVersion())
        }
    }

    @GetMapping(value = ["/trainer/sender"])
    fun sender(): ModelAndView {
        val trainerProcess = TrainerProcess(
            version = manager.getVersion(),
            sender = manager.getSender().distinctBy { it.name }.sortedBy { it.name })
        return ModelAndView("${directory}trainer_sender").apply {
            addObject("trainerProcess", trainerProcess)
            addObject("kb_version", manager.getVersion())
            addObject("application_version", infoService.getVersion())
        }
    }

    @GetMapping(value = ["/trainer/sender/{id}"])
    fun senderDetail(@PathVariable id: String): ModelAndView {
        val sender = manager.getSender().distinctBy { it.name }.sortedBy { it.name }
        val trainerProcess =
            TrainerProcess(version = manager.getVersion(),
                sender = sender,
                selected = sender.first { it.name == id })
        return ModelAndView("${directory}trainer_sender").apply {
            addObject("trainerProcess", trainerProcess)
            addObject("kb_version", manager.getVersion())
            addObject("application_version", infoService.getVersion())
        }
    }

    @GetMapping(value = ["/trainer/metadata"])
    fun metadata(): ModelAndView {
        val trainerProcess = TrainerProcess(version = manager.getVersion(), metadata = manager.getMetadata().map {
            Metadata().apply {
                name = it.name
                tokenList = it.tokenList
                regexContent = it.regexContent
            }
        }.distinctBy { it.name }.sortedBy { it.name })
        return ModelAndView("${directory}trainer_metadata").apply {
            addObject("trainerProcess", trainerProcess)
            addObject("kb_version", manager.getVersion())
            addObject("application_version", infoService.getVersion())
        }
    }

    @GetMapping(value = ["/trainer/metadata/{id}"])
    fun metadataDetail(@PathVariable id: String): ModelAndView {
        val metadata = manager.getMetadata().toMetadata().distinctBy { it.name }.sortedBy { it.name }
        val trainerProcess =
            TrainerProcess(
                version = manager.getVersion(),
                metadata = metadata,
                selected = metadata.first { it.name == id })
        return ModelAndView("${directory}trainer_metadata").apply {
            addObject("trainerProcess", trainerProcess)
            addObject("kb_version", manager.getVersion())
            addObject("application_version", infoService.getVersion())
        }
    }

    @GetMapping(value = ["/trainer/regex"])
    fun regex(): ModelAndView {
        val trainerProcess =
            TrainerProcess(version = manager.getVersion(), regex = manager.getMetadata().map {
                Regex().apply {
                    name = it.regexContent.firstOrNull()?.first ?: ""
                    regexContent = it.regexContent
                }
            }.distinctBy { it.name }.sortedBy { it.name }, selected = Regex())
        return ModelAndView("${directory}trainer_regex").apply {
            addObject("trainerProcess", trainerProcess)
            addObject("kb_version", manager.getVersion())
            addObject("application_version", infoService.getVersion())
        }
    }

    @GetMapping(value = ["/trainer/regex/{id}"])
    fun regexDetail(@PathVariable id: String): ModelAndView {
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
        return ModelAndView("${directory}trainer_regex").apply {
            addObject("trainerProcess", trainerProcess)
            addObject("kb_version", manager.getVersion())
            addObject("application_version", infoService.getVersion())
        }
    }

    companion object {
        const val directory = "trainer/knowledgebase/"
    }
}