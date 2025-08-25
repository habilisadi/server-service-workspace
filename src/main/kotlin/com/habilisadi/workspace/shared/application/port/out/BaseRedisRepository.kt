package com.habilisadi.workspace.shared.application.port.out

import java.time.Duration

interface BaseRedisRepository<T> {
    fun findByKey(key: String): T?
    fun saveValue(key: String, value: T): Boolean
    fun saveValue(key: String, value: T, timeOut: Duration): Boolean
    fun deleteByKey(key: String): Boolean
}