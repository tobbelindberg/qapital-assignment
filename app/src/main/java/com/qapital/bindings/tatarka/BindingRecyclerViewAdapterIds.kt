package com.qapital.bindings.tatarka

import com.qapital.base.vm.ItemViewModel
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter
import org.apache.commons.lang3.builder.HashCodeBuilder

object BindingRecyclerViewAdapterIds : BindingRecyclerViewAdapter.ItemIds<ItemViewModel> {

    override fun getItemId(position: Int, item: ItemViewModel): Long {
        return HashCodeBuilder(13, 37)
            .append(item.javaClass.name)
            .append(item.itemId())
            .toHashCode().toLong()
    }
}