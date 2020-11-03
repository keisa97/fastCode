package com.keisardevs.myapplication.ui.newui

import android.app.PendingIntent
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.keisardevs.myapplication.R
import com.keisardevs.myapplication.firebaseDAO.*
import com.keisardevs.myapplication.model.HatClothingItem
import com.keisardevs.myapplication.model.PantsClothingItem
import com.keisardevs.myapplication.model.ShirtClothingItem
import com.keisardevs.myapplication.model.User
import com.keisardevs.myapplication.model.game.Quest
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_user_status.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UserStatusFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserStatusFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var userClothID: String
    var imageUrl: String? = null
    lateinit var nfcTagId : String

    lateinit var tvNextItem : TextView


    lateinit var progressBar : ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            println(param1)
//            tv_status.text = param1.toString()
//            param2 = it.getString(ARG_PARAM2)
//        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            println(param1)
            tv_user_status_next_item.text = param1.toString()
            param2 = it.getString(ARG_PARAM2)
        }

        tvNextItem = view.findViewById<TextView>(R.id.tv_user_status_next_item)

        progressBar = view.findViewById(R.id.progressBar_quest_status)
        val characterImageView : ImageView = requireView().findViewById(R.id.imageView_character)


//        activity?.intent

//        mNfcAdapter = NfcAdapter.getDefaultAdapter(activity)
//
//        mPendingIntent = PendingIntent.getActivity(
//            activity, 0,
//            Intent(activity, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0
//        )


//        mNdefPushMessage = NdefMessage(
//            arrayOf(
////                newTextRecord(
////                    "Message from NFC Reader :-)", Locale.ENGLISH, true
////                )
//            )
//        )

        //getNfcTagID()

       // val avatarHatImageView : ImageView = requireView().findViewById(R.id.hat_imageView)
//
//        getUserHats(object : UserDataCallBack {
//            override fun onCallback(value: User) {
//
//                userClothID = value.activeHatClothId.toString()
//                println("userClothID" + userClothID)
//
//
//
//
//                getUserHatsObject(
//                    userClothID,
//                    object : HatClothCallBack {
//                        override fun onCallback(value: HatClothingItem) {
//                            imageUrl = value.clothingItemImageUrl!!
//                            println("clothimageUrl" + imageUrl)
//
//                            val clothExist: Boolean
//                            if (imageUrl.isNullOrEmpty()) {
//                                clothExist = false
//                                println("clothExist" + clothExist)
//                            } else {
//                                clothExist = true
//                                println("clothExist" + clothExist)
//
//                            }
//
//                            if (clothExist && avatarHatImageView != null) {
//                                Picasso.get()
//                                    .load(imageUrl)
//                                    .placeholder(R.drawable.blue_hat_stick_man)
//                                    //.error(R.drawable.user_placeholder_error)
//                                    .into(avatarHatImageView);
//                            }
//
//                        }
//                    })
//            }
//
//        })
//
//        getShirtCloth(object : ShirtClothCallBack {
//            override fun onCallback(value: ShirtClothingItem) {
//                if (value != null) {
//                    val url = value.clothingItemImageUrl
//                    Picasso.get().load(url).into(shirt_imageView)
//
//                }
//
//            }
//
//        })
//
//        getPantsCloth(object : PantsClothListCallBack {
//            override fun onCallback(value: PantsClothingItem) {
//                if (value != null) {
//                    val url = value.clothingItemImageUrl
//                    Picasso.get().load(url).into(pants_imageView)
//
//                }
//
//            }
//
//        })


        getQuestProgressStatus(object : QuestCallBack {
            override fun onCallback(value: Quest) {
                if (value != null) {
                    progressBar.progress = value.questProgressStatus

                    if (!value.firstQuestItemScanned){
                        tvNextItem.text = "next item: Pants"
                        characterImageView.setImageResource(R.drawable.base_character)
                    }else if (!value.secondQuestItemScanned){
                        tvNextItem.text = "next item: Shirt"
                        characterImageView.setImageResource(R.drawable.character_pants)
                    }else if(!value.thirdQuestItemScanned){
                        tvNextItem.text = "next item: Hat"
                        characterImageView.setImageResource(R.drawable.character_pant_shirt)
                    }else{
                        tvNextItem.text = "congrats, you finish the quest!"
                       characterImageView.setImageResource(R.drawable.character_fully_dressed)
                    }
//
//                    if(value.questProgressStatus == 33){
//                        tv_user_status_next_item.text = "next item: Shirt"
//                        characterImageView.setImageResource(R.drawable.character_pants)
//                    }
//                    else if (value.questProgressStatus == 66){
//                        tv_user_status_next_item.text = "next item: Hat"
//                        characterImageView.setImageResource(R.drawable.character_pant_shirt)
//                    }
//                    else if (value.questProgressStatus > 66){
//                        tv_user_status_next_item.text = "congrats, you finish the quest!"
//                        characterImageView.setImageResource(R.drawable.character_fully_dressed)
//
//                    }
                }
            }
        })




    }

