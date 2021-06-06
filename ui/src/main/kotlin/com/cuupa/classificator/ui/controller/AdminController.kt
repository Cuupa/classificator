package com.cuupa.classificator.ui.controller

import com.cuupa.classificator.api_implementation.api_key.repository.ApiKeyRepository
import com.cuupa.classificator.api_implementation.api_key.repository.entity.ApiKeyEntity
import com.cuupa.classificator.ui.AdminProcess
import org.apache.commons.logging.LogFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
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
    fun admin(model: Model): ModelAndView {
        val adminProcess: AdminProcess = if (model.containsAttribute("adminProcess")) {
            model.getAttribute("adminProcess") as AdminProcess
        } else {
            AdminProcess()
        }.apply { apiUsers = apiKeyRepository.findAll().mapNotNull { it.assosiate } }

        model.addAttribute("adminProcess", adminProcess)
        return ModelAndView("admin")
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
        @ModelAttribute adminProcess: AdminProcess, model: Model
    ): ModelAndView {
        try {
            if (adminProcess.assosiate.isBlank()) {
                handleError(adminProcess, model)
                return ModelAndView("admin")
            }

            val exists = apiKeyRepository.findAll().find { it.assosiate == adminProcess.assosiate } != null
            if (exists) {
                handleError(adminProcess, model)
                return ModelAndView("admin")
            }

            val uuid = UUID.randomUUID().toString().also { adminProcess.generatedApiKey = it }
            apiKeyRepository.save(ApiKeyEntity().apply {
                apiKey = uuid
                assosiate = adminProcess.assosiate
            })

            adminProcess.apiUsers = apiKeyRepository.findAll().mapNotNull { it.assosiate }
        } catch (e: Exception) {
            log.error(e)
            handleError(adminProcess, model)
        }
        model.addAttribute("adminProcess", adminProcess)
        return ModelAndView("admin")
    }

    private fun handleError(adminProcess: AdminProcess, model: Model) {
        log.error("99. Handle error")
        adminProcess.error = true
        log.error("999. Set error = true")
        adminProcess.apiUsers = apiKeyRepository.findAll().mapNotNull { it.assosiate }
        log.error("999. Stored API keys in gui object")
        model.addAttribute("adminProcess", adminProcess)
        log.error("9999. Stored GUI object in model")
    }

    companion object {
        private val log = LogFactory.getLog(AdminController::class.java)
    }
}