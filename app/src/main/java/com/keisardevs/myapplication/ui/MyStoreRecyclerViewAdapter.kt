package com.keisardevs.myapplication.ui

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.keisardevs.myapplication.R
import com.keisardevs.myapplication.model.Store

import com.keisardevs.myapplication.ui.dummy.DummyContent.DummyItem

/**
 * [RecyclerView.Adapter] that can display a [DummyItem].
 * TODO: Replace the implementation with code for your data type.
 */
class MyStoreRecyclerViewAdapter(
    private val storesList: List<Store>
) : RecyclerView.Adapter<MyStoreRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_stores_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = storesList[position]
        holder.storeId.text = item.storeId
        holder.storeName.text = item.storeName
        holder.storeImage.text = item.storeImageUrl

    }

    override fun getItemCount(): Int = storesList.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val storeId: TextView = view.findViewById(R.id.store_id)
        val storeName: TextView = view.findViewById(R.id.store_name)
        val storeImage: TextView = view.findViewById(R.id.store_image)


        override fun toString(): String {
            return "ViewHolder(storeId=$storeId, storeName=$storeName, storeImage=$storeImage)"
        }


    }
}