package com.cuupa.classificator.engine.services

import com.cuupa.classificator.domain.Metadata
import com.cuupa.classificator.engine.services.token.MetaDataToken

class MetadataService(private val metadataList: List<MetaDataToken>) {

    fun getMetadata(text: String): List<Metadata> {
        return metadataList.map { it.extract(text) }.flatten()
    }
}
