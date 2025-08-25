package com.habilisadi.workspace.domain.event

import java.time.Instant

data class WorkspaceDeactivatedEvent(
    val id: String,
    val timestamp: Instant = Instant.now()
)
