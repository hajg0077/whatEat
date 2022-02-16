package com.example.whateat.mainmenu


import android.content.ClipData.newIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.whateat.DBKey.Companion.DB_MENUS
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
    var ct: Context? = null

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

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
        override fun onChildRemoved(snapshot: DataSnapshot) {}
        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
        override fun onCancelled(error: DatabaseError) { }
    }

    private val startDetailForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result: ActivityResult ->
            // 성공적으로 처리 완료 이후 동작

        }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentMainMenuBinding = FragmentMainmenuBinding.bind(view)
        binding = fragmentMainMenuBinding

        Log.d(TAG, "메인메뉴 프래그먼트 들어옴")

        mainMenuList.clear()
        mainMenuDB = Firebase.database.reference.child(DB_MENUS)



        //mainMenuAdapter = MainMenuAdapter( MainMenuModel, activity
            //startDetailForResult.launch(
                //DetailActivity.newIntent(requireContext(), it.title)
            //)
            //Log.d(TAG, "아이템 클릭")
            //val intent = Intent(activity, DetailActivity::class.java)
            //intent.putExtra("mainMenu", mainMenuDB)
            //startActivity(intent)
            //startActivity(Intent(activity, DetailActivity::class.java))
        //)

//        mainMenuAdapter.setOnItemClickListener(object : MainMenuAdapter.OnItemClickListener{
//            override fun onItemClick(v: View, data: MainMenuModel, pos : Int) {
//                Intent(activity, DetailActivity::class.java).apply {
//                    putExtra("data", data)
//                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                }.run { startActivity(this) }
//            }
//
//        })

        fragmentMainMenuBinding.mainMenuRecyclerView.layoutManager = LinearLayoutManager(this.context)
        fragmentMainMenuBinding.mainMenuRecyclerView.adapter = mainMenuAdapter

        mainMenuDB.addChildEventListener(listener)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_mainmenu, container, false)

        ct = container!!.context

        return super.onCreateView(inflater, container, savedInstanceState)
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