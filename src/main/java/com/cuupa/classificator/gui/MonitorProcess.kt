package com.cuupa.classificator.gui

import com.cuupa.classificator.monitor.Event
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate

class MonitorProcess {
    var events: List<Event> = listOf()

    @DateTimeFormat(style = "yyyy-MM-dd")
    var from: LocalDate? = null

    @DateTimeFormat(style = "yyyy-MM-dd")
    var to: LocalDate? = null
}
