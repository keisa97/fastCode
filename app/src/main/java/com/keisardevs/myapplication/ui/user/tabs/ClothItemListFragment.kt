package com.keisardevs.myapplication.ui.user.tabs

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.keisardevs.myapplication.R
import com.keisardevs.myapplication.firebaseDAO.HatClothCallBack
import com.keisardevs.myapplication.firebaseDAO.HatClothListCallBack
import com.keisardevs.myapplication.firebaseDAO.UserClothListCallBack
import com.keisardevs.myapplication.model.HatClothingItem
import com.keisardevs.myapplication.model.Store
import com.keisardevs.myapplication.model.UserCloth

/**
 * A fragment representing a list of Items.
 */
class ClothItemListFragment : Fragment() {

    private var columnCount = 1

    val clothList = ArrayList<HatClothingItem>()


    lateinit var imageUrl: String
    lateinit var userClothID: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cloths_item_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }

                getUserHats(object : UserClothListCallBack {
                    override fun onCallback(value: ArrayList<UserCloth>) {
                        for (cloth in value) {
                            userClothID = cloth.activeHatClothId.toString()

                        }


                        getUserHatsObject(
                            userClothID,
                            object : HatClothListCallBack {
                                override fun onCallback(value: ArrayList<HatClothingItem>) {
                                    adapter = MyclothitemRecyclerViewAdapter(value)

                                }
                            })
                    }

                })


                //adapter = MyclothitemRecyclerViewAdapter(DummyContent.ITEMS)
            }
        }
        return view
    }


    private fun getUserHats(userClothListCallBack: UserClothListCallBack){
        val firebaseUser = FirebaseAuth.getInstance().currentUser!!
        val rootRef = FirebaseDatabase.getInstance().reference
        val uid = firebaseUser.uid.toString()
        val clothRef = rootRef.child("users").child(uid).child("Hats")
        val eventListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (ds in dataSnapshot.children) {
                    val userCloth: UserCloth? = ds.getValue<UserCloth>(UserCloth::class.java)
                    userCloth?.let {
                        val userHatList = ArrayList<UserCloth>()
                        userHatList.add(it)
                        userClothListCallBack.onCallback(userHatList)

                    }

                }

                val userCloth: UserCloth = dataSnapshot.getValue<UserCloth>(UserCloth::class.java)!!
                //userClothListCallBack.onCallback(userCloth)
                Log.d("TAG", userCloth.toString() + "")

            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        }

        clothRef.addListenerForSingleValueEvent(eventListener)
    }

    private fun getUserHatsObject(clothID: String, hatClothListCallBack: HatClothListCallBack){

        val rootRef = FirebaseDatabase.getInstance().reference
        println("clothID" + clothID)
        val clothRef = rootRef.child("clothItem").child("hat")
            //.child(clothID)
        val eventListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (ds in dataSnapshot.children){
                    val cloth : HatClothingItem? = ds.getValue<HatClothingItem>(HatClothingItem::class.java)
                    cloth?.let { clothList.add(it) }
                }

                hatClothListCallBack.onCallback(clothList)

            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        }

        clothRef.addListenerForSingleValueEvent(eventListener)
    }







    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            ClothItemListFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}