package com.cuupa.classificator.controller

import com.cuupa.classificator.gui.GuiProcess
import com.cuupa.classificator.gui.MonitorProcess
import com.cuupa.classificator.monitor.Event
import com.cuupa.classificator.monitor.Monitor
import com.cuupa.classificator.services.Classificator
import com.cuupa.classificator.services.kb.KnowledgeManager
import com.google.gson.Gson
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.servlet.ModelAndView
import java.time.LocalDate

@Controller
class GuiController(private val classificator: Classificator, private val manager: KnowledgeManager) {

    @RequestMapping(value = ["/", "/index"], method = [RequestMethod.GET])
    fun index(model: Model): String {
        model.addAttribute("guiProcess", GuiProcess())
        return "index"
    }

    @RequestMapping(value = ["/guiProcess"], method = [RequestMethod.POST])
    fun guiProcess(@ModelAttribute guiProcess: GuiProcess, model: Model): String {
        val result = classificator.classify(guiProcess.inputText)
        guiProcess.result = result
        model.addAttribute("guiProcess", guiProcess)
        return "index"
    }

    @RequestMapping(value = ["/reloadKB"], method = [RequestMethod.POST])
    fun reloadKB(@ModelAttribute guiProcess: GuiProcess, model: Model): String {
        //manager.reloadKB()
        model.addAttribute("guiProcess", guiProcess)
        return "index"
    }
}