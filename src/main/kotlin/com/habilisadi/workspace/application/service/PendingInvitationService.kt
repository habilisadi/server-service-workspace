package com.habilisadi.workspace.application.service

import com.habilisadi.workspace.application.dto.InvitationCommand
import com.habilisadi.workspace.application.port.`in`.PendingInvitationUseCase
import com.habilisadi.workspace.application.port.out.InvitationRedisRepository
import com.habilisadi.workspace.application.port.out.WorkspaceRepository
import com.habilisadi.workspace.infrastructure.config.properties.RedisInvitationKeyProperties
import org.springframework.stereotype.Service

@Service
class PendingInvitationService(
    @Suppress("SpringJavaInjectionPointsAutowiringInspection")
    private val invitationRedisRepository: InvitationRedisRepository,
    private val workspaceRepository: WorkspaceRepository,
    private val redisInvitationKeyProps: RedisInvitationKeyProperties,
) : PendingInvitationUseCase {
    override fun pendingInvitation(command: InvitationCommand.Sending): InvitationCommand.Pending {
        val workspaceEntity = workspaceRepository.findById(command.workspacePk)
            .orElseThrow { throw IllegalArgumentException("Workspace not found") }

        val invitationEntities = command.toEntities(workspaceEntity.name)
        val keyAndInvitation = invitationEntities.map { invitationEntity ->
            Pair(
                redisInvitationKeyProps.generateInvitationKey(
                    invitationEntity.workspacePk,
                    invitationEntity.code.value
                ), invitationEntity
            )
        }

        keyAndInvitation.forEach { (key, invitationEntity) ->
            invitationRedisRepository.saveValue(key, invitationEntity)
        }


        return InvitationCommand.Pending.fromInvitation(invitationEntities)
    }
}