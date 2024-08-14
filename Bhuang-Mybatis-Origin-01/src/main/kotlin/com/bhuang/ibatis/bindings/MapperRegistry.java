package com.bhuang.ibatis.bindings;

import com.bhuang.ibatis.sessions.SqlSession;
import org.apache.ibatis.binding.BindingException;
import org.apache.ibatis.builder.annotation.MapperAnnotationBuilder;
import org.apache.ibatis.io.ResolverUtil;
import org.apache.ibatis.session.Configuration;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * MapperRegistry 这个类将在Configuration中被使用，它是一个Mapper接口的注册中心，负责管理Mapper接口的注册和获取。
 *
 */
public class MapperRegistry {

    private final Configuration config;

    // 将Mapper接口注册到knownMappers中，key为Mapper接口的Class对象，value为MapperProxyFactory对象
    private final Map<Class<?>, MapperProxyFactory<?>> knownMappers = new ConcurrentHashMap<>();

    public MapperRegistry(Configuration config) {
        this.config = config;
    }

    /**
     *
     * @param type mapper接口 的class
     * @param sqlSession
     * @return
     * @param <T> 获取mapper接口的代理对象
     */

    @SuppressWarnings("unchecked")
    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        final MapperProxyFactory<T> mapperProxyFactory = (MapperProxyFactory<T>) knownMappers.get(type);
        if (mapperProxyFactory == null) {
            throw new BindingException("Type " + type + " is not known to the MapperRegistry.");
        }
        try {
            // mapperProxyFactory 中创建一个mapperProxy
            return mapperProxyFactory.newInstance(sqlSession);
        } catch (Exception e) {
            throw new BindingException("Error getting mapper instance. Cause: " + e, e);
        }
    }

    public <T> boolean hasMapper(Class<T> type) {
        return knownMappers.containsKey(type);
    }

    public <T> void addMapper(Class<T> type) {
        if (type.isInterface()) {
            if (hasMapper(type)) {
                throw new BindingException("Type " + type + " is already known to the MapperRegistry.");
            }
            boolean loadCompleted = false;
            try {
                knownMappers.put(type, new MapperProxyFactory<>(type));
                // It's important that the type is added before the parser is run
                // otherwise the binding may automatically be attempted by the
                // mapper parser. If the type is already known, it won't try.
                MapperAnnotationBuilder parser = new MapperAnnotationBuilder(config, type);
                parser.parse();
                loadCompleted = true;
            } finally {
                if (!loadCompleted) {
                    knownMappers.remove(type);
                }
            }
        }
    }

    /**
     * Gets the mappers.
     *
     * @return the mappers
     *
     * @since 3.2.2
     */
    public Collection<Class<?>> getMappers() {
        return Collections.unmodifiableCollection(knownMappers.keySet());
    }

    /**
     * Adds the mappers.
     *
     * @param packageName
     *          the package name
     * @param superType
     *          the super type
     *
     * @since 3.2.2
     */
    public void addMappers(String packageName, Class<?> superType) {
        ResolverUtil<Class<?>> resolverUtil = new ResolverUtil<>();
        resolverUtil.find(new ResolverUtil.IsA(superType), packageName);
        Set<Class<? extends Class<?>>> mapperSet = resolverUtil.getClasses();
        for (Class<?> mapperClass : mapperSet) {
            addMapper(mapperClass);
        }
    }

    /**
     * Adds the mappers.
     *
     * @param packageName
     *          the package name
     *
     * @since 3.2.2
     */
    public void addMappers(String packageName) {
        addMappers(packageName, Object.class);
    }

}
