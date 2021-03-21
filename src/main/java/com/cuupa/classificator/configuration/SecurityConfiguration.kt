package com.cuupa.classificator.configuration

import com.cuupa.classificator.configuration.external.Config
import com.cuupa.classificator.monitor.MonitorAccessDeniedHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.access.AccessDeniedHandler


@Configuration
open class SecurityConfiguration : WebSecurityConfigurerAdapter() {

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
                .antMatchers("/monitor", "/download").hasAnyRole("USER")
                .anyRequest().authenticated()
            .and()
            .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/monitor")
                .permitAll()
            .and()
            .logout()
                .permitAll()
            .and()
            .exceptionHandling()
            .accessDeniedHandler(accessDeniedHandler())
    }

    @Bean
    open fun accessDeniedHandler(): AccessDeniedHandler{
        return MonitorAccessDeniedHandler()
    }

    @Autowired
    open fun configureGlobal(auth: AuthenticationManagerBuilder) {
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
}
