package com.keisardevs.myapplication.firebaseDAO

import com.keisardevs.myapplication.model.UserCloth

interface UserClothListCallBack {
    fun onCallback(value: ArrayList<UserCloth>)

}