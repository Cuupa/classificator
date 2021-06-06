package com.cuupa.classificator.api_implementation.api_key

import com.cuupa.classificator.api_implementation.api_key.repository.ApiKeyRepository
import com.cuupa.classificator.externalconfiguration.Config

/**
 * @author Simon Thiel (https://github.com/cuupa) (30.05.2021)
 */
class ApiKeyValidator(private val config: Config, private val repository: ApiKeyRepository) {

    fun apiKeyValid(apiKey: String?): Boolean {
        if (!config.classificator!!.isApiKeyRequired()) {
            return true
        }

        if (apiKey.isNullOrBlank()) {
            return false;
        }
        return repository.findById(apiKey).isPresent
    }
}