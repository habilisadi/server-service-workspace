package com.habilisadi.workspace.domain.model

import com.github.f4b6a3.ulid.UlidCreator
import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "workspace_channel_messages")
data class ChannelMessageEntity(
    @Id
    var id: String? = null,

    var parentId: String? = null,

//    @Embedded
//    @AttributeOverride(name = "value", column = Column(name = "message"))
//    var message: Message,
    var message: String,

    var createdAt: Instant = Instant.now(),

    var deletedAt: Instant? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    var channel: ChannelEntity,

    @ManyToOne(fetch = FetchType.EAGER)
    var user: UserEntity
) {
    @PrePersist
    fun init() {
        if (id == null) id = UlidCreator.getUlid().toString()
    }
}
