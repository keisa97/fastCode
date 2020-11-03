package com.keisardevs.myapplication.nfc

import android.content.Intent
import android.graphics.drawable.Drawable
import android.nfc.NfcAdapter
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.keisardevs.myapplication.ui.newui.Host
import com.keisardevs.myapplication.MainActivity
import com.keisardevs.myapplication.R
import com.keisardevs.myapplication.firebaseDAO.HatClothCallBack
import com.keisardevs.myapplication.model.HatClothingItem
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_nfc.*


class NfcActivity : AppCompatActivity() {

    lateinit var mAuth: FirebaseAuth
    lateinit var firebaseUser: FirebaseUser
    lateinit var nfcTagId : String

    var clothList : ArrayList<HatClothingItem?>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nfc)



            mAuth = FirebaseAuth.getInstance()
            this.firebaseUser = mAuth!!.currentUser!!
            println(mAuth)
            println(firebaseUser)


        openChestGif()
        getNfcTagID()

        getHatObject(nfcTagId, object : HatClothCallBack {
            override fun onCallback(value: HatClothingItem) {
                val imageUrl = value.clothingItemImageUrl!!
                println("clothimageUrl" + imageUrl)


                if (imageUrl != null) {
                    Picasso.get()
                        .load(imageUrl)
                        .placeholder(R.drawable.blue_hat_stick_man)
                        //.error(R.drawable.user_placeholder_error)
                        .into(imageView_loot);
                }

            }
        })







    }



    private fun openChestGif(){
        val imageViewChestOpen: ImageView = findViewById(R.id.imageView_chest) as ImageView

        Glide.with(this).asGif().load(R.drawable.chest_opening).listener(object :
            RequestListener<GifDrawable?> {

            override fun onResourceReady(
                resource: GifDrawable?,
                model: Any?,
                target: com.bumptech.glide.request.target.Target<GifDrawable?>?,
                dataSource: com.bumptech.glide.load.DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                if (resource != null) {
                    resource.setLoopCount(1)
                }
                if (resource != null) {
                    resource.registerAnimationCallback(object :
                        Animatable2Compat.AnimationCallback() {
                        override fun onAnimationStart(drawable: Drawable?) {
                            super.onAnimationStart(drawable)

                        }

                        override fun onAnimationEnd(drawable: Drawable) {
                            //do whatever after specified number of loops complete
                            checkForUser(firebaseUser)
                            //goToGameSession()

                        }


                    })
                }
                return false
                TODO("Not yet implemented")
            }

            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<GifDrawable?>?,
                isFirstResource: Boolean
            ): Boolean {
                TODO("Not yet implemented")
            }
        }).into(imageViewChestOpen)

    }

    fun ByteArray.toHexString() = joinToString("") { "%02x".format(it) }

    private fun getNfcTagID(){
        val tagId : ByteArray? = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID)
        if (tagId != null){
            textView_nfc_id.text = tagId?.toHexString().toString()
            nfcTagId = tagId?.toHexString().toString()


        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            val name = user.displayName

            //TODO:
            //when user is logged in
            val moveToUI = Intent(this@NfcActivity, Host::class.java)



            val userId = user.uid
            val userName = user.displayName
            moveToUI.putExtra("userId", userId)
            moveToUI.putExtra("userName", userName)
            startActivity(moveToUI)
            Toast.makeText(this, "Welcome", Toast.LENGTH_SHORT).show()
        } else {
//            text!!.text = "Firebase Login \n"
            //            Picasso.get().load(R.drawable.ic_firebase_logo).into(image);
//            btn_logout!!.visibility = View.INVISIBLE
//            btn_login!!.visibility = View.VISIBLE
        }
    }

    private fun checkForUser(firebaseUser: FirebaseUser?){



        if (firebaseUser != null){
            //there is login user
            updateUI(firebaseUser)
        }else{
            //move to login
            Toast.makeText(this, "no User - transaction to Login Page", Toast.LENGTH_LONG).show()

            val moveToLogin = Intent(this@NfcActivity, MainActivity::class.java)
            //todo: pass the scanned ID Tag for save in database and שnd get rid of another scan
            startActivity(moveToLogin)


        }
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


    private fun goToGameSession(){
        val intent = Intent(this, Host::class.java)
        intent.putExtra("toGame", true)
        startActivity(intent)
    }









}

//work with nfc
//        val tagId : ByteArray? = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID)
//        if (tagId != null){
//            textView_nfc_id.text = tagId?.toHexString().toString()
//
//        }
//
//        val myTag: Tag? = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG) as Tag?
//        println(myTag)
//
//        //val tagId = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID)
//        val msgs =
//            intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES) as Array<NdefMessage>?
//        println(msgs)


