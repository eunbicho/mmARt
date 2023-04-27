package com.ssafy.mmart.config

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.CacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer
import springfox.documentation.service.*


@Configuration
class RedisConfig(@Value("\${spring.redis.host}")
                  private val redisHost: String,
                  @Value("\${spring.redis.port}")
                  private val redisPort: Int) {

    /**
     * Redis Client 중 Lettuce 방식 사용 (성능이 Jedis 보다 좋음)
     */
    @Bean
    fun redisConnectionFactory(): RedisConnectionFactory? {
        return LettuceConnectionFactory(redisHost!!, redisPort)
    }

    @Bean
    fun redisTemplate(): RedisTemplate<String, Any>? {
        val redisTemplate = RedisTemplate<String, Any>()
        redisTemplate.setConnectionFactory(redisConnectionFactory()!!)
        redisTemplate.keySerializer = StringRedisSerializer()
        redisTemplate.valueSerializer = Jackson2JsonRedisSerializer(String::class.java)
        return redisTemplate
    }

//    @Bean
//    fun redisTemplate(): RedisTemplate<*, *>? {
//        val redisTemplate = RedisTemplate<ByteArray, ByteArray>()
//        redisTemplate.setConnectionFactory(redisConnectionFactory()!!)
//        redisTemplate.keySerializer = StringRedisSerializer()
//        return redisTemplate
//    }
    //https://crazy-horse.tistory.com/entry/%EC%BD%94%ED%8B%80%EB%A6%B0-redis-cache-%EC%A0%81%EC%9A%A9-%EC%8B%9C%ED%82%A4%EA%B8%B0


}