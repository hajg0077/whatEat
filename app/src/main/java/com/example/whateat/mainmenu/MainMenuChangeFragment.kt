package com.example.whateat.mainmenu

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.whateat.R
import com.example.whateat.databinding.FragmentMainmenuBinding
import com.example.whateat.model.DetailEntity
import com.example.whateat.model.MainMenuEntity

class MainMenuChangeFragment: Fragment(R.layout.fragment_mainmenu) {

    private var binding: FragmentMainmenuBinding? = null
    private lateinit var adapter: MainMenuChangeAdapter
    private val TAG = "MainMenuChangeFragment"




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "MainMenuChangeFragment 들어옴")
        val fragmentMainMenuBinding = FragmentMainmenuBinding.bind(view)
        binding = fragmentMainMenuBinding


        initAdapter()

        fragmentMainMenuBinding.mainMenuRecyclerView.layoutManager = LinearLayoutManager(this.context)
        fragmentMainMenuBinding.mainMenuRecyclerView.adapter = adapter
        initViews()
        initData()
        setData()
    }

    private fun initViews() = with(binding) {
        val fragmentMainMenuBinding = view?.let { FragmentMainmenuBinding.bind(it) }
        fragmentMainMenuBinding?.mainMenuRecyclerView?.adapter = adapter
        Log.d(TAG, "MainMenuChangeFragment initViews 들어옴")

    }

    private fun initAdapter() {
        adapter = MainMenuChangeAdapter()
        Log.d(TAG, "MainMenuChangeFragment initAdapter들어옴")

    }

    private fun initData(){
        adapter.notifyDataSetChanged()
        Log.d(TAG, "MainMenuChangeFragment 중 ininData들어옴")

    }

    private fun setData(){
        Log.d(TAG, "MainMenuChangeFragment중 setData 들어옴")
        val dataList = (0..10).map{
            MainMenuEntity(
                title = "$it",
                imageUrl = "$it",
                explanation = "$it",
                classification = "$it",
                detail = DetailEntity(
                    ingredients = "$it",
                    hyperlink = "$it"
                )
            )
        }
        adapter.setSearchReaultList(dataList){

            Log.d(TAG, "title : ${it.title}")

        }
    }
}