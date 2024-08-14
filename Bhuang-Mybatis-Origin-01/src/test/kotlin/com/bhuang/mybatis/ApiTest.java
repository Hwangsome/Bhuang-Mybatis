package com.bhuang.mybatis;

import com.bhuang.ibatis.bindings.MapperRegistry;
import com.bhuang.ibatis.sessions.SqlSession;
import com.bhuang.ibatis.sessions.defaults.DefaultSqlSession;
import com.bhuang.ibatis.sessions.defaults.DefaultSqlSessionFactory;
import org.apache.ibatis.builder.xml.XMLConfigBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.Configuration;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class ApiTest {
    private Configuration configuration;
    private MapperRegistry mapperRegistry;
    {
        try {
            configuration = new XMLConfigBuilder( Resources.getResourceAsStream("mybatis/mybatis-config.xml")).parse();
            mapperRegistry = new MapperRegistry(configuration);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void testSqlSession() {

        try (SqlSession sqlSession = new DefaultSqlSessionFactory(configuration, mapperRegistry).openSession()) {
           sqlSession.selectOne("com.bhuang.mapper.DepartmentMapper.findById", "00000000000000000000000000000000");
        }
    }
}
