package com.example.whateat.mainmenu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.whateat.databinding.ItemMainmenuBinding
import com.example.whateat.model.MainMenuEntity

class MainMenuChangeAdapter: RecyclerView.Adapter<MainMenuChangeAdapter.MainMenuItemViewHolder>() {

    private var MainMenuList: List<MainMenuEntity> = listOf()
    private lateinit var MainMenuClickListener: (MainMenuEntity) -> Unit

    class MainMenuItemViewHolder(val binding: ItemMainmenuBinding, val MainMenuClickListener: (MainMenuEntity) -> Unit): RecyclerView.ViewHolder(binding.root){

        fun bindData(data: MainMenuEntity) = with(binding){
            titleTextView.text = data.title
            ingredientsTextView.text = data.explanation
                if (data.imageUrl.isNotEmpty()){
                    Glide.with(binding.thumbnailImageView)
                        .load(data.imageUrl)
                        .into(binding.thumbnailImageView)
                }

        }



        fun bindViews(data: MainMenuEntity) {
            binding.root.setOnClickListener {
                MainMenuClickListener(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainMenuItemViewHolder {
        val view = ItemMainmenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainMenuItemViewHolder(view, MainMenuClickListener)
    }

    override fun onBindViewHolder(holder: MainMenuItemViewHolder, position: Int) {
        holder.bindData(MainMenuList[position])
        holder.bindViews(MainMenuList[position])
    }

    override fun getItemCount(): Int = MainMenuList.size

    fun setSearchReaultList(MainMenuList: List<MainMenuEntity>, MainMenuClickListener: (MainMenuEntity) -> Unit) {
        this.MainMenuList = MainMenuList
        this.MainMenuClickListener = MainMenuClickListener
    }
}