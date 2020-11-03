package com.keisardevs.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.keisardevs.myapplication.model.User
import com.keisardevs.myapplication.nfc.NfcActivity
import com.keisardevs.myapplication.ui.newui.Host
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    val GOOGLE_SIGN_IN = 123
    var mAuth: FirebaseAuth? = null
    lateinit var firebaseUser: FirebaseUser
    private var btn_login: Button? = null
    private var btn_logout: Button? = null
    private var btn_login_byEmail: Button? = null
    private var image: ImageView? = null
    private var text: TextView? = null
    var progressBar: ProgressBar? = null
    var mGoogleSignInClient: GoogleSignInClient? = null
    //val callbackManager = CallbackManager.Factory.create();

    private lateinit var databaseReference: DatabaseReference

    lateinit var ETAccountName: EditText
    private var accountName :String? = null





    lateinit var callbackManager: CallbackManager
    private val EMAIL = "email"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val actionBar = supportActionBar
//        actionBar?.hide()
        setContentView(R.layout.activity_main)
        //btn_login = findViewById(R.id.btn_google_register)
        val signInButton = findViewById(R.id.btn_google_register) as SignInButton


        btn_to_nfc_activity.setOnClickListener { toNfcActivity() }
        //facebook login:
        //first try:

//        facebook_login_button.setReadPermissions(listOf(EMAIL));
//
//        facebook_login_button.registerCallback(callbackManager,
//
//            object : FacebookCallback<LoginResult> {
//            override fun onSuccess(loginResult: LoginResult) {
//                Log.d("letsSee", "Facebook token: " + loginResult.accessToken.token)
//                Log.d("FBLOGIN", loginResult.accessToken.token.toString())
//                Log.d("FBLOGIN", loginResult.recentlyDeniedPermissions.toString())
//                Log.d("FBLOGIN", loginResult.recentlyGrantedPermissions.toString())
//
//                val moveToUI = Intent(this@MainActivity, HomePage::class.java)
//
//                startActivity(moveToUI)
//                //Toast.makeText(this, "Welcome", Toast.LENGTH_SHORT).show()
//
//                val request = GraphRequest.newMeRequest(loginResult.accessToken) { `object`, response ->
//                    try {
//                        //here is the data that you want
//                        Log.d("FBLOGIN_JSON_RES", `object`.toString())
//
//                        if (`object`.has("id")) {
//                            //handleSignInResultFacebook(`object`)
//                        } else {
//                            Log.e("FBLOGIN_FAILD", `object`.toString())
//                        }
//
//                    } catch (e: Exception) {
//                        e.printStackTrace()
//                       // dismissDialogLogin()
//                    }
//                    println("facebook")
//
//
//                }
//
//
//                val parameters = Bundle()
//                parameters.putString("fields", "name,email,id,picture.type(large)")
//                request.parameters = parameters
//                request.executeAsync()
//
//
//            }
//
//            override fun onCancel() {
//                Log.d("FBLOGIN_FAILD", "Facebook onCancel.")
//                println("facebook")
//
//            }
//
//            override fun onError(error: FacebookException) {
//                Log.d("FBLOGIN_FAILD", "Facebook onError.")
//                println("facebook")
//
//            }
//        })

//        val accessToken: AccessToken = AccessToken.getCurrentAccessToken()
//        val isLoggedIn = accessToken != null && !accessToken.isExpired()

        //LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));


        //second try for facebook login

        facebook_login_button.setOnClickListener {
            facebook_login_button.setReadPermissions(listOf(EMAIL))
            callbackManager = CallbackManager.Factory.create()
            // If you are using in a fragment, call loginButton.setFragment(this);
            // Callback registration
            // If you are using in a fragment, call loginButton.setFragment(this);
            // Callback registration
            facebook_login_button.registerCallback(
                callbackManager,
                object : FacebookCallback<LoginResult?> {
                    override fun onSuccess(loginResult: LoginResult?) {
                        Log.d("MainActivity", "Facebook token: " + loginResult!!.accessToken.token)
                        startActivity(
                            Intent(
                                applicationContext,
                                LogoutActivity::class.java
                            )
                        )// App code
                    }

                    override fun onCancel() { // App code
                    }

                    override fun onError(exception: FacebookException) { // App code
                    }
                })


            callbackManager = CallbackManager.Factory.create()
            LoginManager.getInstance().registerCallback(callbackManager,
                object : FacebookCallback<LoginResult?> {
                    override fun onSuccess(loginResult: LoginResult?) { // App code
                    }

                    override fun onCancel() { // App code
                    }

                    override fun onError(exception: FacebookException) { // App code
                    }
//                    val accessToken = AccessToken.getCurrentAccessToken()
//                    accessToken != null && !accessToken.isExpired


                })

            FacebookSdk.sdkInitialize(applicationContext) {
                if (AccessToken.getCurrentAccessToken() == null) {
                    println("not logged in yet")
                } else {
                    println("Logged in")
                }
            }
        }

        //google login part:
        mAuth = FirebaseAuth.getInstance()
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        /*btn_register_byEmail.setOnClickListener(v -> {
            startActivity(new Intent(MyAccount_activity.this,RegisterActivity.class));
        });*/
        signInButton.setOnClickListener(View.OnClickListener { v: View? -> SignInGoogle() })
