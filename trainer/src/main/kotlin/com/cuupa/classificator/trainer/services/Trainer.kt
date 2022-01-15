package com.cuupa.classificator.trainer.services

import com.cuupa.classificator.domain.SemanticResultData
import com.cuupa.classificator.trainer.services.statistics.MeasureType
import com.cuupa.classificator.trainer.services.statistics.Measures

class Trainer(private val documentService: DocumentService) {

    fun persist(documents: List<Document>) {
        documentService.save(documents)
    }

    fun getOpenDocuments() = documentService.getOpenDocuments()
    fun getDocument(id: String?) = documentService.find(id)
    fun getBatchNames() = documentService.getBatchNames()
    fun getBatch(id: String?) = documentService.getBatch(id)
    fun complete(document: Document) {
        documentService.complete(document)
    }

    fun removeBatch(id: String?) = documentService.removeBatch(id)

    private fun getPrecision(semData: List<SemanticResultData>): Double {
        val type = MeasureType.getFor(semData.firstOrNull())
        val data = getData(type)

        var precision = 0.0
        if (data.isEmpty()) {
            return precision
        }

        data.forEach {
            precision = precision.plus(documentService.getPrecision(it, type).precision)
        }

        return precision / data.size
    }

    private fun getData(type: MeasureType): List<String> {
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
        return data
//        return semData.filter { data.contains(it.name) }.map { it.name }
    }

    private fun getRecall(semData: List<SemanticResultData>): Double {
        val type = MeasureType.getFor(semData.firstOrNull())
        val data = getData(type)

        var recall = 0.0
        if (data.isEmpty()) {
            return recall
        }

        data.forEach {
            recall = recall.plus(documentService.getRecall(it, type).recall)
        }

        return recall / data.size
    }

    private fun getFScore(precision: Double, recall: Double): Double {
        val denominator = precision + recall

        if (denominator == 0.0) {
            return 0.0
        }
        val nominator = precision * recall
        val fracture = nominator / denominator
        return 2 * fracture
    }

    private fun getNumberOfDocuments(): Int {
        return documentService.listDone().size
    }

    private fun getNumberOfCorrect(value: List<SemanticResultData>): Int {
        val type = MeasureType.getFor(value.firstOrNull())
        val list = documentService.listDone()

        return when (type) {
            MeasureType.TOPIC -> list.filter { it.expectedTopics == it.actualTopics }.size
            MeasureType.SENDER -> list.filter { it.expectedSenders == it.actualSenders }.size
            MeasureType.METADATA -> list.filter { it.expectedMetadata == it.actualMetadata }.size
            MeasureType.UNDEFINED -> -1
        }
    }

    private fun getNumberOfIncorrect(value: List<SemanticResultData>): Int {
        val type = MeasureType.getFor(value.firstOrNull())
        val list = documentService.list()

        return when (type) {
            MeasureType.TOPIC -> {
                list.filter { it.expectedTopics != it.actualTopics }
                    .size
            }
            MeasureType.SENDER -> {
                list.filter { it.expectedSenders != it.actualSenders }
                    .size
            }
            MeasureType.METADATA -> {
                list.filter { it.expectedMetadata != it.actualMetadata }
                    .size
            }
            MeasureType.UNDEFINED -> -1
        }
    }

    fun getMeasures(value: List<SemanticResultData>) = Measures().apply {
        precision = getPrecision(value)
        recall = getRecall(value)
        fScore = getFScore(precision, recall)
        numberOfDocuments = getNumberOfDocuments()
        correct = getNumberOfCorrect(value)
        incorrect = getNumberOfIncorrect(value)
    }
}
