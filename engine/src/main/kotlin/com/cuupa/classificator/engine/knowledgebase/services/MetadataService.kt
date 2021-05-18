package com.cuupa.classificator.engine.knowledgebase.services

import com.cuupa.classificator.domain.Metadata
import com.cuupa.classificator.engine.knowledgebase.services.token.MetaDataToken

class MetadataService(private val metadataList: List<MetaDataToken>) {

    fun getMetadata(text: String): List<Metadata> {
        return metadataList.map { it.extract(text) }.flatten()
    }
}