//        btn_logout.setOnClickListener(View.OnClickListener { v: View? -> Logout() })
//        if (mAuth!!.currentUser != null) {
//            val user = mAuth!!.currentUser
//            updateUI(user)
//        }
//        btn_login_byEmail.setOnClickListener(View.OnClickListener { v: View? ->
//            startActivity(
//                Intent(
//                    this@MyAccount_activity,
//                    EmailLoginActivity::class.java
//                )
//            )
//        })

        btn_set_user.setOnClickListener {
            //writeNewUser(firebaseUser)
            //updateUI(firebaseUser)
        }




    }

    fun SignInGoogle() {
        // progressBar.setVisibility(View.VISIBLE);
        val signInIntent = mGoogleSignInClient!!.signInIntent
        Log.d("GoogleLoGin", "google good.")
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN)
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d("TAG", "firebaseAuthWithGoogle:" + acct.id)
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth!!.signInWithCredential(credential)
            .addOnCompleteListener(
                this
            ) { task: Task<AuthResult?> ->
                if (task.isSuccessful) {
                    onAuthSuccess(task.result?.user!!)
                    progress_circular!!.visibility = View.INVISIBLE
                    Log.d("TAG", "signInWithCredential:success")
                    firebaseUser = mAuth!!.currentUser!!
//                    btn_set_user.visibility = View.VISIBLE
//                    editTextAccountName.visibility = View.VISIBLE
                    updateUI(firebaseUser)
                } else {
                    progress_circular!!.visibility = View.INVISIBLE
                    Log.w(
                        "TAG",
                        "signInWithCredential:failure",
                        task.exception
                    )
                    Toast.makeText(
                        this, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                    updateUI(null)
                }
            }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
//        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data)
        //callbackManager.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GOOGLE_SIGN_IN) {
            val task =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account =
                    task.getResult(ApiException::class.java)
                account?.let { firebaseAuthWithGoogle(it) }
            } catch (e: ApiException) {
                Log.w("TAG", "Google sign in failed", e)
            }
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            val name = user.displayName

            //TODO:
            //when user is logged in
            val moveToUI = Intent(this@MainActivity, Host::class.java)



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

    private fun Logout() {
        FirebaseAuth.getInstance().signOut()
        mGoogleSignInClient!!.signOut().addOnCompleteListener(
            this
        ) { task: Task<Void?>? ->
            updateUI(
                null
            )
        }
    }


    private fun onAuthSuccess(user: FirebaseUser) {
        databaseReference = Firebase.database.reference
        //updateUI(firebaseUser)
        Toast.makeText(this, "Welcome!", Toast.LENGTH_SHORT).show()



        //writeNewUser(user)
//        val username = usernameFromEmail(user.email!!)
//
//        // Write new user
//        writeNewUser(user.uid, username, user.email)
//
//        // Go to MainActivity
//        startActivity(Intent(this@SignInActivity, MainActivity::class.java))
//        finish()
    }

    private fun usernameFromEmail(email: String): String {
        return if (email.contains("@")) {
            email.split("@".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
        } else {
            email
        }
    }

    private fun writeNewUser(user: FirebaseUser) {

        ETAccountName = findViewById<EditText>(R.id.editTextAccountName)

        accountName = ETAccountName.text.toString().trim()

        println(accountName + "account name")


        //todo: check there is no same account name exist
        //while(...)

        if (accountName!!.isEmpty() || accountName.equals("enter account name") ){
            ETAccountName.error = "Please enter a Account name"

        }

        val uidFromFirebaseUser: String = user.uid
        val strUid = uidFromFirebaseUser
        val userName = user.displayName.toString()
        //new user - score start from 0
        val score = 0

        val databaseAccountUser = User(strUid, userName, accountName!!, score)
        databaseReference.child("users").child(strUid).setValue(databaseAccountUser)



    }

    fun toNfcActivity(){
        val moveToUI = Intent(this@MainActivity, NfcActivity::class.java)
        startActivity(moveToUI)

    }






}