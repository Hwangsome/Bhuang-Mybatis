package com.bhuang.jdk

import org.junit.jupiter.api.Test

class MapTest {

    @Test
    fun testMap() {
        val map = mapOf("a" to 1, "b" to 2, "c" to 3).toMutableMap()
        map.computeIfAbsent("d") { 4 }
        println(map)
    }
}