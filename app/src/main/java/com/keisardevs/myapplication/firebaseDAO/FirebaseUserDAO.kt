package com.keisardevs.myapplication.firebaseDAO

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.keisardevs.myapplication.model.User
import java.util.*

class FirebaseUserDAO {
//    fun saveUser(user: User) {
//        val newUserRef = FirebaseDatabase.getInstance()
//            .getReference(USERS).child(user.getName())
//        //.push();
//        user.setName(user.getName())
//        user.setUid(user.getUid())
//        user.setOnlineStatus(user.getOnlineStatus())
//        user.setUserProfileImageUrl(user.getUserProfileImageUrl())
//        user.setUserDetails(user.getUserDetails())
//        newUserRef.setValue(user)
//    }
//
//    fun saveDetailsOnly(user: User) {
//        val newUserRef = FirebaseDatabase.getInstance()
//            .getReference(USERS).child(user.getName())
//        // .push();
//        user.setName(user.getName())
//        //user.setUserDetails(user.getUserDetails());
//        user.setUserDetails("test")
//        newUserRef.setValue(user)
//    }
//
//    fun readUser(userListener: UserListener) {
//        val users: ArrayList<User> = ArrayList<User>()
//        FirebaseDatabase.getInstance().getReference(USERS)
//            .addListenerForSingleValueEvent(object : ValueEventListener {
//                override fun onDataChange(dataSnapshot: DataSnapshot) {
//                    //children=> json of the messages
//                    for (child in dataSnapshot.children) {
//                        //each child is a single message json
//                        users.add(child.getValue<User>(User::class.java))
//                    }
//                    userListener.UserArrived(users)
//                }
//
//                override fun onCancelled(databaseError: DatabaseError) {}
//            })
//    }
//
//    interface UserListener {
//        fun UserArrived(users: List<User>?)
//    }
//
//    companion object {
//        const val USERS = "Users"
//        var shared = FirebaseUserDAO()
//    }
}
