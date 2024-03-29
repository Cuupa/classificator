package com.cuupa.classificator.ui.controller

import com.cuupa.classificator.api_implementation.api_key.repository.ApiKeyRepository
import com.cuupa.classificator.api_implementation.api_key.repository.entity.ApiKeyEntity
import com.cuupa.classificator.ui.AdminProcess
import org.apache.commons.logging.LogFactory
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView
import java.util.*
import javax.servlet.http.HttpServletResponse

/**
 * @author Simon Thiel (https://github.com/cuupa) (30.05.2021)
 */
@Controller
class AdminController(private val apiKeyRepository: ApiKeyRepository) {

    @RequestMapping(value = ["/admin"], method = [RequestMethod.GET])
    fun admin(): ModelAndView {
        val adminProcess = AdminProcess().apply { apiUsers = apiKeyRepository.findAll().mapNotNull { it.assosiate } }
        return ModelAndView("admin").apply { addObject("adminProcess", adminProcess) }
    }

    @GetMapping(value = ["/admin/revoke{id}"])
    @ResponseBody
    fun revoke(
        @RequestParam(name = "id") id: String,
        response: HttpServletResponse
    ): ModelAndView {
        try {
            val key = apiKeyRepository.findAll().first { it.assosiate == id }.apiKey
            apiKeyRepository.deleteById(key)
        } catch (e: Exception) {
            log.error(e)
        }
        return ModelAndView("redirect:/admin")
    }

    @PostMapping(value = ["/admin/create"])
    fun create(
        @ModelAttribute adminProcess: AdminProcess
    ): ModelAndView {
        val modelAndView = ModelAndView("admin")
        try {

            if (adminProcess.associate.isBlank()) {
                return handleError(adminProcess, modelAndView)
            }

            val exists = apiKeyRepository.findAll().find { it.assosiate == adminProcess.associate } != null
            if (exists) {
                return handleError(adminProcess, modelAndView)
            }

            val uuid = UUID.randomUUID().toString().also { adminProcess.generatedApiKey = it }
            apiKeyRepository.save(ApiKeyEntity().apply {
                apiKey = uuid
                assosiate = adminProcess.associate
            })

            adminProcess.apiUsers = apiKeyRepository.findAll().mapNotNull { it.assosiate }
        } catch (e: Exception) {
            log.error(e)
            return handleError(adminProcess, modelAndView)
        }
        return modelAndView.apply { addObject("adminProcess", adminProcess) }
    }

    private fun handleError(adminProcess: AdminProcess, modelAndView: ModelAndView): ModelAndView {
        adminProcess.error = true
        adminProcess.apiUsers = apiKeyRepository.findAll().mapNotNull { it.assosiate }
        modelAndView.addObject("adminProcess", adminProcess)
        return modelAndView
    }

    companion object {
        private val log = LogFactory.getLog(AdminController::class.java)
    }
}