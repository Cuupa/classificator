package com.cuupa.classificator.engine.services

import com.cuupa.classificator.engine.services.token.MetaDataToken

class MetadataService(private val metadataList: List<MetaDataToken>) {

    fun getMetadata(text: String) = metadataList.map { it.extract(text) }.flatten()
}
