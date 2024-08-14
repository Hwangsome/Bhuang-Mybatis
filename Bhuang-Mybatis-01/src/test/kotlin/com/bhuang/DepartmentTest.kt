package com.bhuang

import com.bhuang.mapper.DepartmentMapper
import com.bhuang.mapper.UserMapper
import com.bhuang.model.DepartmentEntity
import mu.KLogging
import org.apache.ibatis.io.Resources
import org.apache.ibatis.session.SqlSession
import org.apache.ibatis.session.SqlSessionFactoryBuilder
import org.junit.jupiter.api.Test
import java.io.InputStream


class DepartmentTest {
    companion object {
        private val logger = KLogging().logger()
    }

    private val resource: InputStream = Resources.getResourceAsStream("mybatis/mybatis-config.xml")
    private var sqlSession: SqlSession

    init {
        sqlSession = SqlSessionFactoryBuilder().build(resource).openSession()
    }



    @Test
    fun happyPathForMybatis() {
        sqlSession.selectList<DepartmentEntity>("com.bhuang.mapper.DepartmentMapper.findAll").forEach {
            logger.info { "department: $it" }
        }
    }

    @Test
    fun testMybatisMapper(){
        // 获取Mapper接口的代理
       sqlSession.getMapper(DepartmentMapper::class.java).findAll()?.forEach {
           logger.info { "department: $it" }
       }
    }

    /**
     * 关联表的查询
     */
    @Test
    fun assosiationTableTest() {
        val userMapper = sqlSession.getMapper(UserMapper::class.java)
        val userEntitys = userMapper.findAll()
        userEntitys?.forEach {
            logger.info { "user: $it" }
        }
    }

    /**
     * 测试懒加载
     * 我在Entity中重写了tostring, 所以输出userEntity的时候会 调用到department的信息，所以会触发懒加载
     * 当你   logger.info { "$it" } 的时候，会去调用toString方法，所以会触发懒加载
     * 但是当你    logger.info { "${it?.name}" } 的时候，不会触发懒加载，因为这里仅仅是需要name属性
     *
     *
     * 非懒加载的日志：
     * PooledDataSource forcefully closed/removed all connections.
     * PooledDataSource forcefully closed/removed all connections.
     * PooledDataSource forcefully closed/removed all connections.
     * PooledDataSource forcefully closed/removed all connections.
     * Opening JDBC Connection
     * Created connection 1168079523.
     * Setting autocommit to false on JDBC Connection [com.mysql.cj.jdbc.ConnectionImpl@459f7aa3]
     * ==>  Preparing: select * from tbl_user
     * ==> Parameters:
     * <==    Columns: id, version, name, age, birthday, department_id, sorder, deleted
     * <==        Row: 09ec5fcea620c168936deee53a9cdcfb, 0, 阿熊, 18, 2003-08-08 10:00:00, 18ec781fbefd727923b0d35740b177ab, 1, 0
     * <==        Row: 5d0eebc4f370f3bd959a4f7bc2456d89, 0, 老狗, 30, 1991-02-20 15:27:20, ee0e342201004c1721e69a99ac0dc0df, 1, 0
     * <==      Total: 2
     * 10:47:38.173 [main] INFO mu.KLogging -- 阿熊
     * 10:47:38.174 [main] INFO mu.KLogging -- 老狗
     * PooledDataSource forcefully closed/removed all connections.
     *
     * 懒加载的日志
     *
     * Logging initialized using 'class org.apache.ibatis.logging.stdout.StdOutImpl' adapter.
     * PooledDataSource forcefully closed/removed all connections.
     * PooledDataSource forcefully closed/removed all connections.
     * PooledDataSource forcefully closed/removed all connections.
     * PooledDataSource forcefully closed/removed all connections.
     * Opening JDBC Connection
     * Created connection 1168079523.
     * Setting autocommit to false on JDBC Connection [com.mysql.cj.jdbc.ConnectionImpl@459f7aa3]
     * ==>  Preparing: select * from tbl_user
     * ==> Parameters:
     * <==    Columns: id, version, name, age, birthday, department_id, sorder, deleted
     * <==        Row: 09ec5fcea620c168936deee53a9cdcfb, 0, 阿熊, 18, 2003-08-08 10:00:00, 18ec781fbefd727923b0d35740b177ab, 1, 0
     * <==        Row: 5d0eebc4f370f3bd959a4f7bc2456d89, 0, 老狗, 30, 1991-02-20 15:27:20, ee0e342201004c1721e69a99ac0dc0df, 1, 0
     * <==      Total: 2
     * ==>  Preparing: select * from tbl_department where id = ?
     * ==> Parameters: 18ec781fbefd727923b0d35740b177ab(String)
     * <==    Columns: id, name, tel
     * <==        Row: 18ec781fbefd727923b0d35740b177ab, 开发部, 123
     * <==      Total: 1
     * 10:50:33.188 [main] INFO mu.KLogging -- UserEntity(id=09ec5fcea620c168936deee53a9cdcfb, name=阿熊, age=18, birthday=Fri Aug 08 10:00:00 CST 2003, department=DepartmentEntity(id=18ec781fbefd727923b0d35740b177ab, name=开发部, tel=123))
     * ==>  Preparing: select * from tbl_department where id = ?
     * ==> Parameters: ee0e342201004c1721e69a99ac0dc0df(String)
     * <==    Columns: id, name, tel
     * <==        Row: ee0e342201004c1721e69a99ac0dc0df, 运维部, 456
     * <==      Total: 1
     * 10:50:33.191 [main] INFO mu.KLogging -- UserEntity(id=5d0eebc4f370f3bd959a4f7bc2456d89, name=老狗, age=30, birthday=Wed Feb 20 15:27:20 CST 1991, department=DepartmentEntity(id=ee0e342201004c1721e69a99ac0dc0df, name=运维部, tel=456))
     * PooledDataSource forcefully closed/removed all connections.
     *
     * 需要用到department的信息，所以会触发懒加载
     */
    @Test
    fun lazyTest() {
        val userMapper = sqlSession.getMapper(UserMapper::class.java)
       // userMapper.findAllUserByLazy()
        userMapper.findAllUserByLazy()?.forEach {
            logger.info { "$it" }
        }

    }
}