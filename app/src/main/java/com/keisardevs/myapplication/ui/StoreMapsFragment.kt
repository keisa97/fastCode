package com.keisardevs.myapplication.ui

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.keisardevs.myapplication.R
import com.keisardevs.myapplication.firebaseDAO.StoreListCallBack
import com.keisardevs.myapplication.model.Store

class StoreMapsFragment : Fragment() {

    val storeList = ArrayList<Store>()

    val markerList = ArrayList<Marker>()

    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */

//        val dbRef = FirebaseDatabase.getInstance().getReference("stores")
//        dbRef.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                storeList.clear()
//                for (ds in dataSnapshot.children) {
//                    val store: Store? = ds.getValue<Store>(Store::class.java)
//                        if (store != null){
//                            val marker = LatLng(store?.storeLocationLatitude?.toDouble(),
//                                store?.storeLocationLongitude?.toDouble())
//                            googleMap.addMarker(MarkerOptions().position(marker).title(store.storeName))
//                                              }
//
//                    store?.let { storeList.add(it) }
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
//            }
//        }
//
//        )

        getMarksFromDatabase(object : StoreListCallBack {
            override fun onCallback(value: List<Store>) {
                for (store in value){
                    val marker = LatLng(store?.storeLocationLatitude?.toDouble(),
                        store?.storeLocationLongitude?.toDouble())
                    googleMap.addMarker(MarkerOptions().position(marker).title(store.storeName))
                }
            }
        })

        val sydney = LatLng(-34.0, 151.0)
       googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_store_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)


    }

    fun getMarksFromDatabase(storeListCallBack: StoreListCallBack){

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

