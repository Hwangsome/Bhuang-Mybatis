package com.bhuang.ibatis.bindings;

import com.bhuang.ibatis.sessions.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;


public class MapperProxy<T> implements InvocationHandler, Serializable {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final SqlSession sqlSession;
    private final Class<T> mapperInterface;
    private final Map<Method, MapperMethodInvoker> methodCache;

    public MapperProxy(SqlSession sqlSession, Class<T> mapperInterface, Map<Method, MapperMethodInvoker> methodCache) {
        this.sqlSession = sqlSession;
        this.mapperInterface = mapperInterface;
        this.methodCache = methodCache;
    }

    /**
     *
     * @param proxy the proxy instance that the method was invoked on
     *
     * @param method the {@code Method} instance corresponding to
     * the interface method invoked on the proxy instance.  The declaring
     * class of the {@code Method} object will be the interface that
     * the method was declared in, which may be a superinterface of the
     * proxy interface that the proxy class inherits the method through.
     *
     * @param args an array of objects containing the values of the
     * arguments passed in the method invocation on the proxy instance,
     * or {@code null} if interface method takes no arguments.
     * Arguments of primitive types are wrapped in instances of the
     * appropriate primitive wrapper class, such as
     * {@code java.lang.Integer} or {@code java.lang.Boolean}.
     *
     * @return
     * @throws Throwable
     * 当调用Mapper接口中的方法的时候，会去调用到MapperProxy的invoke方法
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
            // 不代理Object类中的方法
            if (Object.class.equals(method.getDeclaringClass())) {
                return method.invoke(this, args);
            }
            return cachedInvoker(method).invoke(proxy, method, args, sqlSession);
        } catch (Throwable e) {
            logger.error("Error invoking method {} on mapper interface {}", method, mapperInterface, e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 缓存一下Mapper接口中的方法
     * @param method
     * @return
     * @throws Throwable
     */
    private MapperMethodInvoker cachedInvoker(Method method) throws Throwable {
        methodCache.computeIfAbsent(method, m ->{
            return new PlainMethodInvoker(new MapperMethod(mapperInterface, method, sqlSession.getConfiguration()));
        });
        return null;
    }

    interface MapperMethodInvoker {
        Object invoke(Object proxy, Method method, Object[] args, SqlSession sqlSession) throws Throwable;
    }

    private static class PlainMethodInvoker implements MapperMethodInvoker {
        private MapperMethod mapperMethod;
        public PlainMethodInvoker(MapperMethod mapperMethod) {
            this.mapperMethod = mapperMethod;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args, SqlSession sqlSession) throws Throwable {
            return null;
        }
    }
}