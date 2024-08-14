package com.bhuang.mapper

import com.bhuang.model.DepartmentEntity
import com.bhuang.model.UserEntity

interface UserMapper {
    fun findAll(): List<UserEntity?>?
    fun findAllUserByLazy(): List<UserEntity?>?

}