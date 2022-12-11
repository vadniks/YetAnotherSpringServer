package com.example.demo.config

import com.example.demo.service.UserService
import com.example.demo.unit
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.core.GrantedAuthorityDefaults
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.util.matcher.AntPathRequestMatcher

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
class MethodSecurityConfig : GlobalMethodSecurityConfiguration()

@EnableWebSecurity
@Configuration
class WebSecurityConfig(private val userDetailsService: UserService) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain = http
        .csrf().disable()
        .authorizeRequests().and()
        .formLogin()
        .usernameParameter("name")
        .passwordParameter("password")
        .loginProcessingUrl("/login")
        .permitAll().and()
        .logout()
        .logoutRequestMatcher(AntPathRequestMatcher("/logout"))
        .permitAll().and().build()

    @Suppress("unused")
    val authenticationProvider @Bean get() = DaoAuthenticationProvider().apply {
        setUserDetailsService(userDetailsService)
        setPasswordEncoder(BCryptPasswordEncoder())
    }

    @Suppress("unused")
    val grantedAuthorityDefaults @Bean get() = GrantedAuthorityDefaults("")
}

@Configuration
class AuthenticationConfig(private val authenticationProvider: AuthenticationProvider) {

    @Autowired
    fun configureGlobal(auth: AuthenticationManagerBuilder) =
        auth.authenticationProvider(authenticationProvider).unit
}
