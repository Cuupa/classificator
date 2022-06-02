package com.cuupa.classificator.ui.controller

import com.cuupa.classificator.engine.Classificator
import com.cuupa.classificator.engine.KnowledgeManager
import com.cuupa.classificator.engine.services.application.InfoService
import com.cuupa.classificator.ui.GuiProcess
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.servlet.ModelAndView

/**
 * @author Simon Thiel (https://github.com/cuupa)
 */
@Controller
class GuiController(
    private val classificator: Classificator,
    private val manager: KnowledgeManager,
    private val infoService: InfoService
) {

    @RequestMapping(value = ["/"], method = [RequestMethod.GET])
    fun index(): ModelAndView {
        return ModelAndView("index").apply {
            addObject("guiProcess", GuiProcess(null, null))
            addObject("kb_version", manager.getVersion())
            addObject("application_version", infoService.getVersion())
        }
    }

    @RequestMapping(value = ["/guiProcess"], method = [RequestMethod.POST])
    fun guiProcess(@ModelAttribute guiProcess: GuiProcess): ModelAndView {
        guiProcess.result = classificator.classify(guiProcess.inputText)
        return ModelAndView("index").apply {
            addObject("guiProcess", guiProcess)
            addObject("kb_version", manager.getVersion())
            addObject("application_version", infoService.getVersion())
        }
    }

    @RequestMapping(value = ["/login"], method = [RequestMethod.GET])
    fun login(): ModelAndView {
        return ModelAndView("login").apply {
            addObject("kb_version", manager.getVersion())
            addObject("application_version", infoService.getVersion())
        }
    }
}