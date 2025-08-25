package com.habilisadi.workspace.adapter.out.persistence

import com.habilisadi.workspace.infrastructure.config.EnabledRedisRepositories
import com.habilisadi.workspace.shared.application.port.out.BaseRedisRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.support.BeanDefinitionBuilder
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar
import org.springframework.core.io.Resource
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.core.type.AnnotationMetadata

class RedisRepositoryRegistrar : ImportBeanDefinitionRegistrar {
    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)
    }

    override fun registerBeanDefinitions(
        importingClassMetadata: AnnotationMetadata,
        registry: BeanDefinitionRegistry
    ) {

        val basePackages = extractBasePackages(importingClassMetadata)

        basePackages.flatMap(
            ::scanPackageForRepositories
        ).forEach { clazz ->
            registerRedisRepository(registry, clazz)
        }
    }

    private fun extractBasePackages(metadata: AnnotationMetadata): List<String> {
        val attributes = metadata.getAnnotationAttributes(EnabledRedisRepositories::class.java.name)
        return (attributes?.get("basePackages") as? Array<*>)?.map { it.toString() }
            ?: listOf("com.habilisadi")
    }

    private fun scanPackageForRepositories(basePackage: String): List<Class<*>> {
        validateBasePackage(basePackage)

        val classLoader = Thread.currentThread().contextClassLoader
        val packageSearchPath = createPackageSearchPath(basePackage)
        val resolver = PathMatchingResourcePatternResolver(classLoader)

        return try {
            resolver.getResources(packageSearchPath)
                .mapNotNull(::loadRepositoryClass)
        } catch (e: Exception) {
            log.warn("Failed to scan package: $basePackage", e)
            emptyList()
        }
    }


    private fun validateBasePackage(basePackage: String) {
        if (basePackage.contains("*")) {
            throw IllegalArgumentException("basePackage must not contain wildcards: $basePackage")
        }
    }

    private fun createPackageSearchPath(basePackage: String): String {
        val path = basePackage.replace('.', '/')
        return "classpath*:$path/**/*.class"
    }


    private fun loadRepositoryClass(resource: Resource): Class<*>? {
        return try {
            val className = extractClassName(resource)
            val clazz = Class.forName(className)

            if (isValidRepositoryInterface(clazz)) clazz else null
        } catch (e: Exception) {
            log.debug("Failed to load class from resource: {}", resource.uri, e)
            null
        }
    }

    private fun extractClassName(resource: Resource): String {
        return resource.url.path
            .substringAfter("/classes/kotlin/main/")
            .substringBefore(".class")
            .replace('/', '.')
    }

    private fun isValidRepositoryInterface(clazz: Class<*>): Boolean {
        return clazz.isInterface &&
                BaseRedisRepository::class.java.isAssignableFrom(clazz) &&
                clazz != BaseRedisRepository::class.java
    }

    private fun registerRedisRepository(registry: BeanDefinitionRegistry, repositoryInterface: Class<*>) {
        val beanDefinition = BeanDefinitionBuilder
            .genericBeanDefinition(RedisRepositoryFactoryBean::class.java)
            .addConstructorArgValue(repositoryInterface)
            .addConstructorArgReference("redisTemplate")
            .addConstructorArgReference("objectMapper")
            .beanDefinition

        val beanName = repositoryInterface.simpleName
            .replaceFirstChar(Char::lowercase)

        registry.registerBeanDefinition(beanName, beanDefinition)
    }

}