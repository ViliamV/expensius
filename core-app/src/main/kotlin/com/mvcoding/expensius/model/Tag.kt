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

package com.mvcoding.expensius.model

import com.mvcoding.expensius.model.ModelState.NONE

data class Tag(
        override val id: String = "",
        override val modelState: ModelState = NONE,
        val title: String = "",
        val color: Int = 0,
        val order: Int = 0) : Model {

    fun withModelState(modelState: ModelState) = copy(modelState = modelState)
    fun withOrder(order: Int) = copy(order = order)
}