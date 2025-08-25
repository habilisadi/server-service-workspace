package com.habilisadi.workspace.infrastructure.config

import org.springframework.context.annotation.Configuration

@Configuration
@EnabledRedisRepositories(
    basePackages = [
        "com.habilisadi.workspace.application.port.out",
        "com.habilisadi.workspace.shared.application.port.out",
    ]
)
class RedisRepositoryConfig
