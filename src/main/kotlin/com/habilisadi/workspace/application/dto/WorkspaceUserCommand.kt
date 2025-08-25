package com.habilisadi.workspace.application.dto

import com.habilisadi.workspace.VerifyInvitationResponse
import com.habilisadi.workspace.domain.model.ProfileImg
import com.habilisadi.workspace.domain.model.Role
import com.habilisadi.workspace.domain.model.UserEntity
import com.habilisadi.workspace.domain.model.WorkspaceEntity

class WorkspaceUserCommand {
    data class SaveWorkspaceUser(
        val workspacePk: String,
        val userPk: String
    ) {
        companion object {
            fun from(res: VerifyInvitationResponse): SaveWorkspaceUser {
                if (res.workspacePk == null || res.userPk == null) {
                    throw IllegalArgumentException("workspacePk or userPk is null")
                }

                if (res.workspacePk.isEmpty() || res.userPk.isEmpty()) {
                    throw IllegalArgumentException("workspacePk or userPk is empty")
                }

                return SaveWorkspaceUser(
                    workspacePk = res.workspacePk,
                    userPk = res.userPk
                )
            }
        }

        fun toEntity(workspaceEntity: WorkspaceEntity): UserEntity {
            val user = UserEntity(
                userPk = userPk,
                workspaceName = workspaceEntity.name,
                profileImg = ProfileImg("/profile"),
                role = Role(),
            )
            user.addWorkspace(workspaceEntity)

            return user
        }
    }
}