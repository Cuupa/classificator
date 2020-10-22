package com.cuupa.classificator.controller

import com.cuupa.classificator.gui.GuiProcess
import com.cuupa.classificator.gui.MonitorProcess
import com.cuupa.classificator.monitor.Event
import com.cuupa.classificator.monitor.Monitor
import com.cuupa.classificator.services.Classificator
import com.cuupa.classificator.services.kb.KnowledgeManager
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import java.time.LocalDateTime

@Controller
class GuiController(private val classificator: Classificator, private val manager: KnowledgeManager,
                    private val monitor: Monitor) {

    @RequestMapping(value = ["/", "/index"], method = [RequestMethod.GET])
    fun index(model: Model): String {
        model.addAttribute("guiProcess", GuiProcess())
        return "index"
    }

    @RequestMapping(value = ["/guiProcess"], method = [RequestMethod.POST])
    fun guiProcess(@ModelAttribute guiProcess: GuiProcess): String {
        val result = classificator.classify(guiProcess.inputText)
        guiProcess.result = result
        return "index"
    }

    @RequestMapping(value = ["/reloadKB"], method = [RequestMethod.POST])
    fun reloadKB(): String {
        manager.reloadKB()
        return "index"
    }

    @RequestMapping(value = ["/monitor"], method = [RequestMethod.GET])
    fun monitor(@ModelAttribute monitorProcess: MonitorProcess): String {
        val start: LocalDateTime? = monitorProcess.from
        val end: LocalDateTime? = monitorProcess.to
        val listOfEvents: List<Event> = monitor.getEvents(start, end)
        monitorProcess.events = listOfEvents
        return "monitor"
    }
}