package com.keisardevs.myapplication.firebaseDAO

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.keisardevs.myapplication.model.Store

class FirebaseStoresDAO {

    fun readStores(storeListener: StoreListener) {
        val stores: ArrayList<Store> = ArrayList<Store>()
        FirebaseDatabase.getInstance().getReference("stores")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    //children=> json of the messages
                    for (child in dataSnapshot.children) {
                        //each child is a single message json
                        child.getValue<Store>(Store::class.java)?.let { stores.add(it) }
                    }
                    storeListener.StoreArrived(stores)
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
    }

    public interface StoreListener{
        fun StoreArrived (stores: List<Store?>?)
    }


}


