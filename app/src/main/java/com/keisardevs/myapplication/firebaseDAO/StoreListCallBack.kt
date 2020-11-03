package com.keisardevs.myapplication.firebaseDAO

import com.keisardevs.myapplication.model.Store

interface StoreListCallBack {
    fun onCallback(value:List<Store>)

}