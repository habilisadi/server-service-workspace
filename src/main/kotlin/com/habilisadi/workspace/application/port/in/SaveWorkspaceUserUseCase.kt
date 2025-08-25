package com.habilisadi.workspace.application.port.`in`

import com.habilisadi.auth.shared.application.dto.ResponseStatus
import com.habilisadi.workspace.application.dto.WorkspaceUserCommand
import com.habilisadi.workspace.application.dto.WorkspaceUserResponse

interface SaveWorkspaceUserUseCase {
    fun save(command: WorkspaceUserCommand.SaveWorkspaceUser): ResponseStatus<WorkspaceUserResponse.Save>
}