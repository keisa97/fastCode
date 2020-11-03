package com.keisardevs.myapplication.firebaseDAO

import com.keisardevs.myapplication.model.User

interface UserDataCallBack {
    fun onCallback(value: User)

}