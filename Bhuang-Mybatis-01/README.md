# MyBatis概述
MyBatis 是一个支持自定义 SQL、存储过程以及高级映射的持久层框架。它是 Apache 的一个开源项目，主要用于 Java 应用程序的持久层开发。

### MyBatis 的核心特点

1. **简化数据库操作**：MyBatis 通过 XML 文件或注解方式将 SQL 语句与 Java 对象进行映射，简化了数据访问层的开发。开发者只需要关注 SQL 本身和数据库的交互逻辑，而不需要关心 SQL 的生成过程。

2. **灵活的 SQL 映射**：MyBatis 允许直接编写 SQL 语句，并支持动态 SQL，使得 SQL 语句能够根据条件动态生成，这种灵活性比起 ORM 框架（如 Hibernate）更为强大。

3. **对象关系映射（ORM）支持**：虽然 MyBatis 不像 Hibernate 那样是一个全功能的 ORM 框架，但它支持从 SQL 结果集映射到 Java 对象的功能，并且允许高度自定义映射规则。

4. **事务管理**：MyBatis 支持与 Spring 等框架集成，提供了对事务管理的支持。开发者可以选择手动管理事务，也可以使用 Spring 的声明式事务管理功能。

5. **缓存支持**：MyBatis 提供一级缓存和二级缓存，一级缓存是默认启用的，作用于单个 SQLSession 范围内。二级缓存是可选的，可以在多个 SQLSession 之间共享数据，通常需要在配置中显式启用。

6. **易于集成**：MyBatis 非常容易与现有的 Java EE 应用集成，尤其是与 Spring 框架配合使用时，可以很好地简化事务管理、依赖注入等工作。

7. **可扩展性**：MyBatis 提供了丰富的插件机制，可以自定义插件来扩展 MyBatis 的功能，如自定义日志输出、SQL 拦截器等。

### MyBatis 的工作原理

MyBatis 的核心是通过 SQL 映射文件将 SQL 语句与 Java 对象关联起来。在执行 SQL 语句时，MyBatis 会根据映射配置，将 SQL 的执行结果映射为 Java 对象，或者将 Java 对象的属性值映射为 SQL 查询条件。

通常，MyBatis 的工作流程如下：

1. **配置阶段**：加载并解析 MyBatis 配置文件与映射文件，建立映射关系。
2. **SQL 执行阶段**：在应用中调用 MyBatis 提供的 API 来执行映射的 SQL 语句。
3. **结果映射阶段**：将 SQL 执行结果映射为预先定义好的 Java 对象，返回给调用者。

### MyBatis 的优势

- **控制力强**：开发者可以完全掌控 SQL 执行过程，这对于一些复杂的查询和优化要求非常有用。
- **性能优化**：相比全功能 ORM 框架，MyBatis 的性能开销较小，因为它不强制使用缓存和延迟加载等特性。
- **灵活性高**：由于直接使用 SQL，开发者可以充分利用数据库特性，如存储过程、触发器等。

### MyBatis 的不足

- **手动编写 SQL**：虽然这种方式提供了更大的灵活性，但也意味着需要手动维护 SQL 语句，增加了开发和维护的工作量。
- **与数据库耦合紧密**：因为直接使用 SQL，MyBatis 应用程序在数据库迁移或升级时可能需要较多调整。

MyBatis 是一个非常适合那些需要高度定制化 SQL 操作的项目，尤其是在复杂的查询和数据映射需求下，它能够提供很大的帮助。

# MyBatis的整体架构
![img.png](img%2Fimg.png)

MyBatis 的整体架构可以按照接口层、核心层和支持层来划分，这样的分层设计有助于理解 MyBatis 各个组件之间的关系和职责。

### 1. **接口层 (API Layer)**

接口层是 MyBatis 提供给开发者使用的 API，是 MyBatis 与应用程序进行交互的入口。它主要包括以下几个部分：

- **SqlSessionFactory**：用于创建 SqlSession 的工厂接口。通过 SqlSessionFactory，开发者可以获取 SqlSession 对象，并开始进行数据库操作。

- **SqlSession**：MyBatis 提供的核心接口，用于执行数据库操作。开发者通过这个接口来执行 SQL 语句、提交或回滚事务、获取映射器（Mapper）等。

- **Mapper Interface**：Mapper 接口是 MyBatis 提供的另一种 API，它将 SQL 语句和 Java 方法进行绑定。开发者定义的接口方法对应一个 SQL 语句，MyBatis 会为这些接口生成实现类，通过动态代理实现 SQL 的执行。

