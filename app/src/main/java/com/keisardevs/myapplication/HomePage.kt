package com.keisardevs.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.keisardevs.myapplication.model.User
import java.util.*


class HomePage : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private var userName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        //database part:
        database = Firebase.database.reference


        val getTheUserId = intent
        userName = getTheUserId.getStringExtra("userName")

        val uuid: UUID = UUID.randomUUID()
        val strUid = uuid.toString()

       // userName?.let { writeNewUser(strUid, userName!!, 0) }

    }

    private fun writeNewUser(userId: String, userName: String, accountName: String ,  score: Int?) {
        val user = User(userId, userName, accountName , score)
        database.child("users").child(userId).setValue(user)
    }
}