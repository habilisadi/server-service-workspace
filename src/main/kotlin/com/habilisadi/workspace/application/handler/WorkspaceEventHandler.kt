package com.habilisadi.workspace.application.handler

import com.habilisadi.workspace.domain.event.WorkspaceDeactivatedEvent
import org.springframework.context.event.EventListener

@ModelEventHandler
class WorkspaceEventHandler {

    @EventListener
    fun deActivateWorkspace(event: WorkspaceDeactivatedEvent) {
        TODO("로직 추가 예정")
    }
}