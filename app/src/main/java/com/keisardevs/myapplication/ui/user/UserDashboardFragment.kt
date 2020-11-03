package com.keisardevs.myapplication.ui.user

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.keisardevs.myapplication.R
import com.keisardevs.myapplication.ViewPagerAdapter
import com.keisardevs.myapplication.firebaseDAO.*
import com.keisardevs.myapplication.model.*
import com.keisardevs.myapplication.ui.user.tabs.ClothItemListFragment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.user_dashboard_fragment.*

class UserDashboardFragment : Fragment() {
    private var tabLayout: TabLayout? = null
    private var viewPager: ViewPager? = null
    private var viewPagerAdapter: ViewPagerAdapter? = null

    private lateinit var firebaseUser: FirebaseUser
    private lateinit var mAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private var databaseUser: User? = null


    private lateinit var accountDatabaseReference: DatabaseReference
    private lateinit var accountKeyCharacterMap: String

    private var accountListener: ValueEventListener? = null


    private var userName: String? = null
    private var accountName :String? = null

    lateinit var userNameTv: TextView
    lateinit var userScoreTv: TextView

     var imageUrl: String? = null
    lateinit var userClothID: String

    val userHatList = ArrayList<UserCloth>()


    lateinit var hatClothItemImageView: ImageView
    lateinit var shirtClothItemImageView: ImageView
    lateinit var pantsClothItemImageView: ImageView






    companion object {
        fun newInstance() = UserDashboardFragment()
    }

    private lateinit var viewModel: UserDashboardViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.user_dashboard_fragment, container, false)




    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        //FirebaseDAO.shared.saveMessage(new Message("text",null,uid));


        //for the tabs:
        tabLayout = view.findViewById(R.id.tabs_user_dashboard)
        viewPager = view.findViewById(R.id.viewpager_dashboard_user_cloths)
        viewPagerAdapter = ViewPagerAdapter(childFragmentManager)
        //add Fragment:
        //add Fragment:
        viewPagerAdapter!!.addFragment(ClothItemListFragment(), "My Hats")
//        viewPagerAdapter.addFragment(UserAcapellaContentFragment(), "My Acapella")
//        viewPagerAdapter.addFragment(UserBeatsContentFragment(), "My Beats")
//        //viewPagerAdapter.addFragment(new UserCollaborationsFragment(),"Collaborations");
//
//
        //viewPagerAdapter.addFragment(new UserCollaborationsFragment(),"Collaborations");
        viewPager!!.setAdapter(viewPagerAdapter)
        tabLayout!!.setupWithViewPager(viewPager)

        //database part:
        databaseReference = Firebase.database.reference

//        postKey = intent.getStringExtra(EXTRA_POST_KEY)
//            ?: throw IllegalArgumentException("Must pass EXTRA_POST_KEY")

        //accountDatabaseReference = Firebase.database.reference.child("users").child()


        //init:
        mAuth = FirebaseAuth.getInstance()
        //todo: if account null...
        //if ()
        firebaseUser = FirebaseAuth.getInstance().currentUser!!

        if (firebaseUser != null) {
            userName = firebaseUser!!.displayName
            println(userName)
        }

        userNameTv = user_tv_user_name
        userScoreTv = user_tv_correct_score_present




    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(UserDashboardViewModel::class.java)
        // TODO: Use the ViewModel

        val avatarHatImageView : ImageView = requireView().findViewById(R.id.hat_imageView)

        getUserHats(object : UserDataCallBack {
            override fun onCallback(value: User) {

                userClothID = value.activeHatClothId.toString()
                println("userClothID" + userClothID)




                getUserHatsObject(userClothID, object : HatClothCallBack {
                    override fun onCallback(value: HatClothingItem) {
                        imageUrl = value.clothingItemImageUrl!!
                        println("clothimageUrl" + imageUrl)

                        val clothExist: Boolean
                        if (imageUrl.isNullOrEmpty()) {
                            clothExist = false
                            println("clothExist" + clothExist)
                        } else {
                            clothExist = true
                            println("clothExist" + clothExist)

                        }

                        if (clothExist && avatarHatImageView != null) {
                            Picasso.get()
                                .load(imageUrl)
                                .placeholder(R.drawable.blue_hat_stick_man)
                                //.error(R.drawable.user_placeholder_error)
                                .into(avatarHatImageView);
                        }

                    }
                })
            }

        })
    }



    override fun onStart() {
        super.onStart()

        getUser(object : UserDataCallBack {
            override fun onCallback(user: User) {
                databaseUser = user
                userNameTv.text = databaseUser?.userName.toString()
                userScoreTv.text = databaseUser?.score.toString()

            }
        })




//        getHatCloth(object : HatClothCallBack{
//            override fun onCallback(value: HatClothingItem) {
//                if (value != null){
//                    val url = value.clothingItemImageUrl
//                    Picasso.get().load(url).into(hat_imageView)
//
//                }
//
//            }
//
//        })

        getShirtCloth(object : ShirtClothCallBack {
            override fun onCallback(value: ShirtClothingItem) {
                if (value != null) {
                    val url = value.clothingItemImageUrl
                    Picasso.get().load(url).into(shirt_imageView)

                }

            }

        })

        getPantsCloth(object : PantsClothListCallBack {
            override fun onCallback(value: PantsClothingItem) {
                if (value != null) {
                    val url = value.clothingItemImageUrl
                    Picasso.get().load(url).into(pants_imageView)

                }

            }

        })


    }




}

    private fun getScore(uid: String){

        val rootRef = FirebaseDatabase.getInstance().reference
        val scoreRef = rootRef.child("users").child(uid).child("score")
        val eventListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val score: Int? = dataSnapshot.getValue(Int::class.java)
                Log.d("TAG", score.toString() + "")
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        }
        scoreRef.addListenerForSingleValueEvent(eventListener)

    }

    private fun getUser(userListCallBack: UserDataCallBack){

        val firebaseUser = FirebaseAuth.getInstance().currentUser!!
        val rootRef = FirebaseDatabase.getInstance().reference
        val uid = firebaseUser.uid.toString()
        val userRef = rootRef.child("users").child(uid)
        val eventListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user: User = dataSnapshot.getValue<User>(User::class.java)!!
                userListCallBack.onCallback(user)
                Log.d("TAG", user.toString() + "")

            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        }

        userRef.addListenerForSingleValueEvent(eventListener)

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


