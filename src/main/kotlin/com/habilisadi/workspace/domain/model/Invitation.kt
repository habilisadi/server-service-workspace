package com.habilisadi.workspace.domain.model

import com.github.f4b6a3.ulid.UlidCreator
import jakarta.persistence.Embedded
import java.time.Instant

data class Invitation(
    var id: String = UlidCreator.getUlid().toString(),
    var email: String,
    var workspacePk: String,
    var workspaceName: String,
    @Embedded
    var code: InvitationCode = InvitationCode.generateCode(),
    var expiredAt: Instant
)
