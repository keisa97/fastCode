package com.keisardevs.myapplication.ui

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.keisardevs.myapplication.R
import com.keisardevs.myapplication.model.HatClothingItem
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.fragment_temp_add_cloth.*
import java.io.ByteArrayOutputStream
import java.io.IOException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TempAddClothFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TempAddClothFragment : Fragment() {

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var databaseReference: DatabaseReference

    private lateinit var firebaseUser: FirebaseUser

    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var clothName : String
    private lateinit var clothId : String
    private var clothScoreValue : Int = 0

    //image part
    private lateinit var clothImageUrl: String
    private var imageUploaded: Boolean = false
    private lateinit var clothType : String

    private var clothReadyToUpload: Boolean = false

    private lateinit var bitmap: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_temp_add_cloth, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_upload_image.setOnClickListener(View.OnClickListener { v: View? ->
            //for simple image chooser without cropping
            //showSoundImageChooser();
            CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON) //.setMaxCropResultSize(1000,1000)
                //.setFixAspectRatio(true)
                //.setCropShape(CropImageView.CropShape.RECTANGLE)
                .start(requireActivity(), this)
        })

        btn_upload_hat_cloth.setOnClickListener {
            clothType = "hat"
            uploadClothItem()
        }

        btn_upload_shirt_cloth.setOnClickListener {
            clothType = "shirt"
            uploadClothItem()
        }

        btn_upload_pants_cloth.setOnClickListener {
            clothType = "pants"
            uploadClothItem()
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        //for cropping use
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result: CropImage.ActivityResult = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                val resultUri: Uri = result.getUri()
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(
                        requireActivity().contentResolver,
                        resultUri
                    )
                    btn_upload_image.setImageBitmap(bitmap)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error: Exception = result.getError()
                println(error)
            }
        }
    }

    private fun uploadClothItem(){
        getEditTextFields()
        handleImageUpload(bitmap)

    }

    private fun getEditTextFields(){
        clothName =  et_cloth_name.text.toString()
        clothId = et_cloth_id.text.toString()
        clothScoreValue = et_cloth_score.text.toString().toInt()
    }

    private fun getImageFileDownloadUrl(reference: StorageReference) {
        imageUploaded = false
        reference.getDownloadUrl()
            .addOnSuccessListener(OnSuccessListener<Uri> { uri -> //soundDownloadUrl = uri.toString();
                clothImageUrl = uri.toString()
                imageUploaded = true
                println("clothImageUrl" + clothImageUrl)
                if (imageUploaded){
                    whenFinishImageUploadSetDatabase()
                }

            })
    }

    private fun handleImageUpload(bitmap: Bitmap) {
        println("handleImageUpload()")
        val progressDialog = ProgressDialog(context)
        progressDialog.setTitle("uploading image")
        progressDialog.show()
        progressDialog.setCancelable(false)
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        println(bitmap)

        val reference = FirebaseStorage.getInstance().reference
            .child("cloth/$clothType/$clothId.jpeg")
           // .child("$clothId.jpeg")
      var uploadTask =  reference.putBytes(baos.toByteArray())
            uploadTask.addOnSuccessListener {
                getImageFileDownloadUrl(reference)
                progressDialog.dismiss()
                println("addOnSuccessListener")
            }
            uploadTask.addOnFailureListener { e ->
                Log.e(
                    "onFailure: ",
                    e.toString()
                )
            }.addOnProgressListener { taskSnapshot ->
                val progress = 100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
                //progressDialog.setMessage((int)progress+"% uploaded");
            }
    }

    private fun whenFinishImageUploadSetDatabase(){
        val hatClothToUpload = HatClothingItem(clothImageUrl, clothId, clothName, clothScoreValue)
        println("hatClothToUpload" + hatClothToUpload)
        databaseReference = FirebaseDatabase.getInstance().reference
        databaseReference.child("clothItem/$clothType/$clothId").setValue(hatClothToUpload)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TempAddClothFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TempAddClothFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


}