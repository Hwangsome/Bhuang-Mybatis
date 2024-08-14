package com.bhuang.ibatis.bindings;

import org.apache.ibatis.annotations.Flush;
import org.apache.ibatis.binding.BindingException;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.session.Configuration;

import java.lang.reflect.Method;
import java.util.HashMap;

public class MapperMethod {
    private final SqlCommand command;
    private final MethodSignature method;

    public MapperMethod(Class<?> mapperInterface, Method method, Configuration config) {
        this.command = new SqlCommand(config, mapperInterface, method);
        this.method = new MethodSignature(config, mapperInterface, method);
    }

    public static class SqlCommand {
        // com.bhuang.mapper.DepartmentMapper.findAll (接口全类名 + 方法名)
        private final String name;
        // 查看你的mapper.xml中的sql语句类型 （CRUD）
        private final SqlCommandType type;
        public String getName() {
            return name;
        }

        public SqlCommandType getType() {
            return type;
        }

        public SqlCommand(Configuration config, Class<?> mapperInterface, Method method) {
            final String methodName = method.getName();
            final Class<?> declaringClass = method.getDeclaringClass();
            MappedStatement ms = resolveMappedStatement(mapperInterface, methodName, declaringClass, config);
            if (ms == null) {
                if (method.getAnnotation(Flush.class) == null) {
                    throw new BindingException(
                            "Invalid bound statement (not found): " + mapperInterface.getName() + "." + methodName);
                }
                name = null;
                type = SqlCommandType.FLUSH;
            } else {
                name = ms.getId();
                type = ms.getSqlCommandType();
                if (type == SqlCommandType.UNKNOWN) {
                    throw new BindingException("Unknown execution method for: " + name);
                }
            }
        }

        //
        private MappedStatement resolveMappedStatement(Class<?> mapperInterface, String methodName, Class<?> declaringClass,
                                                       Configuration configuration) {
            // 生成 statementId
            String statementId = mapperInterface.getName() + "." + methodName;
            if (configuration.hasStatement(statementId)) {
                // 根据 statementId 获取 MappedStatement
                // mappedStatement 在解析配置文件的时候会被缓存到mappedStatements 中
                return configuration.getMappedStatement(statementId);
            }
            if (mapperInterface.equals(declaringClass)) {
                return null;
            }
            for (Class<?> superInterface : mapperInterface.getInterfaces()) {
                if (declaringClass.isAssignableFrom(superInterface)) {
                    MappedStatement ms = resolveMappedStatement(superInterface, methodName, declaringClass, configuration);
                    if (ms != null) {
                        return ms;
                    }
                }
            }
            return null;
        }
    }

    public static class ParamMap<V> extends HashMap<String, V> {

        private static final long serialVersionUID = -2212268410512043556L;

        @Override
        public V get(Object key) {
            if (!super.containsKey(key)) {
                throw new BindingException("Parameter '" + key + "' not found. Available parameters are " + keySet());
            }
            return super.get(key);
        }

    }

    public static class MethodSignature {

        public MethodSignature(Configuration config, Class<?> mapperInterface, Method method) {

        }
    }
}
