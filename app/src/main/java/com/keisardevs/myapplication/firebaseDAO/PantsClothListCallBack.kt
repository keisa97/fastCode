package com.keisardevs.myapplication.firebaseDAO

import com.keisardevs.myapplication.model.PantsClothingItem

interface PantsClothListCallBack {
    fun onCallback(value: PantsClothingItem)

}