package com.keisardevs.myapplication.ui.newui

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import android.nfc.NfcAdapter.ReaderCallback
import android.nfc.tech.Ndef
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.keisardevs.myapplication.R


class Host : AppCompatActivity() {

    var fragment: Fragment? = null

    private var mAdapter: NfcAdapter? = null
    private var mPendingIntent: PendingIntent? = null

     var intentFiltersArray: Array<IntentFilter> = emptyArray()
     var techList: Array<Array<String>> = emptyArray()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_host)

        //for hide the actionBar (contains Fragment name) on top of the screen
        val actionBar = supportActionBar
        actionBar?.hide()



        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        val navView = findViewById<BottomNavigationView>(R.id.nav_view)
        navView?.setupWithNavController(navController)


        NavigationUI.setupWithNavController(navView, navController)



        //tries to stop poping the activity each time NFC Tag scanned
        mAdapter = NfcAdapter.getDefaultAdapter(this)



//        val fragment: Fragment = GameSessionFragment()
//
//        val fm: FragmentManager = getSupportFragmentManager()
//
//        val transaction: FragmentTransaction = fm.beginTransaction()
//        transaction.replace(R.id.nav_host_fragment, fragment)
//        transaction.commit()



    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        for (fragment in supportFragmentManager.fragments) {
            fragment.onActivityResult(requestCode, resultCode, data)
        }
    }


    fun ByteArray.toHexString() = joinToString("") { "%02x".format(it) }


    private fun testTest(){

        val flags = NfcAdapter.FLAG_READER_NFC_A


        mAdapter!!.enableReaderMode(this, ReaderCallback { tag ->
            runOnUiThread {
                Log.d("WTF", "Tag discovered")
                val tagId: String = (tag.id).toHexString()
                Toast.makeText(
                    this@Host,
                    "tag detected",
                    Toast.LENGTH_SHORT
                ).show()
                val ndef = Ndef.get(tag)

            }
        }, flags, null)

    }


}


//private fun toGameSession(whatToDo: Boolean){
//
//    if (whatToDo) {
//
//        var mFragment: Fragment? = null
//        mFragment = GameSessionFragment()
//        val fragmentManager: FragmentManager = supportFragmentManager
//        fragmentManager.beginTransaction().replace(R.id.nav_host_fragment_container, mFragment).commit()
//    }
//}
//
//val intent = intent
//val whatToDO = intent.getBooleanExtra("toGame", true)