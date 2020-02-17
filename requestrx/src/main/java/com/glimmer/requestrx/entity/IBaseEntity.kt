package com.glimmer.requestrx.entity

interface IBaseEntity {
    fun getCode(): Int

    fun getMsg(): String?

    fun isSuccess(): Boolean
}