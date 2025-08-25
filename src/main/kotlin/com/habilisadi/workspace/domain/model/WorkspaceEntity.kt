package com.habilisadi.workspace.domain.model

import com.github.f4b6a3.ulid.UlidCreator
import com.habilisadi.workspace.domain.event.WorkspaceDeactivatedEvent
import jakarta.persistence.*
import org.springframework.data.domain.AbstractAggregateRoot
import java.time.Instant

@Entity
@Table(name = "workspaces")
data class WorkspaceEntity(
    @Id
    var id: String? = null,
    var name: String,
    var description: String,
    var isActive: Boolean = true,

    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "domain"))
    var domain: Domain,

    var createdAt: Instant = Instant.now(),
    var deletedAt: Instant?,

    @ManyToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var users: MutableList<UserEntity> = mutableListOf(),

    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var channels: MutableList<ChannelEntity> = mutableListOf()

) : AbstractAggregateRoot<WorkspaceEntity>() {
    @PrePersist
    fun init() {
        if (id == null) id = UlidCreator.getUlid().toString()
    }

    fun deActivate() {
        registerEvent(WorkspaceDeactivatedEvent(id!!))
    }

    fun addUser(userEntity: UserEntity) {
        userEntity.role.add(UserRoleType.ROLE_USER)
        users.add(userEntity)
    }

    fun removeUser(userEntity: UserEntity) {
        users.remove(userEntity)
    }
}
