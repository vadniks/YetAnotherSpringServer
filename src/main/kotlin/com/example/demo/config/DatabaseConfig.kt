package com.example.demo.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.hibernate5.HibernateTransactionManager
import org.springframework.orm.hibernate5.LocalSessionFactoryBean
import java.util.*

@EnableJpaRepositories(basePackages = ["com.example.demo"])
@Configuration
class DatabaseConfig {

    val dataSource @Bean(name = ["dataSource"]) get() = HikariDataSource(HikariConfig().apply {
        jdbcUrl = "jdbc:mysql://demo_mysql:3306/db"
        driverClassName = "com.mysql.cj.jdbc.Driver"
        username = "root"
        password = "root"
    })

    val entityManagerFactory @Bean(name = ["entityManagerFactory"])
    get() = LocalSessionFactoryBean().apply {
        setDataSource(dataSource)
        setPackagesToScan("com.example.demo")
        hibernateProperties = Properties().apply {
            put("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect")
            put("hibernate.hbm2ddl.auto", "update")
        }
    }

    @Suppress("unused")
    val transactionManager @Bean("transactionManager")
    get() = HibernateTransactionManager(entityManagerFactory.`object`!!)
}
