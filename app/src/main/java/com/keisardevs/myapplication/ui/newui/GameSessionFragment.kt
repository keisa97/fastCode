package com.keisardevs.myapplication.ui.newui

import android.app.PendingIntent
import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.keisardevs.myapplication.R
import com.keisardevs.myapplication.ViewPagerAdapter
import com.keisardevs.myapplication.firebaseDAO.HatClothCallBack
import com.keisardevs.myapplication.firebaseDAO.PantsClothListCallBack
import com.keisardevs.myapplication.firebaseDAO.QuestCallBack
import com.keisardevs.myapplication.firebaseDAO.ShirtClothCallBack
import com.keisardevs.myapplication.model.HatClothingItem
import com.keisardevs.myapplication.model.PantsClothingItem
import com.keisardevs.myapplication.model.ShirtClothingItem
import com.keisardevs.myapplication.model.game.Quest
import com.keisardevs.myapplication.ui.newui.map.GameSessionMapsFragment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_game_session.*
import java.util.logging.Handler
import java.util.logging.LogRecord


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GameSessionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GameSessionFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    //for map viewpager
    private var tabLayout: TabLayout? = null
    private var viewPager: ViewPager? = null
    private var viewPagerAdapter: ViewPagerAdapter? = null

    lateinit var nfcTagId : String
    var actualQuest : Quest = Quest(false, false, false, 0)


    lateinit var tvDestination : TextView
    lateinit var imageViewScannedCloth : ImageView

    private var nfcScanned : Boolean = false


    private var mNfcAdapter: NfcAdapter? = null
    private var mPendingIntent: PendingIntent? = null
    private var mNdefPushMessage: NdefMessage? = null


    var mAuth: FirebaseAuth? = null
    lateinit var firebaseUser: FirebaseUser
    private lateinit var databaseReference: DatabaseReference

    lateinit var mapView: MapView
    var map: GoogleMap? = null



    val callback = OnMapReadyCallback { googleMap ->


        val fox_a = LatLng(32.0639363,34.7564644)
        val fox_b = LatLng(32.0752779,34.7745389)
        val fox_c = LatLng(32.0691568,34.7833344)




        val sydney = LatLng(-34.0, 151.0)
        googleMap.addMarker(MarkerOptions().position(fox_a).title("Fox Shop"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(fox_a))
        if (!actualQuest.firstQuestItemScanned){
            googleMap.addMarker(MarkerOptions().position(fox_a).title("Fox Shop"))
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(fox_a))
        }

        if (actualQuest.firstQuestItemScanned){

            googleMap.addMarker(MarkerOptions().position(fox_b).title("Fox Shop"))
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(fox_b))
        }

        if (actualQuest.secondQuestItemScanned){

            googleMap.addMarker(MarkerOptions().position(fox_c).title("Fox Shop"))
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(fox_c))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }



//        mapView = view?.findViewById(R.id.mapView) as MapView
//        mapView!!.onCreate(savedInstanceState)
//
//        // Gets to GoogleMap from the MapView and does initialization stuff
//        //map = mapView.getMap();
//        map!!.getUiSettings().setMyLocationButtonEnabled(false);
//        if (activity?.let {
//                ActivityCompat.checkSelfPermission(
//                    it,
//                    Manifest.permission.ACCESS_FINE_LOCATION
//                )
//            } != PackageManager.PERMISSION_GRANTED && activity?.let {
//                ActivityCompat.checkSelfPermission(
//                    it,
//                    Manifest.permission.ACCESS_COARSE_LOCATION
//                )
//            } != PackageManager.PERMISSION_GRANTED
//        ) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return
//        }
//        map!!.setMyLocationEnabled(true);
//
//        // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
//        // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
//        try {
//            MapsInitializer.initialize(this.activity)
//        } catch (e: GooglePlayServicesNotAvailableException) {
//            e.printStackTrace()
//        }


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewPager = view.findViewById(R.id.game_session_viewpager_mapView)
        viewPagerAdapter = ViewPagerAdapter(childFragmentManager)
        //add Fragment:
        //add Fragment:
        viewPagerAdapter!!.addFragment(GameSessionMapsFragment(), "Map")

        viewPager!!.setAdapter(viewPagerAdapter)
        //tabLayout!!.setupWithViewPager(viewPager)



        mAuth = FirebaseAuth.getInstance()
        firebaseUser = mAuth!!.currentUser!!
        databaseReference = FirebaseDatabase.getInstance().reference


        tvDestination = view.findViewById<TextView>(R.id.tv_destination)
        imageViewScannedCloth = view.findViewById<ImageView>(R.id.imageView_scanned_cloth)
