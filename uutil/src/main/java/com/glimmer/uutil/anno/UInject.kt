package com.glimmer.uutil.anno

import androidx.lifecycle.MutableLiveData
import com.tencent.mmkv.MMKV

object UInject {
    private val map = hashMapOf<String, Any>()

    fun inject(any: Any) {
        val clz = any::class.java
        val fields = clz.declaredFields
        fields.forEach { field ->
            field.getAnnotation(LocalKV::class.java)?.apply {
                MMKV.defaultMMKV().let { mmkv ->
                    // 取值
                    val value = mmkv.decodeString(key, defaultValue)
                    // 初始化保存
                    if (value == defaultValue) {
                        mmkv.encode(key, value)
                    }

                    if (!map.containsKey(key)) {
                        map[key] = MutableLiveData<Any>(field)
                    }
                    (map[key] as? MutableLiveData<*>)?.observeForever {
                        println()
                    }

                    field.isAccessible = true
                    field.set(any, value)
                }
            }
        }
    }

}