### 2. **核心层 (Core Layer)**

核心层是 MyBatis 的核心部分，负责管理和执行与数据库交互的具体逻辑。它主要包括以下几个组件：

- **Configuration**：MyBatis 的全局配置对象，包含了所有 MyBatis 的配置细节，包括数据库连接池、事务管理器、Mapper 映射信息、插件配置等。它在 MyBatis 初始化时被加载，并在整个应用生命周期内被使用。

- **Mapped Statements**：这些是根据配置文件或注解定义的 SQL 语句映射信息。每个 Mapped Statement 包含了 SQL 语句、输入参数类型、输出结果类型以及结果映射规则。

- **Executor**：执行器是核心层的重要组件，它负责执行具体的 SQL 语句。MyBatis 提供了不同类型的 Executor，例如 SimpleExecutor、BatchExecutor 和 ReuseExecutor，它们分别适用于不同的场景和性能要求。Executor 还负责管理缓存和事务。

- **Statement Handler**：负责将 Mapped Statement 转换为 JDBC Statement 并执行。它与 Executor 协同工作，通过 Type Handlers 和 Parameter Handlers 处理输入参数和返回结果。

- **ResultSet Handler**：处理 SQL 执行后的结果集，并将其映射为相应的 Java 对象。ResultSet Handler 会使用 ResultMap 进行复杂的结果映射处理。

- **Parameter Handler**：负责将 Java 对象转换为 SQL 语句中的参数值，并设置到 JDBC PreparedStatement 中。

### 3. **支持层 (Support Layer)**

支持层包含 MyBatis 的辅助功能和扩展功能，帮助核心层更好地完成任务。它主要包括以下部分：

- **Type Handlers**：用于在 Java 类型和 JDBC 类型之间进行转换。MyBatis 提供了许多内置的 Type Handlers，例如将字符串转换为 SQL 的 VARCHAR 类型，将日期类型转换为 SQL 的 DATE 类型等。开发者也可以自定义 Type Handlers 以满足特定的需求。

- **Plugins**：MyBatis 提供的插件机制，允许开发者通过插件来扩展 MyBatis 的功能。插件可以拦截 Executor、Statement Handler、Parameter Handler 和 ResultSet Handler 的方法，从而实现日志记录、性能监控、权限控制等功能。

- **Caching**：缓存是 MyBatis 提供的性能优化功能，分为一级缓存（SqlSession 范围内）和二级缓存（全局或 Mapper 范围内）。缓存机制帮助减少数据库访问次数，从而提高性能。

- **Transaction Management**：MyBatis 支持与 Spring 等框架集成，提供声明式或编程式的事务管理功能。它可以管理事务的提交和回滚操作，确保数据一致性。

### 总结

- **接口层**：提供 MyBatis 的 API 给开发者使用，如 SqlSessionFactory、SqlSession 和 Mapper 接口。
- **核心层**：包含执行 SQL 的核心逻辑，如 Configuration、Mapped Statements、Executor、Statement Handler 等。
- **支持层**：提供辅助功能和扩展机制，如 Type Handlers、Plugins、Caching 和 Transaction Management。

这种分层架构设计使得 MyBatis 在实现复杂功能的同时保持了良好的模块化和可扩展性，开发者可以根据需要对不同层次进行自定义或扩展。

# 快速开始
要快速开始使用 MyBatis，以下是一个简要的步骤指南。我们将通过一个简单的示例来演示如何使用 MyBatis 进行基本的数据库操作。

### 1. 引入 MyBatis 依赖

首先，在你的项目中引入 MyBatis 所需的依赖。如果你使用的是 Maven，可以在 `pom.xml` 中添加以下依赖：

```xml
<dependencies>
    <dependency>
        <groupId>org.mybatis</groupId>
        <artifactId>mybatis</artifactId>
        <version>3.5.10</version>
    </dependency>
    <dependency>
        <groupId>org.mybatis</groupId>
        <artifactId>mybatis-spring</artifactId>
        <version>2.0.7</version>
    </dependency>
    <!-- 添加数据库驱动，例如 H2 数据库 -->
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <version>1.4.200</version>
    </dependency>
</dependencies>
```

### 2. 创建数据库和表

假设我们要使用 H2 内存数据库并创建一个简单的 `User` 表。首先，我们需要配置数据库，并创建相应的表结构。

