package com.bhuang.mapper

import com.bhuang.model.DepartmentEntity

interface DepartmentMapper {
    fun findAll(): List<DepartmentEntity?>?

    fun insert(departmentEntity: DepartmentEntity): Int

    fun update(departmentEntity: DepartmentEntity): Int

    fun deleteById(id: String?): Int

    fun findById(id: String): DepartmentEntity?
}