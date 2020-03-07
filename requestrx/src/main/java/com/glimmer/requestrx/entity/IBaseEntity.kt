package com.glimmer.requestrx.entity

interface IBaseEntity {
    fun getCode(): String

    fun getMsg(): String?

    fun isSuccess(): Boolean
}