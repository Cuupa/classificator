package com.cuupa.classificator.monitor.service

class MonitorStatistics {

    // domain statistics
    var topicDistribution: Map<String, Int> = mapOf()

    var senderDistribution: Map<String, Int> = mapOf()

    // technical statistics
    var maxProcessingTime: Long = 0

    var minProcessingTime: Long = 0

    var averageProcessingTime: Long = 0

    var averageTextLength: Long = 0

    var processingHistory: Map<String, Double> = mapOf()
}
