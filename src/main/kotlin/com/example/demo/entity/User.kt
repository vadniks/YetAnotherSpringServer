package com.example.demo.entity

import org.hibernate.Hibernate
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*
import javax.persistence.*

@kotlinx.serialization.Serializable
@Entity
@Table(name = "users")
data class User(
    @Column(name = "name") private val _name: String,
    @Column(name = "password") private val _password: String,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int?
) : IEntity, UserDetails {
    val name get() = _name
    val password @JvmName("_getPassword") get() = _password

    override fun getAuthorities(): List<SimpleGrantedAuthority>
    = Collections.singletonList(SimpleGrantedAuthority(name))

    override fun getUsername() = _name

    override fun getPassword() = _password

    override fun isAccountNonExpired() = true

    override fun isAccountNonLocked() = true

    override fun isCredentialsNonExpired() = true

    override fun isEnabled() = true

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false

        other as User
        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString() = this::class.simpleName + "($id, $name, $password)"
}
