package com.example.demo.config

import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.thymeleaf.spring5.SpringTemplateEngine
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver
import org.thymeleaf.spring5.view.ThymeleafViewResolver
import org.thymeleaf.templatemode.TemplateMode

@Configuration
class TemplateConfig(private val context: ApplicationContext) {

    val templateResolver @Bean get() = SpringResourceTemplateResolver().apply {
        setApplicationContext(context)
        prefix = "classpath:/templates/"
        suffix = ".html"
        templateMode = TemplateMode.HTML
        isCacheable = false
    }

    val templateEngine @Bean get() = SpringTemplateEngine().apply {
        setTemplateResolver(templateResolver)
        enableSpringELCompiler = true
    }

    @Suppress("unused")
    val viewResolver @Bean get() = ThymeleafViewResolver().apply {
        templateEngine = this@TemplateConfig.templateEngine
        order = 1
        viewNames = arrayOf(".html")
    }
}
