package com.example.whateat.mainmenu

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.whateat.DBKey.Companion.DB_MAINMENUS
import com.example.whateat.DatailActivity
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
    private val TAG = "MainMenuFragment"

    private var binding: FragmentMainmenuBinding? = null
    private val auth: FirebaseAuth by lazy{
        Firebase.auth
    }

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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentMainMenuBinding = FragmentMainmenuBinding.bind(view)
        binding = fragmentMainMenuBinding


        Log.d(TAG, "메인메뉴 프래그먼트 들어옴")
        mainMenuList.clear()
        mainMenuDB = Firebase.database.reference.child(DB_MAINMENUS)
        mainMenuAdapter = MainMenuAdapter(itemClickedListener = {
            Log.d(TAG,"아이템 클릭함")
            val intent = Intent(activity, DatailActivity::class.java)
            intent.putExtra("mainMenuModel", it)
            startActivity(intent)
        })

        mainMenuAdapter.submitList(mutableListOf<MainMenuModel>().apply {
            add(MainMenuModel("0", "김치", "","김치찌개","http","물", "김치", "양파"))
            add(MainMenuModel("1", "된장, 호박", "","된장찌개","www", "물", "된장", "호박"))
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