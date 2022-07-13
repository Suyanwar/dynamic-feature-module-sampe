package com.example.dynamicfeaturemoduleexample

/**
 * Created by Suyanwar on 13/07/22.
 */
private val classMap = mutableMapOf<String, Class<*>>()
fun <T> String.loadClassOrNull(): Class<out T>? =
    classMap.getOrPut(this) {
        try {
            Class.forName(this)
        } catch (e: ClassNotFoundException) {
            return null
        }
    }.castOrNull()

inline fun <reified T : Any> Any.castOrNull() = this as? T