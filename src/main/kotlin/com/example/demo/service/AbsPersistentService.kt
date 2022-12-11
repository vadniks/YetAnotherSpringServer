package com.example.demo.service

import com.example.demo.IRepo
import com.example.demo.entity.IEntity
import com.example.demo.unit

abstract class AbsPersistentService<E : IEntity>(private val repo: IRepo<E>) {

    operator fun plusAssign(entity: E) = repo.save(entity).unit

    operator fun get(id: Int): E? = repo.findById(id).orElse(null)

    val all: List<E> get() = repo.findAll()

    operator fun minusAssign(id: Int) = repo.deleteById(id)
}
