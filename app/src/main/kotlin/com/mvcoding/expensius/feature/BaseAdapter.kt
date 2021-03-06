/*
 * Copyright (C) 2015 Mantas Varnagiris.
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

package com.mvcoding.expensius.feature

import android.support.v7.widget.RecyclerView
import java.util.Collections.swap

abstract class BaseAdapter<T, VH : RecyclerView.ViewHolder>() : RecyclerView.Adapter<VH>() {
    private val items = arrayListOf<T>()

    override fun getItemCount(): Int {
        return items.size
    }

    fun setItems(items: List<T>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun getItem(position: Int): T {
        return items[position]
    }

    fun insert(item: T, position: Int) {
        items.add(position, item)
        notifyItemInserted(position)
    }

    fun insert(items: List<T>, position: Int) {
        this.items.addAll(position, items)
        notifyItemRangeInserted(position, items.size)
    }

    fun remove(item: T) {
        val removedItemPosition = items.indexOf(item)
        items.remove(item)
        notifyItemRemoved(removedItemPosition)
    }

    fun move(fromPosition: Int, toPosition: Int) {
        swap(items, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }
}