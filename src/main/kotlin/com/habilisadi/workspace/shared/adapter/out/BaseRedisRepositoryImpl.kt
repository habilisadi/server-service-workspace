package com.habilisadi.workspace.shared.adapter.out

import com.fasterxml.jackson.databind.ObjectMapper
import com.habilisadi.workspace.shared.application.port.out.BaseRedisRepository
import org.springframework.data.redis.core.RedisTemplate
import java.time.Duration

class BaseRedisRepositoryImpl<T>(
    private val clazz: Class<T>,
    private val redisTemplate: RedisTemplate<String, String>,
    private val objMapper: ObjectMapper
) : BaseRedisRepository<T> {

    override fun findByKey(key: String): T? {
        val json = redisTemplate.opsForValue().get(key) ?: return null
        return objMapper.readValue(json, clazz)
    }

    override fun saveValue(key: String, value: T): Boolean {
        return this.saveValue(key, value, Duration.ofMinutes(10))
    }

    override fun saveValue(key: String, value: T, timeOut: Duration): Boolean {
        val json = objMapper.writeValueAsString(value)

        return try {
            redisTemplate.opsForValue().set(key, json, timeOut)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override fun deleteByKey(key: String): Boolean {
        return redisTemplate.delete(key)
    }
}