package com.keisardevs.myapplication.ui.newui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.keisardevs.myapplication.R
import com.keisardevs.myapplication.model.game.Quest

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


/**
 * A simple [Fragment] subclass.
 * Use the [LobbyFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LobbyFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    var mAuth: FirebaseAuth? = null
    lateinit var firebaseUser: FirebaseUser
    private lateinit var databaseReference: DatabaseReference



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            println(param1)
            param2 = it.getString(ARG_PARAM2)
        }


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAuth = FirebaseAuth.getInstance()
        firebaseUser = mAuth!!.currentUser!!
        databaseReference = FirebaseDatabase.getInstance().reference

        val startBtn = view?.findViewById<Button>(R.id.btn_lobby_start)

        if (startBtn != null) {
            startBtn.setOnClickListener {
                param1 = "test"
                println("click on start")
                val actualQuest = Quest(false, false, false, 0)
                databaseReference.child("Quests").child(firebaseUser.uid).setValue(actualQuest)

//                val fragment: Fragment = GameSessionFragment()
//
//                val fm: FragmentManager = requireActivity().getSupportFragmentManager()
//                val transaction: FragmentTransaction = fm.beginTransaction()
//                transaction.replace(R.id.nav_host_fragment, fragment)
//                transaction.addToBackStack(null)
//                transaction.commit()

                navigateToProductDetail("")



            }
        }

    }




    fun navigateToProductDetail(productId: String) {
        val fragment = GameSessionFragment()
        val fragment1 = LobbyFragment()
        val bottombar = BottomNavigationView(activity)
        bottombar.inflateMenu(2)

        requireActivity().supportFragmentManager.beginTransaction()
            .addToBackStack(GameSessionFragment.toString())
            .replace(R.id.nav_host_fragment, fragment)
            .hide(fragment1)
            .detach(fragment1)
            .remove(fragment1)
            .commit()
        val navController = activity?.let { Navigation.findNavController(it, R.id.nav_host_fragment) }

        val navView = activity?.findViewById<BottomNavigationView>(R.id.nav_view)
        if (navController != null) {
            navView?.setupWithNavController(navController)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_lobby, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LobbyFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LobbyFragment().apply {
                arguments = Bundle().apply {
                   val param12 = " param1"
                    putString(ARG_PARAM1, param12)
                    println("param1" + param12)
                    putString(ARG_PARAM2, param2)
                }
            }

    }
}