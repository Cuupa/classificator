package com.cuupa.classificator.ui.controller

import com.cuupa.classificator.engine.Classificator
import com.cuupa.classificator.engine.KnowledgeManager
import com.cuupa.classificator.ui.GuiProcess
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

/**
 * @author Simon Thiel (https://github.com/cuupa)
 */
@Controller
class GuiController(private val classificator: Classificator, private val manager: KnowledgeManager) {

    @RequestMapping(value = ["/"], method = [RequestMethod.GET])
    fun index(model: Model): String {
        if (!model.containsAttribute("guiProcess")) {
            model.addAttribute("guiProcess", GuiProcess(null, null))
        }
        return "index"
    }

    @RequestMapping(value = ["/guiProcess"], method = [RequestMethod.POST])
    fun guiProcess(@ModelAttribute guiProcess: GuiProcess, model: Model): String {
        guiProcess.result = classificator.classify(guiProcess.inputText)
        model.addAttribute("guiProcess", guiProcess)
        return "index"
    }

    @RequestMapping(value = ["/login"], method = [RequestMethod.GET])
    fun login() = "login"
}