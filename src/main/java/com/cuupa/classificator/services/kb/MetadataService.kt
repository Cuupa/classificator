package com.cuupa.classificator.services.kb

import com.cuupa.classificator.services.kb.semantic.Metadata
import com.cuupa.classificator.services.kb.semantic.token.MetaDataToken

class MetadataService(private val metadataList: List<MetaDataToken>) {

    fun getMetadata(text: String): List<Metadata> {
        return metadataList.map { it.extract(text) }.flatten()
    }
}
