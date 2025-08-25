package com.habilisadi.workspace.domain.model

import com.habilisadi.workspace.domain.converter.WorkspaceUserRoleTypesConverter
import jakarta.persistence.Convert
import jakarta.persistence.Embeddable
import org.springframework.security.core.GrantedAuthority

@Embeddable
data class Role(
    @Convert(converter = WorkspaceUserRoleTypesConverter::class)
    val value: MutableSet<UserRoleType> = mutableSetOf()
) {
    fun add(role: UserRoleType) {
        this.value.plus(role)
    }

    fun remove(role: UserRoleType) {
        this.value.minus(role)
    }

    fun getAuthorities(): Set<GrantedAuthority> {
        return value.map { GrantedAuthority { it.name } }.toSet()
    }
}