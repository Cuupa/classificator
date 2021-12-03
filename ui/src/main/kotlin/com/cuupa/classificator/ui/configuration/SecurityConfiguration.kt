package com.cuupa.classificator.ui.configuration

import com.cuupa.classificator.externalconfiguration.Config
import com.cuupa.classificator.ui.handler.MonitorAccessDeniedHandler
import com.cuupa.classificator.ui.handler.MonitorAuthenticationFailureHandler
import org.apache.commons.logging.LogFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import javax.annotation.PostConstruct

@Configuration
open class SecurityConfiguration : WebSecurityConfigurerAdapter() {

    @Autowired
    private var configuration: Config? = null

    override fun configure(http: HttpSecurity) {
        http.csrf().disable()
            .authorizeRequests()
            .antMatchers("/").permitAll()
            .antMatchers("/css/**").permitAll()
            .antMatchers("/fonts/**").permitAll()
            .antMatchers("/js/**").permitAll()
            .antMatchers("/api/**").permitAll()
            .antMatchers("/guiProcess").permitAll()
            .antMatchers("/monitor", "/download").hasAnyRole("USER", "ADMIN")
            .antMatchers("/admin").hasAnyRole("ADMIN")
            .anyRequest().authenticated()
            .and()
            .formLogin()
            .loginPage("/login")
            .failureHandler(authenticationFailureHandler())
            .defaultSuccessUrl("/monitor")
            .failureUrl("/login?error")
            .permitAll()
            .and()
            .logout()
            .permitAll()
            .and()
            .exceptionHandling()
            .accessDeniedHandler(accessDeniedHandler())

        http.headers().frameOptions().sameOrigin()
    }

    private fun authenticationFailureHandler() = MonitorAuthenticationFailureHandler()

    @Bean
    open fun accessDeniedHandler() = MonitorAccessDeniedHandler()

    @Autowired
    fun configureGlobal(auth: AuthenticationManagerBuilder) {

        listOf<UserDetails>()

        val users = if (getMonitorUsername() == getAdminUsername()) {
            if (getMonitorPassword() != getAdminPassword()) {
                throw IllegalArgumentException("The user for accessing the monitor and the admin gui seems to be the same, but with different passwords.")
            }
            listOf(
                User.builder()
                    .username(getAdminUsername())
                    .password("{noop}${getAdminPassword()}")
                    .roles("ADMIN")
                    .build()
            )
        } else {
            listOf(
                User.builder()
                    .username(getMonitorUsername())
                    .password("{noop}${getMonitorPassword()}")
                    .roles("USER")
                    .build(),

                User.builder()
                    .username(getAdminUsername())
                    .password("{noop}${getAdminPassword()}")
                    .roles("ADMIN")
                    .build()
            )
        }
        val authentication = auth.inMemoryAuthentication()
        users.forEach { authentication.withUser(it) }
    }


    private fun getMonitorUsername() = configuration?.classificator?.monitorConfig?.username ?: ""

    private fun getMonitorPassword() = configuration?.classificator?.monitorConfig?.password ?: ""

    private fun getAdminPassword() = configuration?.classificator?.adminConfig?.password ?: ""

    private fun getAdminUsername() = configuration?.classificator?.adminConfig?.username ?: ""

    @PostConstruct
    fun configLoaded() {
        log.info("Loaded ${SecurityConfiguration::class.simpleName}")
    }

    companion object {
        private val log = LogFactory.getLog(SecurityConfiguration::class.java)
    }
}
