package com.cuupa.classificator.controller

import com.cuupa.classificator.gui.MonitorProcess
import com.cuupa.classificator.monitor.Event
import com.cuupa.classificator.monitor.Monitor
import com.google.gson.Gson
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.servlet.ModelAndView
import java.time.LocalDate

@Controller
class MonitorController(private val monitor: Monitor, private val gson: Gson) {

    @RequestMapping(value = ["/monitor"], method = [RequestMethod.GET])
    fun monitor(): ModelAndView {
        val monitorProcess = MonitorProcess()
        monitorProcess.events = load(monitorProcess)
        val modelAndView = ModelAndView("monitor")
        val statistics = monitor.getStatistics(null, null)
        modelAndView.addObject("topics", gson.toJson(statistics.topicDistribution))
        modelAndView.addObject("senders", gson.toJson(statistics.senderDistribution))
        return modelAndView
    }

    @RequestMapping(value = ["/monitorWithFilter"], method = [RequestMethod.POST])
    fun monitorWithFilter(@ModelAttribute monitorProcess: MonitorProcess, model: Model): String {
        monitorProcess.events = load(monitorProcess)
        model.addAttribute("monitorProcess", monitorProcess)
        return "monitor"
    }

    @RequestMapping(value = ["/exportAsPdf"], method = [RequestMethod.POST])
    fun exportAsPdf(@ModelAttribute monitorProcess: MonitorProcess) {
        val start: LocalDate? = monitorProcess.from
        val end: LocalDate? = monitorProcess.to
        val statistics = monitor.getStatistics(start, end)
    }

    private fun load(monitorProcess: MonitorProcess): List<Event> {
        val start: LocalDate? = monitorProcess.from
        val end: LocalDate? = monitorProcess.to
        return monitor.getEvents(start, end)
    }
}