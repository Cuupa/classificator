package com.cuupa.classificator.trainer.services

import com.cuupa.classificator.domain.SemanticResultData
import com.cuupa.classificator.trainer.services.statistics.MeasureType

class Trainer(private val documentService: DocumentService) {

    fun persist(contentType: String, bytes: ByteArray, plainText: String?, batchName: String?, timestamp: Long) {

        val document = listOf(
            Document(
                content = bytes,
                contentType = contentType,
                plainText = plainText ?: "",
                batchName = batchName ?: "Unnamed",
                timestamp = timestamp
            )
        )
        documentService.save(document)
    }

    fun getOpenDocuments() = documentService.getOpenDocuments()
    fun getDocument(id: String?) = documentService.find(id)
    fun getBatchNames() = documentService.getBatchNames()
    fun getBatch(id: String?) = documentService.getBatch(id)
    fun complete(document: Document) {
        documentService.complete(document)
    }

    fun removeBatch(id: String?) = documentService.removeBatch(id)

    fun getPrecision(semData: List<SemanticResultData>): Double {
        val type = MeasureType.getFor(semData.firstOrNull())
        val data = getData(type, semData)

        var precision = 0.0
        if (data.isEmpty()) {
            return precision
        }

        data.forEach {
            precision = precision.plus(documentService.getPrecision(it, type).precision)
        }

        return precision / data.size
    }

    private fun getData(type: MeasureType, semData: List<SemanticResultData>): List<String> {
        val data = when (type) {
            MeasureType.TOPIC -> {
                val expected = documentService.getExpectedTopics()
                val actual = documentService.getActualTopics()
                expected.filter { actual.contains(it) }
            }

            MeasureType.SENDER -> {
                val expected = documentService.getExpectedSender()
                val actual = documentService.getActualSender()
                expected.filter { actual.contains(it) }
            }

            MeasureType.METADATA -> {
                val expected = documentService.getExpectedMetadata()
                val actual = documentService.getActualMetadata()
                expected.filter { actual.contains(it) }
            }

            MeasureType.UNDEFINED -> listOf()
        }
        return semData.filter { data.contains(it.name) }.map { it.name }
    }

    fun getRecall(semData: List<SemanticResultData>): Double {
        val type = MeasureType.getFor(semData.firstOrNull())
        val data = getData(type, semData)

        var recall = 0.0
        if (data.isEmpty()) {
            return recall
        }

        data.forEach {
            recall = recall.plus(documentService.getRecall(it, type).recall)
        }

        return recall / data.size
    }

    fun getFScore(precision: Double, recall: Double): Double {
        val denominator = precision + recall

        if (denominator == 0.0) {
            return 0.0
        }
        val nominator = precision * recall
        val fracture = nominator / denominator
        return 2 * fracture
    }
}

