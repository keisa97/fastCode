package com.keisardevs.myapplication.firebaseDAO

import com.keisardevs.myapplication.model.HatClothingItem

interface HatClothListCallBack {
    fun onCallback(value: ArrayList<HatClothingItem>)

}