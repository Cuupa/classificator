package com.cuupa.classificator.api_implementation.api_key.repository

import com.cuupa.classificator.api_implementation.api_key.repository.entity.ApiKeyEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * @author Simon Thiel (https://github.com/cuupa) (30.05.2021)
 */
@Repository
interface ApiKeyRepository : JpaRepository<ApiKeyEntity, String>