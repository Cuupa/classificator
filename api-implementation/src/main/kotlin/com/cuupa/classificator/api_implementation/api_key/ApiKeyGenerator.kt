package com.cuupa.classificator.api_implementation.api_key

import com.cuupa.classificator.api_implementation.api_key.repository.ApiKeyRepository
import com.cuupa.classificator.api_implementation.api_key.repository.entity.ApiKeyEntity
import java.util.*

/**
 * @author Simon Thiel (https://github.com/cuupa) (30.05.2021)
 */
class ApiKeyGenerator(private val repository: ApiKeyRepository) {

    private fun get(assosiate: String): String {
        val uuid = UUID.randomUUID().toString()
        repository.save(ApiKeyEntity().apply {
            apiKey = uuid
            this.assosiate = assosiate
        })
        return uuid
    }
}