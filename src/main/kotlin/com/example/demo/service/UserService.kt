package com.example.demo.service

import com.example.demo.UserRepo
import com.example.demo.entity.User
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Scope
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
class UserService(userRepo: UserRepo) : AbsPersistentService<User>(userRepo), UserDetailsService {

    override fun loadUserByUsername(username: String?): UserDetails =
        all.find { it.name == username } ?: throw UsernameNotFoundException(null.toString())
}
