package com.habilisadi.workspace.domain.model

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "workspace_channel_users")
data class ChannelUserEntity(
    @Id
    var id: String? = null,
    var createdAt: Instant = Instant.now(),

    @ManyToOne(fetch = FetchType.LAZY)
    var channel: ChannelEntity,

    @OneToMany(mappedBy = "channelUser", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    var users: MutableList<UserEntity> = mutableListOf()
)