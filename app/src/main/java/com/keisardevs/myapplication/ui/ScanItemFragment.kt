package com.keisardevs.myapplication.ui

import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.keisardevs.myapplication.R
import com.keisardevs.myapplication.firebaseDAO.HatClothCallBack
import com.keisardevs.myapplication.firebaseDAO.PantsClothListCallBack
import com.keisardevs.myapplication.firebaseDAO.ShirtClothCallBack
import com.keisardevs.myapplication.model.HatClothingItem
import com.keisardevs.myapplication.model.PantsClothingItem
import com.keisardevs.myapplication.model.ShirtClothingItem
import com.keisardevs.myapplication.model.UserCloth
import kotlinx.android.synthetic.main.scan_item_fragment.*
import java.io.UnsupportedEncodingException
import java.util.logging.Handler
import java.util.logging.LogRecord


class ScanItemFragment : Fragment() {

    private lateinit var database: DatabaseReference
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var clothingItem: HatClothingItem

    private lateinit var hatClothItemIdToSaveInUserDatabase: String
    private var hatClothItemScoreValue : Int = 0

    private lateinit var shirtClothItemIdToSaveInUserDatabase: String
    private var shirtClothItemScoreValue : Int = 0

    private lateinit var pantsClothItemIdToSaveInUserDatabase: String
    private var pantsClothItemScoreValue : Int = 0

    lateinit var pantsImageView: ImageView

    lateinit var nfcAdapter: NfcAdapter


    companion object {
        fun newInstance() = ScanItemFragment()
    }

    private lateinit var viewModel: ScanItemViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.scan_item_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //database part:
        database = Firebase.database.reference

        firebaseAuth = FirebaseAuth.getInstance()

       var uid = firebaseAuth.currentUser?.uid.toString()

        //pantsImageView = view.findViewById<ImageView>(R.id.pants_imageView)


//        btn_cloth_pants.setOnClickListener {
//           // updateUserScore(50, uid)
//            setScore(getString(R.string.IncreaseScore), uid, hatClothItemScoreValue)
//            addClothToUser(uid)
//            //pants_imageView.setImageDrawable(R.drawable.gold_pants_stick_man)
//            Toast.makeText(
//                context,
//                "congrats you have just recivied 50 points!",
//                Toast.LENGTH_SHORT
//            ).show()
//
//
//        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ScanItemViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onStart() {
        super.onStart()

        getHatCloth(object : HatClothCallBack {
            override fun onCallback(value: HatClothingItem) {
                if (value != null) {
                    hatClothItemIdToSaveInUserDatabase = value.clothingItemID.toString()
                    println("hatClothItemIdToSaveInUserDatabase" + hatClothItemIdToSaveInUserDatabase)
                    hatClothItemScoreValue = value.scoreValue?.toInt()!!
                    val url = value.clothingItemImageUrl

                }

            }

        })

        getShirtCloth(object : ShirtClothCallBack {
            override fun onCallback(value: ShirtClothingItem) {
                if (value != null) {
                    val url = value.clothingItemImageUrl
                    //Picasso.get().load(url).into(shirt_imageView)

                }

            }

        })

        getPantsCloth(object : PantsClothListCallBack {
            override fun onCallback(value: PantsClothingItem) {
                if (value != null) {
                    val url = value.clothingItemImageUrl
                    //Picasso.get().load(url).into(pants_imageView)

                }

            }

        })
    }


    private fun updateUserScore(score: Int?, uid: String) {
        if (score != null) {
            database.child("users").child(uid).child("score").setValue(ServerValue.increment(score.toLong()))
        }
    }

    private fun readUserScore(){

    }

    //ActiveHatClothId
    private fun addClothToUser(useruid: String){
        database.child("users").child(useruid).child("Hats/$hatClothItemIdToSaveInUserDatabase").setValue(
            UserCloth(
                hatClothItemIdToSaveInUserDatabase,
                false
            )
        )

    }

    fun setScore(operation: String, useruid: String, clothScoreValue: Int) {
        val rootRef = FirebaseDatabase.getInstance().reference
        val scoreRef = rootRef.child("users").child(useruid).child("score")
        scoreRef.runTransaction(object : Handler(), Transaction.Handler {
            override fun doTransaction(mutableData: MutableData): Transaction.Result {
                val score =
                    mutableData.getValue(Int::class.java) ?: return Transaction.success(mutableData)
                if (operation == "increaseScore") {
                    mutableData.value = score + 50
                } else if (operation == "decreaseScore") {
                    mutableData.value = score - 1
                }
                return Transaction.success(mutableData)
            }

            override fun onComplete(
                databaseError: DatabaseError?,
                b: Boolean,
                dataSnapshot: DataSnapshot?
            ) {
            }

            override fun publish(p0: LogRecord?) {
                TODO("Not yet implemented")
            }

            override fun flush() {
                TODO("Not yet implemented")
            }

            override fun close() {
                TODO("Not yet implemented")
            }
        })
    }

    private fun getHatCloth(hatClothCallBack: HatClothCallBack){
        val rootRef = FirebaseDatabase.getInstance().reference
        val clothRef = rootRef.child("clothItem").child("hat")
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




    private fun createNdefMessage(content: String): NdefMessage? {
        val ndefRecord = NdefRecord.createTextRecord(content, "text")
        return NdefMessage(arrayOf(ndefRecord))
    }

    fun getTextFromNdefRecord(ndefRecord: NdefRecord): String? {
        var tagContent: String? = null
        try {
//            val payload = ndefRecord.payload
//            val textEncoding = if (payload[0] and 128 == 0) "UTF-8" else "UTF-16"
//            val languageSize: Int = payload[0] and 51
//            tagContent = String(
//                payload, languageSize + 1,
//                payload.size - languageSize - 1, textEncoding
//            )
        } catch (e: UnsupportedEncodingException) {
           // Log.e("getTextFromNdefRecord", e.getMessage(), e)
        }
        return tagContent
    }



//     fun onNewIntent(intent: Intent) {
//        if (intent.hasExtra(NfcAdapter.EXTRA_TAG)) {
//           // Toast.makeText(this, "NfcIntent!", Toast.LENGTH_SHORT).show()
//            if (tglReadWrite.isChecked()) {
//                val parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_ID)
//                if (parcelables != null && parcelables.size > 0) {
//                    readTextFromMessage(parcelables[0] as NdefMessage)
//                } else {
//                   // Toast.makeText(this, "No NDEF messages found!", Toast.LENGTH_SHORT).show()
//                }
//            } else {
//                val tag: Tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)!!
//                val ndefMessage = createNdefMessage(tglReadWrite.getText().toString() + "")
//                    // writeNdefMessage(tag, ndefMessage)
//            }
//        }
//    }



    private fun readTextFromMessage(ndefMessage: NdefMessage) {
        val ndefRecords = ndefMessage.records
        if (ndefRecords != null && ndefRecords.size > 0) {
            val ndefRecord = ndefRecords[0]
            val tagContent = getTextFromNdefRecord(ndefRecord)
            tglReadWrite.setText(tagContent)
        } else {
           // Toast.makeText(this, "No NDEF records found!", Toast.LENGTH_SHORT).show()
        }
    }



}
