package com.example.demo.config

import com.example.demo.unit
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class MvcConfig : WebMvcConfigurer {

    private operator fun ViewControllerRegistry.plusAssign(controller: String)
    = addViewController(controller).unit

    override fun addViewControllers(registry: ViewControllerRegistry) = registry.apply {
        this += "/catalogue"
        this += "/view"
        this += "/admin"
        this += "/pdf"
        this += "/stats"
    }.unit

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) = registry
        .addResourceHandler("/static/**")
        .addResourceLocations("classpath:/static/")
        .unit
}