//    override fun onResume() {
//        super.onResume()
//
//        if (mNfcAdapter != null) {
//            if (!mNfcAdapter!!.isEnabled()) {
//                //showMessage(R.string.error, R.string.nfc_disabled);
//            }
//            mNfcAdapter!!.enableForegroundDispatch(getActivity(), mPendingIntent, null, null);
//            mNfcAdapter!!.enableForegroundNdefPush(getActivity(), mNdefPushMessage);
//        }
//
//        //enableForegroundDispatch(this, pendingIntent, writeTagFilters, null)
//
//    }
//
//    override fun onPause() {
//        super.onPause()
//
//        if (mNfcAdapter != null) {
//            mNfcAdapter!!.disableForegroundDispatch(getActivity());
//            mNfcAdapter!!.disableForegroundNdefPush(getActivity());
//        }
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_status, container, false)
    }



    private fun getShirtCloth(shirtClothCallBack: ShirtClothCallBack){
        val rootRef = FirebaseDatabase.getInstance().reference
        val clothRef = rootRef.child("clothItem").child("shirt")
        val eventListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val shirt: ShirtClothingItem = dataSnapshot.getValue<ShirtClothingItem>(
                    ShirtClothingItem::class.java
                )!!
                shirtClothCallBack.onCallback(shirt)
                Log.d("TAG", shirt.toString() + "")

            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        }

        clothRef.addListenerForSingleValueEvent(eventListener)
    }

    private fun getPantsCloth(pantsClothListCallBack: PantsClothListCallBack){
        val rootRef = FirebaseDatabase.getInstance().reference
        val clothRef = rootRef.child("clothItem").child("pants")
        val eventListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val pants: PantsClothingItem = dataSnapshot.getValue<PantsClothingItem>(
                    PantsClothingItem::class.java
                )!!
                pantsClothListCallBack.onCallback(pants)
                Log.d("TAG", pants.toString() + "")

            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        }

        clothRef.addListenerForSingleValueEvent(eventListener)
    }


    private fun getUserHats(userHat: UserDataCallBack){
        val firebaseUser = FirebaseAuth.getInstance().currentUser!!
        val rootRef = FirebaseDatabase.getInstance().reference
        val uid = firebaseUser.uid.toString()
        val clothRef = rootRef.child("users").child(uid)
        //.child("activeHatClothId")
        val eventListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {


                val userCloth: User = dataSnapshot.getValue<User>(User::class.java)!!
                userHat.onCallback(userCloth)
                Log.d("TAG", userCloth.toString() + "")

            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        }

        clothRef.addListenerForSingleValueEvent(eventListener)
    }

    private fun getUserHatsObject(clothID: String, hatClothCallBack: HatClothCallBack){

        val rootRef = FirebaseDatabase.getInstance().reference
        println("clothID" + clothID)
        val clothRef = rootRef.child("clothItem").child("hat").child(clothID)
        val eventListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val hatClothingItem: HatClothingItem = dataSnapshot.getValue<HatClothingItem>(
                    HatClothingItem::class.java
                )!!
                hatClothCallBack.onCallback(hatClothingItem)
                Log.d("TAG", hatClothingItem.toString() + "")


            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        }

        clothRef.addListenerForSingleValueEvent(eventListener)
    }

    private fun getQuestProgressStatus( questCallBack: QuestCallBack){
        val firebaseUser = FirebaseAuth.getInstance().currentUser!!


        val rootRef = FirebaseDatabase.getInstance().reference
        val clothRef = rootRef.child("Quests").child(firebaseUser.uid.toString())
        val eventListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val quest: Quest = dataSnapshot.getValue<Quest>(
                    Quest::class.java
                )!!
                questCallBack.onCallback(quest)
                Log.d("TAG", quest.toString() + "")


            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        }

        clothRef.addListenerForSingleValueEvent(eventListener)
    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment UserStatusFragment.
         */
        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            UserStatusFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
    }
}