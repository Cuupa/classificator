package com.cuupa.classificator.api_implementation.api_key

import com.cuupa.classificator.api_implementation.api_key.repository.ApiKeyRepository
import com.cuupa.classificator.api_implementation.api_key.repository.entity.ApiKeyEntity
import com.cuupa.classificator.externalconfiguration.ClassificatorConfig
import com.cuupa.classificator.externalconfiguration.Config
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.anyString
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * @author Simon Thiel (https://github.com/cuupa) (30.05.2021)
 */
@ExtendWith(MockitoExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class ApiKeyValidatorTest {

    private var unitToTest: ApiKeyValidator? = null

    @Mock
    private var config: ClassificatorConfig? = null

    @Mock
    private var repository: ApiKeyRepository? = null

    @Test
    fun shouldMockitoCorrectlyInitialized() {
        assertNotNull(unitToTest)
        assertNotNull(config)
        assertNotNull(repository)
    }

    @Test
    fun shouldReturnTrueIfCheckDisabled() {
        unitToTest = ApiKeyValidator(Config().apply { classificator = config }, repository!!)

        `when`(config?.isApiKeyRequired()).thenReturn(false)

        val result = unitToTest?.apiKeyValid(null) ?: false
        assertTrue(result)
    }

    @Test
    fun shouldApiKeyNullInvalid() {
        unitToTest = ApiKeyValidator(Config().apply { classificator = config }, repository!!)

        `when`(config?.isApiKeyRequired()).thenReturn(true)

        val result = unitToTest?.apiKeyValid(null) ?: true
        assertFalse(result)
    }

    @Test
    fun shouldApiKeyEmptyInvalid() {
        unitToTest = ApiKeyValidator(Config().apply { classificator = config }, repository!!)

        `when`(config?.isApiKeyRequired()).thenReturn(true)

        val result = unitToTest?.apiKeyValid("") ?: true
        assertFalse(result)
    }

    @Test
    fun shouldApiKeyBlankInvalid() {
        unitToTest = ApiKeyValidator(Config().apply { classificator = config }, repository!!)

        `when`(config?.isApiKeyRequired()).thenReturn(true)

        val result = unitToTest?.apiKeyValid(" ") ?: true
        assertFalse(result)
    }

    @Test
    fun shouldApiKeyNotRegisteredInvalid() {
        unitToTest = ApiKeyValidator(Config().apply { classificator = config }, repository!!)

        `when`(config?.isApiKeyRequired()).thenReturn(true)
        `when`(repository?.findById(anyString())).thenReturn(Optional.empty())

        val result = unitToTest?.apiKeyValid("ABCDEFG") ?: true
        assertFalse(result)
    }

    @Test
    fun shouldApiKeyRegisteredValid() {
        unitToTest = ApiKeyValidator(Config().apply { classificator = config }, repository!!)

        `when`(config?.isApiKeyRequired()).thenReturn(true)
        `when`(repository?.findById(anyString())).thenReturn(Optional.of(ApiKeyEntity()))

        val result = unitToTest?.apiKeyValid("ABCDEFG") ?: true
        assertTrue(result)
    }
}