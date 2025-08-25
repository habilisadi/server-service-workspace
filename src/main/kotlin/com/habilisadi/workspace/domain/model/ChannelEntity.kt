package com.habilisadi.workspace.domain.model

import com.github.f4b6a3.ulid.UlidCreator
import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "workspace_channels")
data class ChannelEntity(
    @Id
    var id: String? = null,
    var workspacePk: String,
    var name: String,
    var isPublic: Boolean,
    var isActive: Boolean,
    var createdAt: Instant = Instant.now(),
    var deletedAt: Instant?,

    @ManyToOne(fetch = FetchType.LAZY)
    var workspace: WorkspaceEntity,

    @OneToMany(mappedBy = "channel", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    var channelUsers: MutableList<ChannelUserEntity> = mutableListOf(),

    @OneToMany(mappedBy = "channel", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    var messages: MutableList<ChannelMessageEntity> = mutableListOf()
) {
    @PrePersist
    fun init() {
        if (id == null) id = UlidCreator.getUlid().toString()
    }
}
