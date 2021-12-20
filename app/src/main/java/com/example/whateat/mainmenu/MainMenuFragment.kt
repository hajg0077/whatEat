package com.example.whateat.mainmenu

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.whateat.DBKey.Companion.DB_MAINMENUS
import com.example.whateat.R
import com.example.whateat.databinding.FragmentMainmenuBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainMenuFragment: Fragment(R.layout.fragment_mainmenu)  {

    private lateinit var mainMenuDB: DatabaseReference
    private lateinit var mainMenuAdapter: MainMenuAdapter

    private val mainMenuList = mutableListOf<MainMenuModel>()
    private val listener = object : ChildEventListener{
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            val mainMenuModel = snapshot.getValue(MainMenuModel::class.java)
            mainMenuModel?: return

            mainMenuList.add(mainMenuModel)
            mainMenuAdapter.submitList(mainMenuList)
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            //TODO("Not yet implemented")
        }

        override fun onChildRemoved(snapshot: DataSnapshot) {
            //TODO("Not yet implemented")
        }

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            //TODO("Not yet implemented")
        }

        override fun onCancelled(error: DatabaseError) {
            //TODO("Not yet implemented")
        }

    }

    private var binding: FragmentMainmenuBinding? = null
    private val auth: FirebaseAuth by lazy{
        Firebase.auth
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentMainMenuBinding = FragmentMainmenuBinding.bind(view)
        binding = fragmentMainMenuBinding

        mainMenuList.clear()
        mainMenuDB = Firebase.database.reference.child(DB_MAINMENUS)
        mainMenuAdapter = MainMenuAdapter()

        mainMenuAdapter.submitList(mutableListOf<MainMenuModel>().apply{
            add(MainMenuModel("0", "aaaa", ""))
            add(MainMenuModel("1", "bbbb", ""))
        })

        fragmentMainMenuBinding.mainMenuRecyclerView.layoutManager = LinearLayoutManager(context)
        fragmentMainMenuBinding.mainMenuRecyclerView.adapter = mainMenuAdapter

        mainMenuDB.addChildEventListener(listener)

    }

    override fun onResume() {
        super.onResume()

        mainMenuAdapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mainMenuDB.removeEventListener(listener)
    }
}