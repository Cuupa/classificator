package com.cuupa.classificator.ui.handler

import org.apache.commons.logging.LogFactory
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.access.AccessDeniedHandler
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class MonitorAccessDeniedHandler : AccessDeniedHandler {

    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        exception: AccessDeniedException?
    ) {
        val auth: Authentication? = SecurityContextHolder.getContext().authentication
        auth?.let { LOGGER.error("User '${auth.name}' attempted to access the protected URL: ${request.requestURI}", exception) }
        response.sendRedirect("${request.contextPath}/login")
    }

    companion object {
        private val LOGGER = LogFactory.getLog(MonitorAccessDeniedHandler::class.java)
    }
}
