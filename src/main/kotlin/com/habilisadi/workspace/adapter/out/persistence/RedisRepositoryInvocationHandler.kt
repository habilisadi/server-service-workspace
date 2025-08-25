package com.habilisadi.workspace.adapter.out.persistence

import com.fasterxml.jackson.databind.ObjectMapper
import com.habilisadi.workspace.shared.adapter.out.BaseRedisRepositoryImpl
import com.habilisadi.workspace.shared.application.port.out.BaseRedisRepository
import org.springframework.cglib.proxy.InvocationHandler
import org.springframework.data.redis.core.RedisTemplate
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType

class RedisRepositoryInvocationHandler(
    private val repositoryInterface: Class<*>,
    private val redisTemplate: RedisTemplate<String, String>,
    private val objMapper: ObjectMapper
) : InvocationHandler {

    private val delegate: BaseRedisRepositoryImpl<*>

    init {
        val entityClass = extractEntityClassFromGeneric(repositoryInterface)
        delegate = BaseRedisRepositoryImpl(
            clazz = entityClass,
            redisTemplate = redisTemplate,
            objMapper = objMapper
        )
    }

    override fun invoke(proxy: Any?, method: Method, args: Array<out Any>?): Any? {
        return try {
            method.invoke(delegate, *(args ?: emptyArray()))
        } catch (e: Exception) {
            e.printStackTrace()
            when (method.returnType) {
                Boolean::class.java -> false
                else -> null
            }
        }
    }


    private fun extractEntityClassFromGeneric(repositoryInterface: Class<*>): Class<*> {
        val genericInterfaces = repositoryInterface.genericInterfaces

        for (genericInterface in genericInterfaces) {
            if (genericInterface !is ParameterizedType) continue

            val rawType = genericInterface.rawType

            if (rawType != BaseRedisRepository::class.java) continue

            val typeArguments = genericInterface.actualTypeArguments

            if (typeArguments.isEmpty()) continue

            return typeArguments[0] as Class<*>
        }

        throw IllegalArgumentException("Cannot extract entity type from ${repositoryInterface.name}")
    }
}