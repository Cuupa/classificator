package com.cuupa.classificator.api_implementation.api_key.repository.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id

/**
 * @author Simon Thiel (https://github.com/cuupa) (30.05.2021)
 */
@Entity
class ApiKeyEntity {

    @Id
    var apiKey: String? = null

    @Column(unique = true)
    var assosiate: String? = null
}