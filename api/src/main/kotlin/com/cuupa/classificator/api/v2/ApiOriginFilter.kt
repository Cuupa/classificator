package com.cuupa.classificator.api.v2

import java.io.IOException
import javax.annotation.Generated
import javax.servlet.*
import javax.servlet.http.HttpServletResponse

/**
 * @author Simon Thiel (https://github.com/cuupa) (29.05.2021)
 */
@Generated(value = ["io.swagger.codegen.v3.generators.java.SpringCodegen"], date = "2021-05-29T11:42:02.031Z[GMT]")
class ApiOriginFilter : Filter {

    @Throws(IOException::class, ServletException::class)
    override fun doFilter(
        request: ServletRequest, response: ServletResponse,
        chain: FilterChain
    ) {
        val res = response as HttpServletResponse
        res.addHeader("Access-Control-Allow-Origin", "*")
        res.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
        res.addHeader("Access-Control-Allow-Headers", "Content-Type")
        chain.doFilter(request, response)
    }

    override fun destroy() {}

    @Throws(ServletException::class)
    override fun init(filterConfig: FilterConfig) {
    }
}