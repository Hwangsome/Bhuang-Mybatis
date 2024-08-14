package com.bhuang.model

open class DepartmentEntity(
    var id: String? = null,
    var name: String? = null,
    var tel: String? = null
) {
    override fun toString(): String {
        return "DepartmentEntity(id=$id, name=$name, tel=$tel)"
    }
}
