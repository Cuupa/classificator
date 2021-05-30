package com.cuupa.classificator.ui.controller

import com.cuupa.classificator.engine.ClassificatorImplementation
import com.cuupa.classificator.engine.KnowledgeManager
import com.cuupa.classificator.ui.GuiProcess
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

@Controller
class GuiController(private val classificator: ClassificatorImplementation, private val manager: KnowledgeManager) {

    @RequestMapping(value = ["/", "/index"], method = [RequestMethod.GET])
    fun index(model: Model): String {
        model.addAttribute("guiProcess", GuiProcess(null, null))
        return "index"
    }

    @RequestMapping(value = ["/guiProcess"], method = [RequestMethod.POST])
    fun guiProcess(@ModelAttribute guiProcess: GuiProcess, model: Model): String {
        guiProcess.result = classificator.classify(guiProcess.inputText)
        model.addAttribute("guiProcess", guiProcess)
        return "index"
    }

    @RequestMapping(value = ["/reloadKB"], method = [RequestMethod.POST])
    fun reloadKB(@ModelAttribute guiProcess: GuiProcess, model: Model): String {
        //manager.reloadKB()
        model.addAttribute("guiProcess", guiProcess)
        return "index"
    }

    @RequestMapping(value = ["/login"], method = [RequestMethod.GET])
    fun login() = "login"
}