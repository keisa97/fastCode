package com.keisardevs.myapplication.firebaseDAO

import com.keisardevs.myapplication.model.HatClothingItem

interface HatClothCallBack {
    fun onCallback(value: HatClothingItem)

}