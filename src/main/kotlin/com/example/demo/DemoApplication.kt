package com.example.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration
import org.springframework.boot.runApplication
import java.util.*
import java.util.logging.Logger

val Any.unit get() = Unit

@Deprecated("debug only")
val appLog = Logger.getLogger("app")

@SpringBootApplication(exclude = [HibernateJpaAutoConfiguration::class])
class DemoApplication

fun main(args: Array<String>) = runApplication<DemoApplication>(*args) {
    setDefaultProperties(Properties().apply {
        put("server.port", 8081)
        put("spring.redis.host", "demo_redis")
        put("spring.redis.port", 6379)
        put("spring.redis.password", "")
        put("spring.session.redis.flush-mode", "on_save")
        put("spring.session.store-type", "redis")
    })
}.unit

/*
insert into valuables(name, description, cost) values('vase', 'just a vase', 100);
insert into valuables(name, description, cost) values('table', 'wooden table', 200);
insert into users(name, password) value('admin', '$2a$12$4Nu7xDnP8R77WdhnpbyyEO8ZaLyWR7NIm8pMOR1Z05tZSsPPtMvpi');
insert into users(name, password) value('user', '$2a$12$eu3GMWULScnjUT7p.W32k.XjUlP76DSh/7kQi9wsPtNkvHp1/x.DG');
*/
