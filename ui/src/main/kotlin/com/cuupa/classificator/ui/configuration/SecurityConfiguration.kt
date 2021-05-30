package com.cuupa.classificator.ui.configuration

import com.cuupa.classificator.externalconfiguration.Config
import com.cuupa.classificator.ui.handler.MonitorAccessDeniedHandler
import com.cuupa.classificator.ui.handler.MonitorAuthenticationFailureHandler
import org.apache.commons.logging.LogFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.User
import javax.annotation.PostConstruct

@Configuration
open class SecurityConfiguration : WebSecurityConfigurerAdapter() {

    @Autowired
    private var configuration: Config? = null

    @Value("\${classificator.monitor.username}")
    private var monitorUsername: String? = null

    @Value("\${classificator.monitor.password}")
    private var monitorPassword: String? = null

    @Value("\${classificator.admin.username}")
    private var adminUsername: String? = null

    @Value("\${classificator.admin.password}")
    private var adminPassword: String? = null


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
        val monitorUser = User.builder()
            .username(getMonitorUsername())
            .password("{noop}${getMonitorPassword()}")
            .roles("USER")
            .build()

        val adminUser = User.builder()
            .username(getAdminUsername())
            .password("{noop}${getAdminPassword()}")
            .roles("ADMIN")
            .build()

        auth.inMemoryAuthentication().withUser(monitorUser).withUser(adminUser)
    }

    private fun getMonitorUsername(): String {
        return if (monitorUsername.isNullOrEmpty()) {
            configuration?.classificator?.monitorConfig?.username ?: ""
        } else {
            monitorUsername ?: ""
        }
    }


    private fun getMonitorPassword(): String {
        return if (monitorPassword.isNullOrEmpty()) {
            configuration?.classificator?.monitorConfig?.password ?: ""
        } else {
            monitorPassword ?: ""
        }
    }

    private fun getAdminUsername(): String {
        return if (adminUsername.isNullOrEmpty()) {
            configuration?.classificator?.adminConfig?.username ?: ""
        } else {
            adminUsername ?: ""
        }
    }


    private fun getAdminPassword(): String {
        return if (adminPassword.isNullOrEmpty()) {
            configuration?.classificator?.adminConfig?.password ?: ""
        } else {
            adminPassword ?: ""
        }
    }

    @PostConstruct
    fun configLoaded() {
        log.info("Loaded ${SecurityConfiguration::class.simpleName}")
    }

    companion object {
        private val log = LogFactory.getLog(SecurityConfiguration::class.java)
    }
}
