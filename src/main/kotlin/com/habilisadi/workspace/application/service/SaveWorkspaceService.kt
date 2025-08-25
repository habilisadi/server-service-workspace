package com.habilisadi.workspace.application.service

import com.habilisadi.auth.shared.application.dto.ResponseStatus
import com.habilisadi.workspace.application.dto.WorkspaceUserCommand
import com.habilisadi.workspace.application.dto.WorkspaceUserResponse
import com.habilisadi.workspace.application.port.`in`.SaveWorkspaceUserUseCase
import com.habilisadi.workspace.application.port.out.WorkspaceRepository
import com.habilisadi.workspace.application.port.out.WorkspaceUserRepository
import org.springframework.stereotype.Service

@Service
class SaveWorkspaceService(
    private val workspaceUserRepository: WorkspaceUserRepository,
    private val workspaceRepository: WorkspaceRepository,
) : SaveWorkspaceUserUseCase {
    override fun save(command: WorkspaceUserCommand.SaveWorkspaceUser): ResponseStatus<WorkspaceUserResponse.Save> {
        val workspaceEntity = workspaceRepository.findById(command.workspacePk)
            .orElseThrow { IllegalArgumentException("Workspace not found") }

        val userEntity = command.toEntity(workspaceEntity)

        workspaceUserRepository.save(userEntity)

        val res = WorkspaceUserResponse.Save(workspaceEntity.domain.value)

        return ResponseStatus.successData(res, "Workspace user saved successfully")
    }
}