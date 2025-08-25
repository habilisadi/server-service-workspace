package com.habilisadi.workspace.infrastructure.config

import com.habilisadi.workspace.WorkspaceServiceGrpc
import com.habilisadi.workspace.WorkspaceServiceGrpc.WorkspaceServiceBlockingStub
import com.netflix.appinfo.InstanceInfo
import com.netflix.discovery.EurekaClient
import io.grpc.Channel
import io.grpc.ManagedChannel
import io.grpc.netty.NettyChannelBuilder
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy
import java.util.concurrent.TimeUnit


@Configuration
class GrpcConfig {

    private val log = org.slf4j.LoggerFactory.getLogger(this::class.java)

    @Bean
    fun workspaceServiceBlockingStub(@Qualifier("authChannel") channel: Channel): WorkspaceServiceBlockingStub {
        return WorkspaceServiceGrpc.newBlockingStub(channel)
    }

    @Bean
    @Lazy
    fun authChannel(client: EurekaClient): Channel {
        return createChannelFromEureka("auth", client)
    }

    @Bean
    @Lazy
    fun fileChannel(client: EurekaClient): Channel {
        return createChannelFromEureka("file", client)
    }

    private fun createChannelFromEureka(serviceName: String, client: EurekaClient): ManagedChannel {
        val instance: InstanceInfo = client.getNextServerFromEureka(serviceName, true)
            ?: throw RuntimeException("Service not found: $serviceName")


        val grpcPort = instance.metadata["grpc.port"]
            ?: throw RuntimeException("gRPC port not found in service metadata")

        val target = instance.ipAddr + ":" + grpcPort

        log.info("Creating gRPC channel to: $target")

        return NettyChannelBuilder.forTarget(target)
            .usePlaintext()
            .keepAliveTime(30, TimeUnit.SECONDS)
            .keepAliveTimeout(5, TimeUnit.SECONDS)
            .keepAliveWithoutCalls(true)
            .idleTimeout(60, TimeUnit.SECONDS)
            .defaultServiceConfig(createRetryServiceConfig(5))
            .enableRetry()
            .build()
    }

    private fun createRetryServiceConfig(maxAttempts: Int): Map<String, Any> {
        return java.util.Map.of<String, Any>(
            "retryPolicy", java.util.Map.of(
                "maxAttempts", maxAttempts.toString(),
                "initialBackoff", "0.5s",
                "maxBackoff", "30s",
                "backoffMultiplier", "2",
                "retryableStatusCodes", listOf("UNAVAILABLE", "DEADLINE_EXCEEDED")
            )
        )
    }

}