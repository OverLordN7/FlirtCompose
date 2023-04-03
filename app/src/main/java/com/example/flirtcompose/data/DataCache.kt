package com.example.flirtcompose.data

import com.google.common.cache.CacheBuilder
import java.util.concurrent.TimeUnit

class DataCache {
    private val cache = CacheBuilder.newBuilder()
        .maximumSize(100)
        .expireAfterAccess(5,TimeUnit.MINUTES)
        .build<String,Any>()

    fun get(key: String): Any? = cache.getIfPresent(key)

    fun put(key: String, value: Any){
        cache.put(key,value)
    }

    fun remove(key: String){
        cache.invalidate(key)
    }
}