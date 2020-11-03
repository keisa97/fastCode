package com.keisardevs.myapplication.ui.user.tabs

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.keisardevs.myapplication.R
import com.keisardevs.myapplication.model.HatClothingItem

import com.keisardevs.myapplication.ui.user.tabs.dummy.DummyContent.DummyItem
import com.squareup.picasso.Picasso

/**
 * [RecyclerView.Adapter] that can display a [DummyItem].
 * TODO: Replace the implementation with code for your data type.
 */
class MyclothitemRecyclerViewAdapter(
    private val cloths: List<HatClothingItem>
) : RecyclerView.Adapter<MyclothitemRecyclerViewAdapter.ViewHolder>() {

    lateinit var databaseReference : DatabaseReference

    var mAuth: FirebaseAuth? = null
    lateinit var firebaseUser: FirebaseUser

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_cloth_item, parent, false)

        databaseReference = FirebaseDatabase.getInstance().reference
        mAuth = FirebaseAuth.getInstance()
        firebaseUser = mAuth!!.currentUser!!


        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = cloths[position]
        holder.clothName.text = item.clothingItemName
        holder.clothScore.text = item.scoreValue!!.toInt().toString()
        var clothingItemImageUrl = item.clothingItemImageUrl
        Picasso.get().load(clothingItemImageUrl).into(holder.clothImageView)


        holder.switch.setOnCheckedChangeListener { _, isChecked ->
            val clothToWear = if (isChecked) item.clothingItemID.toString() else ""
            setHatToWear(firebaseUser, clothToWear)
        }









    }

    override fun getItemCount(): Int = cloths.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val clothName: TextView = view.findViewById(R.id.tv_cloth_item_name)
        val clothScore: TextView = view.findViewById(R.id.tv_cloth_item_score)
        val clothImageView : ImageView = view.findViewById(R.id.imageview_cloth_item)
        val switch = view.findViewById<Switch>(R.id.switch_cloth_item)


        override fun toString(): String {
            return super.toString() + " '" + clothName.text + "'"
        }
    }

    private fun setHatToWear(firebaseUser: FirebaseUser,hatToWear : String ){
        val uidFromFirebaseUser: String = firebaseUser.uid
        val strUid = uidFromFirebaseUser

        val currentlyHatWearing = hatToWear.toString()
        databaseReference.child("users").child(strUid).child("activeHatClothId").setValue(currentlyHatWearing)

    }
}