package com.example.demo

import com.example.demo.entity.IEntity
import com.example.demo.entity.User
import com.example.demo.entity.Valuable
import org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_SINGLETON
import org.springframework.context.annotation.Scope
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.stereotype.Repository

@NoRepositoryBean interface IRepo<E : IEntity> : JpaRepository<E, Int>
@Repository @Scope(SCOPE_SINGLETON) interface ValuableRepo : IRepo<Valuable>
@Repository @Scope(SCOPE_SINGLETON) interface UserRepo : IRepo<User>
