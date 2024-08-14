package com.bhuang.binding

import com.bhuang.mapper.UserMapper
import mu.KLogging
import org.apache.ibatis.binding.MapperMethod
import org.apache.ibatis.builder.xml.XMLConfigBuilder
import org.apache.ibatis.io.Resources
import org.apache.ibatis.session.Configuration
import org.apache.ibatis.session.SqlSession
import org.apache.ibatis.session.SqlSessionFactoryBuilder
import org.junit.jupiter.api.Test
import java.io.InputStream

class MapperMethodTest {
    companion object {
        private val logger = KLogging().logger()
    }


    private val resource: InputStream = Resources.getResourceAsStream("mybatis/mybatis-config.xml")
    private val resource2: InputStream = Resources.getResourceAsStream("mybatis/mybatis-config.xml")

    private var sqlSession: SqlSession
    private var configuration: Configuration

    init {
        sqlSession = SqlSessionFactoryBuilder().build(resource).openSession()
        configuration = XMLConfigBuilder(resource2).parse()
    }

    @Test
    fun testMapperMethod() {
        val method = UserMapper::class.java.getMethod("findAll")
        val res = MapperMethod(UserMapper::class.java, method, configuration).execute(sqlSession, null)
        println(res)
    }
}