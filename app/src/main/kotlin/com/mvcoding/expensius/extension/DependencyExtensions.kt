/*
 * Copyright (C) 2016 Mantas Varnagiris.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */

package com.mvcoding.expensius.extension

import android.content.Context
import com.memoizrlabs.Scope
import com.memoizrlabs.Shank
import kotlin.reflect.KClass

fun <T : Any> provideNew(cls: KClass<T>) = Shank.provideNew(cls.java)

fun <T : Any> provideSingleton(cls: KClass<T>) = Shank.provideSingleton(cls.java)

fun <T : Any> provideActivityScopedSingleton(cls: KClass<T>,
        context: Context,
        arg1: Any? = null,
        arg2: Any? = null,
        arg3: Any? = null,
        arg4: Any? = null): T {
    return provideScopedSingleton(cls, context.toBaseActivity().scope, arg1, arg2, arg3, arg4)
}

fun <T : Any> provideScopedSingleton(
        cls: KClass<T>,
        scope: Scope,
        arg1: Any? = null,
        arg2: Any? = null,
        arg3: Any? = null,
        arg4: Any? = null): T {
    val shank = Shank.with(scope)
    return when {
        arg4 != null -> shank.provideSingleton(cls.java, arg1, arg2, arg3, arg4)
        arg3 != null -> shank.provideSingleton(cls.java, arg1, arg2, arg3)
        arg2 != null -> shank.provideSingleton(cls.java, arg1, arg2)
        arg1 != null -> shank.provideSingleton(cls.java, arg1)
        else -> shank.provideSingleton(cls.java)
    }
}