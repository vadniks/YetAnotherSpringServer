package com.example.demo.entity

import javax.persistence.MappedSuperclass

@kotlinx.serialization.Serializable
@MappedSuperclass
sealed interface IEntity
