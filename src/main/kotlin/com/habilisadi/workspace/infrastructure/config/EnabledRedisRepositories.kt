package com.habilisadi.workspace.infrastructure.config


import com.habilisadi.workspace.adapter.out.persistence.RedisRepositoryRegistrar
import org.springframework.context.annotation.Import

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Import(RedisRepositoryRegistrar::class)
annotation class EnabledRedisRepositories(
    val basePackages: Array<String> = [],
)
