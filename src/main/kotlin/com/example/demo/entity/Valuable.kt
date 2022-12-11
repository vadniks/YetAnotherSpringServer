package com.example.demo.entity

import org.hibernate.Hibernate
import javax.persistence.*

@kotlinx.serialization.Serializable
@Entity
@Table(name = "valuables")
data class Valuable(
    val name: String,
    val description: String,
    val cost: Int,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null
) : IEntity {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false

        other as Valuable
        return id != null && id == other.id
    }

    override fun hashCode() = javaClass.hashCode()

    @Override
    override fun toString() = this::class.simpleName + "($id, $name, $description, $cost)"
}
