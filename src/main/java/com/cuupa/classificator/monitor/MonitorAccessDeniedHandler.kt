package com.cuupa.classificator.monitor

import org.apache.juli.logging.LogFactory
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.access.AccessDeniedHandler
import java.lang.Exception
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class MonitorAccessDeniedHandler : AccessDeniedHandler {

    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        exception: AccessDeniedException?
    ) {
        val auth: Authentication? = SecurityContextHolder.getContext().authentication
        if (auth != null) {
            LOGGER.error(
                "User '${auth.name}' attempted to access the protected URL: ${request.requestURI}"
            )
        }
        response.sendRedirect("${request.contextPath}/login")
    }

    companion object {
        private val LOGGER = LogFactory.getLog(MonitorAccessDeniedHandler::class.java)
    }
}
