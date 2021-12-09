package com.example.whateat.mainmenu

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.whateat.R
import com.example.whateat.databinding.FragmentMainmenuBinding

class MainMenuFragment: Fragment(R.layout.fragment_mainmenu)  {

    private var binding: FragmentMainmenuBinding? = null
    private lateinit var mainMenuAdapter: MainMenuAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentMainMenuBinding = FragmentMainmenuBinding.bind(view)
        binding = fragmentMainMenuBinding

        mainMenuAdapter = MainMenuAdapter()
        mainMenuAdapter.submitList(mutableListOf<MainMenuModel>().apply{
            add(MainMenuModel("0", "aaaa", ""))
        })

        fragmentMainMenuBinding.mainMenuRecyclerView.layoutManager = LinearLayoutManager(context)
        fragmentMainMenuBinding.mainMenuRecyclerView.adapter = mainMenuAdapter

    }
}