package com.keisardevs.myapplication.firebaseDAO

import com.keisardevs.myapplication.model.HatClothingItem
import com.keisardevs.myapplication.model.game.Quest

interface QuestCallBack {
    fun onCallback(value: Quest)

}