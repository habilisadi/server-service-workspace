package com.habilisadi.workspace.domain.model

import com.github.f4b6a3.ulid.UlidCreator
import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "workspace_users")
data class UserEntity(
    @Id
    var id: String? = null,
    var userPk: String,
    var workspaceName: String,
    var profileImg: ProfileImg,

    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "role"))
    var role: Role,

    var createdAt: Instant = Instant.now(),

    @ManyToMany(mappedBy = "users", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var workspaces: MutableList<WorkspaceEntity> = mutableListOf(),

    @ManyToOne(fetch = FetchType.LAZY)
    var channel: ChannelEntity? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    var channelUser: ChannelUserEntity? = null
) {
    @PrePersist
    fun init() {
        if (id == null) id = UlidCreator.getUlid().toString()
    }

    fun addWorkspace(workspace: WorkspaceEntity) {
        workspaces.add(workspace)
        workspace.users.add(this)
    }

    fun removeWorkspace(workspace: WorkspaceEntity) {
        workspaces.remove(workspace)
    }
}
