package com.lectorie.lectorie.model

import com.lectorie.lectorie.enums.Role
import jakarta.persistence.*
import org.hibernate.annotations.UuidGenerator
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Entity
@Table(name = "users")
data class User @JvmOverloads constructor(

    @Id
    @UuidGenerator
    val id : String? = "",

    val email: String,
    private val password: String,

    @Enumerated(EnumType.STRING)
    val role: Role,

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "user_settings_id")
    val userSettings: UserSettings,

    @OneToOne
    val tutor: Tutor?,

    private val isEnabled: Boolean,
    private val isCredentialsNonExpired: Boolean = true, //credential can be expired,eg. Change the password every three months
    private val isAccountNonExpired: Boolean = true, //eg. Demo account（guest） can only be online  24 hours
    private val isAccountNonLocked: Boolean = true, //eg. Users who malicious attack system,lock their account for one year
    val balance: Double,
    val session: String?

    ) : UserDetails {

    fun changeEmail(email: String) = this.copy(email = email)
    fun changePassword(password: String) = this.copy(password = password)
    fun changeRole(role: Role) = this.copy(role = role)
    fun changeUserSettings(userSettings: UserSettings) = this.copy(userSettings = userSettings)
    fun changeTutor(tutor: Tutor?) = this.copy(tutor = tutor)
    fun changeIsEnabled(isEnabled: Boolean) = this.copy(isEnabled = isEnabled)
    fun changeIsCredentialsNonExpired(isCredentialsNonExpired: Boolean) = this.copy(isCredentialsNonExpired = isCredentialsNonExpired)
    fun changeIsAccountNonExpired(isAccountNonExpired: Boolean) = this.copy(isAccountNonExpired = isAccountNonExpired)
    fun changeIsAccountNonLocked(isAccountNonLocked: Boolean) = this.copy(isAccountNonLocked = isAccountNonLocked)
    fun changeBalance(balance: Double) = this.copy(balance = balance)
    fun changeSession(session: String?) = this.copy(session = session)

    override fun getUsername(): String = email
    override fun getPassword(): String = password
    override fun isEnabled(): Boolean = isEnabled
    override fun isCredentialsNonExpired(): Boolean = isCredentialsNonExpired
    override fun isAccountNonExpired(): Boolean = isAccountNonExpired
    override fun isAccountNonLocked(): Boolean = isAccountNonLocked

    override fun getAuthorities(): Collection<GrantedAuthority> {
        val authorities = mutableListOf<GrantedAuthority>()

        // Add role-based authorities
        authorities.add(SimpleGrantedAuthority("ROLE_${role.name}"))

        // Add additional authorities based on your requirements
        when (role) {
            Role.TUTOR -> {
                authorities.add(SimpleGrantedAuthority("PERMISSION_TEACH"))
            }
            Role.STUDENT -> {
                authorities.add(SimpleGrantedAuthority("PERMISSION_LEARN"))
            }
            Role.ADMIN -> {
                authorities.add(SimpleGrantedAuthority("PERMISSION_MANAGE_USERS"))
            }
        }

        return authorities
    }
}
