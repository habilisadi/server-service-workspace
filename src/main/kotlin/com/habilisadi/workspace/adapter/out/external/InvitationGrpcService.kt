package com.habilisadi.workspace.adapter.out.external

import com.habilisadi.auth.shared.application.dto.ResponseStatus
import com.habilisadi.workspace.VerifyInvitationResponse
import com.habilisadi.workspace.WorkspaceServiceGrpc
import com.habilisadi.workspace.application.dto.InvitationCommand
import com.habilisadi.workspace.application.port.`in`.SendEmailInvitationUseCase
import com.habilisadi.workspace.application.port.`in`.VerifyInvitationUseCase
import com.habilisadi.workspace.application.port.out.InvitationRedisRepository
import com.habilisadi.workspace.infrastructure.config.properties.RedisInvitationKeyProperties
import org.springframework.stereotype.Service

@Service
class InvitationGrpcService(
    private val workspaceStub: WorkspaceServiceGrpc.WorkspaceServiceBlockingStub,
    @Suppress("SpringJavaInjectionPointsAutowiringInspection")
    private val invitationRedisRepository: InvitationRedisRepository,
    private val redisInvitationKeyProperties: RedisInvitationKeyProperties,
) : SendEmailInvitationUseCase, VerifyInvitationUseCase {
    override fun pendingSend(command: InvitationCommand.Pending): ResponseStatus<Boolean> {

        val grpcInvitationReq = command.toGrpcRequest()

        val grpcInvitationRes = workspaceStub.invitationRequest(grpcInvitationReq)

        if (grpcInvitationRes.status == com.habilisadi.workspace.Status.FAILED) {
            return ResponseStatus.error("Failed to send invitation")
        }

        return ResponseStatus.success(true)
    }

    override fun verifyCode(command: InvitationCommand.VerifyCode): Boolean {
        val key = redisInvitationKeyProperties.generateInvitationKey(command.workspacePk, command.code)
        val invitation = invitationRedisRepository.findByKey(key)
            ?: throw IllegalArgumentException("Invitation not found")

        if (invitation.code.value == command.code
            && invitation.workspacePk == command.workspacePk
            && invitation.email == command.email
        ) {
            throw IllegalArgumentException("Invitation not found")
        }

        return true
    }

    override fun verifyUser(command: InvitationCommand.VerifyUser): Pair<Boolean, VerifyInvitationResponse> {

        val grpcVerifyUserReq = command.toGrpcRequest()

        val grpcVerifyUserRes = workspaceStub.verifyInvitationRequest(grpcVerifyUserReq)


        if (grpcVerifyUserRes.status != com.habilisadi.workspace.Status.COMPLETED) {
            throw IllegalArgumentException("Failed to verify invitation")
        }

        return Pair(true, grpcVerifyUserRes)
    }
}