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
import javax.annotation.PostConstruct

@Configuration
class SecurityConfiguration : WebSecurityConfigurerAdapter() {

    @Autowired
    private var configuration: Config? = null

    @Value("\${classificator.monitor.username}")
    private var username: String? = null

    @Value("\${classificator.monitor.password}")
    private var password: String? = null

    override fun configure(http: HttpSecurity) {
        http.csrf().disable()
            .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/css/**").permitAll()
                .antMatchers("/fonts/**").permitAll()
                .antMatchers("/js/**").permitAll()
                .antMatchers("/api/**").permitAll()
                .antMatchers("/guiProcess").permitAll()
                .antMatchers("/monitor", "/download").hasAnyRole("USER")
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
    fun accessDeniedHandler() = MonitorAccessDeniedHandler()

    @Autowired
    fun configureGlobal(auth: AuthenticationManagerBuilder) {
        auth.inMemoryAuthentication()
            .withUser(getUsername()).password("{noop}${getPassword()}").roles("USER")
    }

    private fun getUsername(): String{
        return if (username.isNullOrEmpty()) {
            configuration?.classificator?.monitorConfig?.username ?: ""
        } else {
            username ?: ""
        }
    }


    private fun getPassword(): String{
        return if (password.isNullOrEmpty()) {
            configuration?.classificator?.monitorConfig?.password ?: ""
        } else {
            password ?: ""
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
