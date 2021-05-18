package com.cuupa.classificator.knowledgebase.services

import com.cuupa.classificator.knowledgebase.resultobjects.Metadata
import com.cuupa.classificator.knowledgebase.services.token.MetaDataToken

class MetadataService(private val metadataList: List<MetaDataToken>) {

    fun getMetadata(text: String): List<Metadata> {
        return metadataList.map { it.extract(text) }.flatten()
    }
}
