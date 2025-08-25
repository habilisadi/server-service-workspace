package com.habilisadi.workspace.infrastructure.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@ConfigurationProperties(prefix = "redis.invitation")
@Component
data class RedisInvitationKeyProperties(
    val INVITATION_PREFIX: String = "invitation"
) {
    fun generateInvitationKey(workspacePk: String, code: String) =
        "${INVITATION_PREFIX}:${workspacePk}:${code}"
}