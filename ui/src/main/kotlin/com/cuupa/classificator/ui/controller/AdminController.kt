package com.cuupa.classificator.ui.controller

import com.cuupa.classificator.api_implementation.api_key.repository.ApiKeyRepository
import com.cuupa.classificator.ui.AdminProcess
import org.apache.commons.logging.LogFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView
import javax.servlet.http.HttpServletResponse

/**
 * @author Simon Thiel (https://github.com/cuupa) (30.05.2021)
 */
@Controller
class AdminController(private val apiKeyRepository: ApiKeyRepository) {

    @RequestMapping(value = ["/admin"], method = [RequestMethod.GET])
    fun admin(model: Model): ModelAndView {
        val adminProcess = AdminProcess().apply { apiUsers = apiKeyRepository.findAll().mapNotNull { it.assosiate } }
        val modelAndView = ModelAndView("admin")
        model.addAttribute("adminProcess", adminProcess)
        return modelAndView
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

    companion object {
        private val log = LogFactory.getLog(AdminController::class.java)
    }
}