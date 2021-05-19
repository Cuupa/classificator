package com.cuupa.classificator.ui.handler

import org.apache.commons.logging.LogFactory
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class MonitorAuthenticationFailureHandler : AuthenticationFailureHandler {

    override fun onAuthenticationFailure(
        request: HttpServletRequest,
        response: HttpServletResponse,
        exception: AuthenticationException
    ) {
        val auth: Authentication? = SecurityContextHolder.getContext().authentication
        auth?.let { LOGGER.error("User '${it.name}:${it.credentials}' attempted to access the protected URL: ${request.requestURI}", exception) }
    }

    companion object {
        private val LOGGER = LogFactory.getLog(MonitorAuthenticationFailureHandler::class.java)
    }
}
