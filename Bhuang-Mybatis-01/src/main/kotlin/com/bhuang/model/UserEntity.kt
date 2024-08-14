package com.bhuang.model

import java.util.Date

// Kotlin 默认的类是 final 的，但为了使用 MyBatis 的延迟加载功能，你需要将相关类声明为 open，以允许 MyBatis 为这些类生成代理对象。
open class UserEntity (
    var id: String? = null,
    var name: String? = null,
    var age: Int? = null,
    var birthday: Date? = null,
    var department: DepartmentEntity? = null
) {
    override fun toString(): String {
        return "UserEntity(id=$id, name=$name, age=$age, birthday=$birthday, department=$department)"
    }
}