```sql
CREATE TABLE User (
    id INT PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100)
);
```

### 3. 配置 MyBatis

MyBatis 可以通过 XML 配置文件进行配置。以下是一个简单的 `mybatis-config.xml` 配置文件示例：

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
  PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
  "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <environments default="development">
        <environment id="development">
            <transactionManager type="JDBC"/>
            <dataSource type="POOLED">
                <property name="driver" value="org.h2.Driver"/>
                <property name="url" value="jdbc:h2:mem:testdb"/>
                <property name="username" value="sa"/>
                <property name="password" value=""/>
            </dataSource>
        </environment>
    </environments>

    <mappers>
        <mapper resource="com/example/mybatis/UserMapper.xml"/>
    </mappers>
</configuration>
```

### 4. 创建 Mapper 接口和 XML 映射文件

定义一个 Mapper 接口和对应的 XML 文件，用来映射 SQL 操作。

#### UserMapper.java
```java
package com.example.mybatis;

import org.apache.ibatis.annotations.Select;
import java.util.List;

public interface UserMapper {
    @Select("SELECT * FROM User WHERE id = #{id}")
    User selectUser(int id);

    List<User> selectAllUsers();
}
```

#### UserMapper.xml
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
  PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
  "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.example.mybatis.UserMapper">

    <select id="selectAllUsers" resultType="com.example.mybatis.User">
        SELECT * FROM User
    </select>

</mapper>
```

### 5. 创建 Java 对象

创建一个与数据库表相对应的 Java 类。

#### User.java
```java
package com.example.mybatis;

public class User {
    private int id;
    private String name;
    private String email;

    // Getters and setters...
}
```

### 6. 使用 MyBatis 进行数据库操作

最后，编写一个简单的 Java 程序来使用 MyBatis 进行数据库操作。

```java
package com.example.mybatis;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;
import java.util.List;

public class MyBatisApp {
    public static void main(String[] args) throws Exception {
        // 1. 加载 MyBatis 配置文件
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        // 2. 获取 SqlSession
        try (SqlSession session = sqlSessionFactory.openSession()) {
            UserMapper mapper = session.getMapper(UserMapper.class);

            // 3. 插入示例数据
            session.insert("insert into User(id, name, email) values(1, 'John Doe', 'john@example.com')");
            session.insert("insert into User(id, name, email) values(2, 'Jane Roe', 'jane@example.com')");
            session.commit();

            // 4. 查询数据
            User user = mapper.selectUser(1);
            System.out.println("User ID 1: " + user.getName());

            List<User> users = mapper.selectAllUsers();
            for (User u : users) {
                System.out.println("User: " + u.getName());
            }
        }
    }
}
```

### 7. 运行程序

编译并运行该程序，你将看到控制台输出查询到的用户信息。这个简单的例子展示了如何使用 MyBatis 进行数据库操作。

### 总结

通过以上步骤，你已经成功地使用 MyBatis 进行了数据库连接配置、映射、以及基本的查询操作。这个入门示例可以扩展到更复杂的场景，包括使用高级的 MyBatis 特性如动态 SQL、缓存、插件等。

# 基础回顾-单表增删改查
## 基于原始Dao的开发方式
你提到的是 MyBatis 中基于原始 DAO 的开发方式，以及它存在的一些问题。这种方式虽然比较传统，但在某些场景下仍然可能被使用。下面，我将详细解释这种原始 DAO 开发方式，以及它的优缺点。

### 基于原始 DAO 的开发方式

####  DAO 接口与实现类

在基于原始 DAO 的开发方式中，开发者需要手动创建 DAO 接口和实现类。DAO（数据访问对象）接口定义了数据访问的方法，而实现类负责具体的数据库操作。

**DAO 接口：**
```java
public interface DepartmentDao {
    List<Department> findAll();
    Department findById(String id);
}
```

**DAO 实现类：**
```java
public class DepartmentDaoImpl implements DepartmentDao {

    private SqlSessionFactory sqlSessionFactory;

    // 构造方法注入 SqlSessionFactory
    public DepartmentDaoImpl(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Override
    public List<Department> findAll() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            return sqlSession.selectList("departmentMapper.findAll");
        }
    }

    @Override
    public Department findById(String id) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            return sqlSession.selectOne("departmentMapper.findById", id);
        }
    }
}
```

**关键点：**