//        btn_quest_part_b.setOnClickListener { questPartFinish("second", firebaseUser.uid) }
//        btn_quest_part_c.setOnClickListener { questPartFinish("third", firebaseUser.uid) }







        questCallBackAction()


//        if (actualQuest.firstQuestItemScanned == false) {
//            println("actualQuest.firstQuestItemScanned:" + actualQuest.firstQuestItemScanned)
//            val fox_a_address = "Azrieli Center, דרך מנחם בגין 132, תל אביב יפו"
//
//            tv_location.text = fox_a_address
//            imageView_scanned_cloth.setImageResource(R.drawable.red_pants)
//        }
//        val fox_a_address = "Azrieli Center, דרך מנחם בגין 132, תל אביב יפו"
//        val fox_b_address = "החשמונאים 96, תל אביב יפו"
//        val fox_c_address = "דיזנגוף 53, תל אביב יפו"
//        if(!actualQuest.firstQuestItemScanned){
//            println("actualQuest.firstQuestItemScanned:" +actualQuest.firstQuestItemScanned)
//            tv_location.text = fox_a_address
//        }else if (actualQuest.firstQuestItemScanned){
//            tv_location.text = fox_b_address
//        }else tv_location.text = fox_c_address
        scanForNfcTagId()
//        if (!actualQuest.firstQuestItemScanned)
//            scanForNfcTagId()
//
//        if (actualQuest.firstQuestItemScanned){
//            scanForNfcTagIdPants()
//            println("scan for pants")
//
//        }
//
//        if (actualQuest.secondQuestItemScanned){
//            println("scan for pants")
//            scanForNfcTagIdShirt()
//
//        }










    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game_session, container, false)


    }




    private fun scanForNfcTagId(){
        val options = Bundle()
        // READER_PRESENCE_CHECK_DELAY is a work around for a Bug in some NFC implementations.
        options.putInt(NfcAdapter.EXTRA_READER_PRESENCE_CHECK_DELAY, 1);

        mNfcAdapter = NfcAdapter.getDefaultAdapter(activity)

        val flags = NfcAdapter.FLAG_READER_NFC_A

        mNfcAdapter!!.enableReaderMode(activity, NfcAdapter.ReaderCallback { tag ->
            activity?.runOnUiThread {
                Log.d("WTF", "Tag discovered")
                nfcTagId = (tag.id).toHexString()
                Toast.makeText(
                    activity,
                    "tag detected",
                    Toast.LENGTH_SHORT
                ).show()
                textView_currect_scanned.text = nfcTagId


                if (!actualQuest.firstQuestItemScanned){
                    hatCallBackAction()

                }else if (!actualQuest.secondQuestItemScanned){
                    println("scan for pants")
                    pantsCallBackAction()

                }else if (!actualQuest.thirdQuestItemScanned){
                    println("scan for shirt")
                    shirtCallBackAction()
                }else {
                    Toast.makeText(activity, "Quest is Finish! no more to search here", Toast.LENGTH_LONG).show()
                }




//                while (!actualQuest.firstQuestItemScanned)
//                {
//                    if (actualQuest.firstQuestItemScanned)
//                        break
//                }
//
//                while (!actualQuest.secondQuestItemScanned)
//                {
//                    nfcTagId = "2"
//                    pantsCallBackAction()
//                    if (actualQuest.secondQuestItemScanned)
//                        break
//                }
//
//                while (!actualQuest.thirdQuestItemScanned)
//                {
//                    nfcTagId = "3"
//
//                    shirtCallBackAction()
//                    if (actualQuest.thirdQuestItemScanned)
//                        break
//                }



            }
        }, flags, null)
    }

    private fun scanForNfcTagIdPants(){
        val options = Bundle()
        // READER_PRESENCE_CHECK_DELAY is a work around for a Bug in some NFC implementations.
        options.putInt(NfcAdapter.EXTRA_READER_PRESENCE_CHECK_DELAY, 1);

        mNfcAdapter = NfcAdapter.getDefaultAdapter(activity)

        val flags = NfcAdapter.FLAG_READER_NFC_A

        mNfcAdapter!!.enableReaderMode(activity, NfcAdapter.ReaderCallback { tag ->
            activity?.runOnUiThread {
                Log.d("WTF", "Tag discovered")
                nfcTagId = (tag.id).toHexString()
                Toast.makeText(
                    activity,
                    "tag detected",
                    Toast.LENGTH_SHORT
                ).show()
                nfcTagId = "2"
                textView_currect_scanned.text = nfcTagId

                pantsCallBackAction()

            }
        }, flags, null)
    }

    private fun scanForNfcTagIdShirt(){
        val options = Bundle()
        // READER_PRESENCE_CHECK_DELAY is a work around for a Bug in some NFC implementations.
        options.putInt(NfcAdapter.EXTRA_READER_PRESENCE_CHECK_DELAY, 1);

        mNfcAdapter = NfcAdapter.getDefaultAdapter(activity)

        val flags = NfcAdapter.FLAG_READER_NFC_A

        mNfcAdapter!!.enableReaderMode(activity, NfcAdapter.ReaderCallback { tag ->
            activity?.runOnUiThread {
                Log.d("WTF", "Tag discovered")
                nfcTagId = (tag.id).toHexString()
                Toast.makeText(
                    activity,
                    "tag detected",
                    Toast.LENGTH_SHORT
                ).show()
                nfcTagId = "3"
                textView_currect_scanned.text = nfcTagId

                shirtCallBackAction()

            }
        }, flags, null)
    }


    private fun getHatObject(tagID: String, hatClothCallBack: HatClothCallBack){
        println("tagID:" + tagID)

        val rootRef = FirebaseDatabase.getInstance().reference
        val clothRef = rootRef.child("clothItem").child("hat")
        //.orderByChild("itemTagID").equalTo(tagID)
        //.child(clothID)
        val query: Query = clothRef.orderByChild("itemTagID").equalTo(tagID).limitToFirst(1)

        val eventListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (ds in dataSnapshot.children){
                    val cloth : HatClothingItem? = ds.getValue<HatClothingItem>(HatClothingItem::class.java)
                    //cloth?.let { clothList?.add(it) }
                    if (cloth != null) {
                        hatClothCallBack.onCallback(cloth)
                    }
                }

                val cloth: HatClothingItem = dataSnapshot.getValue<HatClothingItem>(HatClothingItem::class.java)!!
                println("cloth i func" + cloth)

            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        }

        query.addListenerForSingleValueEvent(eventListener)
    }

    private fun hatCallBackAction(){
        getHatObject(nfcTagId, object : HatClothCallBack {
            override fun onCallback(value: HatClothingItem) {
                val imageUrl = value.clothingItemImageUrl!!
                println("clothimageUrl" + imageUrl)
                questPartFinish("first", firebaseUser.uid)
                actualQuest.firstQuestItemScanned = true

//                if (imageUrl != null) {
//                    Picasso.get()
//                        .load(imageUrl)
//                        .placeholder(R.drawable.mystery_box_transpert)
//                        //.error(R.drawable.user_placeholder_error)
//                        .into(imageView_scanned_cloth);
//                }
                setClothItemToSearchAndDesinationToGo()

            }
        })
    }




    private fun getPantsObject(tagID: String, pantsClothCallBack: PantsClothListCallBack){
        println("tagID:" + tagID)

        val rootRef = FirebaseDatabase.getInstance().reference
        val clothRef = rootRef.child("clothItem")
            //.child("pants")
        //.orderByChild("itemTagID").equalTo(tagID)
        //.child(clothID)
        val query: Query = clothRef.orderByChild("itemTagID").equalTo(tagID).limitToFirst(1)

        val eventListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (ds in dataSnapshot.children){
                    println("looking for pants")
                    val cloth : PantsClothingItem? = ds.getValue<PantsClothingItem>(
                        PantsClothingItem::class.java
                    )
                    //cloth?.let { clothList?.add(it) }
                    if (cloth != null) {
                        println("pants cloth : "+cloth)
                        pantsClothCallBack.onCallback(cloth)
                    }
                }

//                val cloth: PantsClothingItem = dataSnapshot.getValue<PantsClothingItem>(
//                    PantsClothingItem::class.java
//                )!!
//                println("cloth i func" + cloth)

            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        }

        query.addListenerForSingleValueEvent(eventListener)
    }

    private fun pantsCallBackAction(){
        nfcTagId = "2"
        getPantsObject(nfcTagId, object : PantsClothListCallBack {
            override fun onCallback(value: PantsClothingItem) {
                val imageUrl = value.clothingItemImageUrl!!
                println("clothimageUrl" + imageUrl)
                questPartFinish("second", firebaseUser.uid)
                actualQuest.secondQuestItemScanned = true


//                if (imageUrl != null) {
//                    Picasso.get()
//                        .load(imageUrl)
//                        .placeholder(R.drawable.mystery_box_transpert)
//                        //.error(R.drawable.user_placeholder_error)
//                        .into(imageView_scanned_cloth);
//                }
                setClothItemToSearchAndDesinationToGo()
            }
        })
    }

    private fun getShirtObject(tagID: String, ShirtClothCallBack: ShirtClothCallBack){
        println("tagID:" + tagID)

        val rootRef = FirebaseDatabase.getInstance().reference
        val clothRef = rootRef.child("clothItem")
            //.child("shirt")
        //.orderByChild("itemTagID").equalTo(tagID)
        //.child(clothID)
        val query: Query = clothRef.orderByChild("itemTagID").equalTo(tagID).limitToFirst(1)

        val eventListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (ds in dataSnapshot.children){
                    val cloth : ShirtClothingItem? = ds.getValue<ShirtClothingItem>(
                        ShirtClothingItem::class.java
                    )
                    //cloth?.let { clothList?.add(it) }
                    if (cloth != null) {
                        ShirtClothCallBack.onCallback(cloth)
                    }
                }

                val cloth: ShirtClothingItem = dataSnapshot.getValue<ShirtClothingItem>(
                    ShirtClothingItem::class.java
                )!!
                println("cloth i func" + cloth)

            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        }

        query.addListenerForSingleValueEvent(eventListener)
    }

    private fun shirtCallBackAction(){
        nfcTagId = "3"

        getShirtObject(nfcTagId, object : ShirtClothCallBack {
            override fun onCallback(value: ShirtClothingItem) {
                val imageUrl = value.clothingItemImageUrl!!
                println("clothimageUrl" + imageUrl)
                questPartFinish("third", firebaseUser.uid)
                actualQuest.thirdQuestItemScanned = true


//                if (imageUrl != null) {
//                    Picasso.get()
//                        .load(imageUrl)
//                        .placeholder(R.drawable.mystery_box_transpert)
//                        //.error(R.drawable.user_placeholder_error)
//                        .into(imageView_scanned_cloth);
//                }

                setClothItemToSearchAndDesinationToGo()

            }
        })
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


    private fun questCallBackAction(){

        getQuestProgressStatus(object : QuestCallBack {
            override fun onCallback(value: Quest) {
                actualQuest = value
                println("actual quest: " + value)
                setClothItemToSearchAndDesinationToGo()
            }
        })
    }

    private fun setClothItemToSearchAndDesinationToGo(){
        val fox_a_address = "Azrieli Center, דרך מנחם בגין 132, תל אביב יפו"
        val fox_b_address = "החשמונאים 96, תל אביב יפו"
        val fox_c_address = "דיזנגוף 53, תל אביב יפו"
        val questInProcces : Boolean
        questInProcces = true
        when(questInProcces){

        }
        if(!actualQuest.firstQuestItemScanned){
            println("actualQuest.firstQuestItemScanned:" +actualQuest.firstQuestItemScanned)
            tvDestination.text = fox_a_address
            imageViewScannedCloth.setImageResource(R.drawable.red_pants)

        }else if (!actualQuest.secondQuestItemScanned){
            println("actualQuest.secondQuestItemScanned:" +actualQuest.secondQuestItemScanned)

            tvDestination.text = fox_b_address
            imageViewScannedCloth.setImageResource(R.drawable.blue_shirt)


        }else if (!actualQuest.thirdQuestItemScanned) {
            tvDestination.text = fox_c_address
            imageViewScannedCloth.setImageResource(R.drawable.red_hat)


        }else{
            imageViewScannedCloth.setImageResource(R.drawable.character_fully_dressed)

            tvDestination.text = "\"congrats, you finish the quest!\""

        }
    }



    private fun getNfcTagID(){
        val tagId : ByteArray? = activity?.intent?.getByteArrayExtra(NfcAdapter.EXTRA_ID)
        if (tagId != null){
            nfcTagId = tagId?.toHexString().toString()
            textView_currect_scanned.text = nfcTagId


        }
    }

    fun ByteArray.toHexString() = joinToString("") { "%02x".format(it) }


    fun questPartFinish(questPart: String, useruid: String) {


        databaseReference.child("Quests").child(useruid)
            .child("$questPart" + "QuestItemScanned").setValue(true)

        updateProgressStatus(getString(R.string.IncreaseScore), useruid)

    }

    fun updateProgressStatus(operation: String, useruid: String) {
        val rootRef = FirebaseDatabase.getInstance().reference
        val scoreRef = rootRef.child("Quests").child(useruid).child("questProgressStatus")
        scoreRef.runTransaction(object : Handler(), Transaction.Handler {
            override fun doTransaction(mutableData: MutableData): Transaction.Result {
                val score =
                    mutableData.getValue(Int::class.java) ?: return Transaction.success(mutableData)
                if (operation == "increaseScore") {
                    mutableData.value = score + 33
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

    fun navigationTo(){

    }

    private fun resloveIntent(intent: Intent, foregroundDispatch: Boolean){
        val action : String = intent.action.toString()

        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
            ||NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
            || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)){

            val tag : Tag? = intent.getParcelableExtra(NfcAdapter.EXTRA_ID)


            println(tag.toString())
        }

    }



//    override fun onPause() {
//        super.onPause()
//        mNfcAdapter?.disableReaderMode(activity);
//    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment GameSessionFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GameSessionFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

fun foo() = println("some foo's")