package com.cuupa.classificator.ui.controller

import com.cuupa.classificator.monitor.service.Event
import com.google.gson.Gson
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import javax.servlet.http.HttpServletResponse
import com.cuupa.classificator.monitor.service.Monitor
import com.cuupa.classificator.ui.MonitorProcess

@Controller
class MonitorController(private val monitor: Monitor, private val gson: Gson) {

    @RequestMapping(value = ["/monitor"], method = [RequestMethod.GET])
    fun monitor(model: Model): ModelAndView {
        val monitorProcess = MonitorProcess()
        monitorProcess.events = load(monitorProcess)
        val modelAndView = ModelAndView("monitor")
        val statistics = monitor.getStatistics(null, null)
        modelAndView.addObject("topics", gson.toJson(statistics.topicDistribution))
        modelAndView.addObject("senders", gson.toJson(statistics.senderDistribution))
        modelAndView.addObject("processingHistory", gson.toJson(statistics.processingHistory))
        model.addAttribute("monitorProcess", monitorProcess)
        return modelAndView
    }

    @RequestMapping(value = ["/login"], method = [RequestMethod.GET])
    fun login() = "login"

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

    @GetMapping(value = ["/download"], produces = [MediaType.APPLICATION_OCTET_STREAM_VALUE])
    @ResponseBody
    fun download(@RequestParam(name = "start") start: Long, @RequestParam(name = "end") end: Long, response: HttpServletResponse): HttpEntity<ByteArray> {
        val startLocalDateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(start), TimeZone.getDefault().toZoneId())
        val endLocalDateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(end), TimeZone.getDefault().toZoneId())
        val event = monitor.getEvents(startLocalDateTime.toLocalDate(), endLocalDateTime.toLocalDate())
            .firstOrNull { it.start.withNano(0) == startLocalDateTime && it.end.withNano(0) == endLocalDateTime }
        val httpHeaders = HttpHeaders()
        event?.let {
            httpHeaders.contentType = MediaType.APPLICATION_OCTET_STREAM
            response.addHeader("Content-Disposition", "attachment; filename=${start}_$end.csv")
            val content = Event.headlines() + event.toCsvString()
            return HttpEntity<ByteArray>(content.encodeToByteArray(), httpHeaders)
        }

        return HttpEntity<ByteArray>(ByteArray(0), httpHeaders)
    }
}
