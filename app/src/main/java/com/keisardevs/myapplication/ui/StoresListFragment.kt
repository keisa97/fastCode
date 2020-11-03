package com.keisardevs.myapplication.ui

import android.nfc.Tag
import android.os.Bundle
import android.util.Log.d
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.keisardevs.myapplication.R
import com.keisardevs.myapplication.firebaseDAO.StoreListCallBack
import com.keisardevs.myapplication.model.Store

/**
 * A fragment representing a list of Items.
 */
class StoresListFragment : Fragment() {


    private var columnCount = 1

    val storeList = ArrayList<Store>()




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_stores_list_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }

                readStoresFromDatabase(object : StoreListCallBack{
                    override fun onCallback(value: List<Store>) {
                        adapter = MyStoreRecyclerViewAdapter(value)
                    }
                })
            }
        }
        return view
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            StoresListFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }

    private fun readStoresFromDatabase(storeListCallBack : StoreListCallBack){

        val dbRef = FirebaseDatabase.getInstance().getReference("stores")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                storeList.clear()
                for (ds in dataSnapshot.children) {
                    val store: Store? = ds.getValue<Store>(Store::class.java)
                    store?.let { storeList.add(it) }
                }
                storeListCallBack.onCallback(storeList)

            }

            override fun onCancelled(databaseError: DatabaseError) {
                throw databaseError.toException()
            }
        }

        )
        
    }
}