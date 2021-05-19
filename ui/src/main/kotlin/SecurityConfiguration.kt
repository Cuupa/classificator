import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import com.cuupa.classificator.externalconfiguration.Config

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
    open fun accessDeniedHandler() = MonitorAccessDeniedHandler()

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
