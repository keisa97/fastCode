package com.keisardevs.myapplication.ui.newui.map

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.keisardevs.myapplication.R
import com.keisardevs.myapplication.firebaseDAO.QuestCallBack
import com.keisardevs.myapplication.model.game.Quest

class GameSessionMapsFragment : Fragment() {

    private val callback = OnMapReadyCallback { googleMap ->

        val fox_a = LatLng(32.0753838, 34.766242)
        val fox_b = LatLng(32.0752779, 34.7745389)
        val fox_c = LatLng(32.0691568, 34.7833344)


        getQuestProgressStatus(object : QuestCallBack {
            override fun onCallback(value: Quest) {
                if (value != null) {
//                    zoom levels
//                    1: World
//                    5: Landmass/continent
//                    10: City
//                    15: Streets
//                    20: Buildings


                    if (!value.firstQuestItemScanned) {
                        googleMap.addMarker(MarkerOptions().position(fox_a).title("Fox Shop_a"))
                            //googleMap.moveCamera(CameraUpdateFactory.newLatLng(fox_a))
                        googleMap.animateCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                fox_a, 15.0f
                            )
                        )


                    } else if (!value.secondQuestItemScanned) {
                        googleMap.addMarker(MarkerOptions().position(fox_b).title("Fox Shop_b"))
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(fox_b, 15.0f))
                    } else if (!value.thirdQuestItemScanned) {
                        googleMap.addMarker(MarkerOptions().position(fox_c).title("Fox Shop_c"))
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(fox_c, 15.0f))
                    }

                }
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_game_session_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)


    }

    private fun getQuestProgressStatus(questCallBack: QuestCallBack){
        val firebaseUser = FirebaseAuth.getInstance().currentUser!!


        val rootRef = FirebaseDatabase.getInstance().reference
        val clothRef = rootRef.child("Quests").child(firebaseUser.uid.toString())
        val eventListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val quest: Quest = dataSnapshot.getValue<Quest>(
                    Quest::class.java
                )!!
                questCallBack.onCallback(quest)
                Log.d("TAG", quest.toString() + "")


            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        }

        clothRef.addListenerForSingleValueEvent(eventListener)
    }


}