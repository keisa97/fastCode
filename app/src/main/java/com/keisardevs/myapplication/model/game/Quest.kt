package com.keisardevs.myapplication.model.game

data class Quest (
    var firstQuestItemScanned : Boolean = false,
    var secondQuestItemScanned : Boolean = false,
    var thirdQuestItemScanned : Boolean = false
    , val questProgressStatus : Int = 0
        ){}