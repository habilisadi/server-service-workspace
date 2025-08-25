package com.habilisadi.workspace.adapter.out.persistence

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.FactoryBean
import org.springframework.cglib.proxy.Proxy

import org.springframework.data.redis.core.RedisTemplate

class RedisRepositoryFactoryBean<T : Any>(
    private val repositoryInterface: Class<T>,
    private val redisTemplate: RedisTemplate<String, String>,
    private val objMapper: ObjectMapper
) : FactoryBean<T> {

    override fun getObject(): T {

        try {
            val handler = RedisRepositoryInvocationHandler(repositoryInterface, redisTemplate, objMapper)

            @Suppress("UNCHECKED_CAST")
            val proxy = Proxy.newProxyInstance(
                repositoryInterface.classLoader,
                arrayOf(repositoryInterface),
                handler
            ) as T

            return proxy

        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    override fun getObjectType(): Class<T> = repositoryInterface

    override fun isSingleton(): Boolean = true
}