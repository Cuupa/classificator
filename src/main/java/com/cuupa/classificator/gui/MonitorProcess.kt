package com.cuupa.classificator.gui

import com.cuupa.classificator.monitor.Event
import java.time.LocalDateTime

class MonitorProcess {
    var events: List<Event> = listOf()
    var from: LocalDateTime? = null
    var to: LocalDateTime? = null
}
