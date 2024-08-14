package com.bhuang.mapper

import mu.KLogging
import org.apache.ibatis.binding.MapperProxy
import org.apache.ibatis.binding.MapperProxyFactory
import org.apache.ibatis.io.Resources
import org.apache.ibatis.session.SqlSession
import org.apache.ibatis.session.SqlSessionFactoryBuilder
import org.junit.jupiter.api.Test
import java.io.InputStream

class MapperProxyTest {
    companion object {
        private val logger = KLogging().logger()
    }


    private val resource: InputStream = Resources.getResourceAsStream("mybatis/mybatis-config.xml")
    private var sqlSession: SqlSession

    init {
        sqlSession = SqlSessionFactoryBuilder().build(resource).openSession()
    }

    @Test
    fun testMapperProxy() {
        // 通过MapperProxyFactory创建MapperProxy
        val mapperProxy = MapperProxyFactory<UserMapper>(UserMapper::class.java).newInstance(sqlSession)
        mapperProxy.findAll()?.forEach {
            logger.info { "user: $it" }
        }

    }
}