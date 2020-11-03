package com.keisardevs.myapplication.firebaseDAO

import com.keisardevs.myapplication.model.ShirtClothingItem

interface ShirtClothCallBack {
    fun onCallback(value: ShirtClothingItem)

}