- `SqlSessionFactory` 通过构造方法注入到 DAO 实现类中，确保整个应用中只有一个 `SqlSessionFactory` 实例。
- `SqlSession` 是从 `SqlSessionFactory` 获取的，每次操作都要从中获取新的 `SqlSession`，并在操作完成后关闭它。
- 使用了 try-with-resources 来确保 `SqlSession` 在使用后自动关闭。

####  Mapper XML 文件

为了使 DAO 实现类能够执行 SQL 语句，我们需要在 XML 文件中定义 SQL 语句。这个 XML 文件与 DAO 实现类中提到的 SQL 语句 ID 对应。

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="departmentMapper">
    <select id="findAll" resultType="com.linkedbear.mybatis.entity.Department">
        SELECT * FROM tbl_department
    </select>

    <select id="findById" parameterType="string" resultType="com.linkedbear.mybatis.entity.Department">
        SELECT * FROM tbl_department WHERE id = #{id}
    </select>

    <insert id="insert" parameterType="com.linkedbear.mybatis.entity.Department">
        INSERT INTO tbl_department (id, name, tel) VALUES (#{id}, #{name}, #{tel})
    </insert>

    <update id="update" parameterType="com.linkedbear.mybatis.entity.Department">
        UPDATE tbl_department SET name = #{name}, tel = #{tel} WHERE id = #{id}
    </update>

    <delete id="deleteById" parameterType="string">
        DELETE FROM tbl_department WHERE id = #{id}
    </delete>
</mapper>
```

**关键点：**

- XML 文件中定义的 SQL 语句通过 `id` 属性与 DAO 实现类中的方法绑定。
- 每个 SQL 语句的 `id` 应该是唯一的，以确保在 DAO 实现类中能够正确调用对应的 SQL 语句。

###  原始 DAO 开发的弊端

如你所指出的，这种原始 DAO 开发方式存在一些明显的弊端：

1. **重复代码**：
    - 每个 DAO 实现类的方法中都有类似的代码结构：获取 `SqlSession`、执行 SQL 语句、关闭 `SqlSession`。这些代码几乎是重复的，增加了维护的复杂度。

2. **扩展性差**：
    - 如果需要修改 SQL 语句或增加新的功能，开发者必须手动在多个 DAO 实现类中添加或修改代码，这增加了出错的概率。

3. **开发效率低**：
    - 相比 Mapper 动态代理方式，原始 DAO 开发方式需要编写大量的模板代码，这在项目规模较大时，显得尤为低效。

### 改进：使用 Mapper 动态代理

为了克服上述弊端，MyBatis 提供了 Mapper 动态代理的开发方式。通过这种方式，开发者只需定义 Mapper 接口，不需要编写实现类。MyBatis 会在运行时自动生成这些实现类并执行对应的 SQL 语句。

**使用动态代理的优势：**

- **减少重复代码**：动态代理自动处理了获取 `SqlSession`、执行 SQL 语句等重复操作。
- **提高开发效率**：开发者只需定义接口和 SQL 语句，大大减少了模板代码的编写量。
- **更好的维护性**：更少的代码意味着更容易维护和扩展。

### 总结
尽管原始 DAO 开发方式提供了对数据库操作的完全控制，但它的重复代码和低效开发流程使其在大型项目中不太理想。使用 MyBatis 的 Mapper 动态代理能够大幅简化代码，提高开发效率，同时减少出错的可能性。因此，在实际开发中，建议使用 Mapper 动态代理的方式来替代传统的原始 DAO 开发方式。

# 基于Mapper动态代理的开发方式
## 使用 Mapper 动态代理的方式开发，需要满足以下几个规范：
在使用 MyBatis 的 Mapper 动态代理方式进行开发时，需要遵循一些规范和约定，以确保 MyBatis 能够正确地工作并生成代理对象。以下是几个关键的规范：

### 1. **Mapper 接口与 XML 映射文件的绑定**

- **命名规范**：Mapper 接口的全限定名（包括包名和接口名）应该与对应的 XML 映射文件的 namespace 一致。通常，XML 映射文件的命名与接口一致，如 `UserMapper.java` 对应 `UserMapper.xml`。

  例如：
  ```java
  package com.example.mybatis;
  
  public interface UserMapper {
      User selectUser(int id);
  }
  ```

  对应的 `UserMapper.xml`：
  ```xml
  <mapper namespace="com.example.mybatis.UserMapper">
      <!-- SQL 映射 -->
  </mapper>
  ```

- **方法名与 SQL 语句的 ID 对应**：Mapper 接口的方法名应与 XML 映射文件中定义的 SQL 语句的 ID 相同，这样 MyBatis 可以正确地找到对应的 SQL 语句执行。

  例如，`UserMapper` 中的方法名 `selectUser` 应对应 XML 中的 `<select>` 标签的 ID：
  ```xml
  <select id="selectUser" resultType="com.example.mybatis.User">
      SELECT * FROM User WHERE id = #{id}
  </select>
  ```

### 2. **Mapper 接口与方法规范**

- **接口**：Mapper 必须是接口，MyBatis 会为该接口生成一个实现类，该实现类会通过动态代理的方式实现接口中的方法。

- **方法参数与 SQL 语句参数匹配**：Mapper 接口方法的参数名称或顺序需要与 SQL 语句中的参数占位符匹配，确保参数能够正确地传递到 SQL 语句中。

    - 如果使用单个参数且为基本类型或包装类型，可以直接在 SQL 中使用 `#{paramName}` 来引用参数。
    - 如果方法有多个参数，应该使用 `@Param` 注解来指定参数名称，或者使用一个包含所有参数的对象。

  例如：
  ```java
  User selectUser(@Param("id") int id, @Param("name") String name);
  ```
  对应的 SQL 语句：
  ```xml
  <select id="selectUser" resultType="com.example.mybatis.User">
      SELECT * FROM User WHERE id = #{id} AND name = #{name}
  </select>
  ```

### 3. **Mapper 方法的返回类型**

- **简单返回类型**：方法可以返回基本类型、包装类型或字符串等简单类型，这种情况通常用于查询单一值，如查询总数、单列值等。

  例如：
  ```java
  int countUsers();
  ```

- **对象类型**：方法可以返回一个与数据库表对应的对象类型。通常用于查询单行记录并将其映射为 Java 对象。

  例如：
  ```java
  User selectUser(int id);
  ```

- **集合类型**：方法可以返回 `List`、`Set` 等集合类型，通常用于查询多行记录。

  例如：
  ```java
  List<User> selectAllUsers();
  ```

### 4. **SQL 注解的使用规范**

- **使用注解**：可以直接在接口方法上使用注解来定义 SQL 语句，例如 `@Select`、`@Insert`、`@Update`、`@Delete` 等。需要注意 SQL 注解只适用于简单的 SQL 语句，对于复杂的 SQL 语句建议使用 XML 文件来定义。

  例如：
  ```java
  @Select("SELECT * FROM User WHERE id = #{id}")
  User selectUser(int id);
  ```

- **避免复杂逻辑**：如果 SQL 语句较为复杂，或者需要动态拼接 SQL，则应使用 XML 文件来定义而不是注解。因为注解内定义的 SQL 语句不支持复杂的动态 SQL 构建。

### 5. **配置文件的正确配置**

- **配置映射**：在 MyBatis 的配置文件（如 `mybatis-config.xml`）中，需要正确配置 Mapper 接口的扫描路径或手动注册 Mapper。

    - 使用包扫描：
      ```xml
      <mappers>
          <package name="com.example.mybatis"/>
      </mappers>
      ```

    - 或者逐个注册：
      ```xml
      <mappers>
          <mapper class="com.example.mybatis.UserMapper"/>
      </mappers>
      ```

### 6. **Mapper 接口不应该包含实现代码**

- **纯接口**：Mapper 接口不应包含任何实现代码，因为 MyBatis 会自动生成实现类。所有的方法定义应只是抽象方法。

  例如：
  ```java
  public interface UserMapper {
      User selectUser(int id);  // 不要在接口中编写实现
  }
  ```

### 7. **注意类型一致性**

- **一致性**：确保 Mapper 方法的参数类型、返回类型与 SQL 语句中的数据类型一致。例如，如果数据库返回一个 `int` 类型的值，那么 Mapper 方法的返回类型也应为 `int` 或对应的包装类型 `Integer`。

### 总结

遵循这些规范可以确保 MyBatis 在使用 Mapper 动态代理时能够正确地工作，并且有助于维护代码的可读性和可维护性。通过保持接口的简洁性、使用正确的映射方式以及保证类型一致性，开发者可以有效地利用 MyBatis 提供的强大功能，实现高效、简洁的持久层开发。

# 基础回顾-关联表查询
