package com.example.whateat.mainmenu


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.whateat.databinding.ItemMainmenuBinding



class MainMenuAdapter(private val itemClickedListener: (MainMenuModel) -> Unit): ListAdapter<MainMenuModel, MainMenuAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(private val binding: ItemMainmenuBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(mainMenuModel: MainMenuModel){
                binding.titleTextView.text = mainMenuModel.title
                binding.ingredientsTextView.text = mainMenuModel.ingredients

                binding.root.setOnClickListener{
                    itemClickedListener(mainMenuModel)
                }

                if (mainMenuModel.imageUrl.isNotEmpty()){
                    Glide.with(binding.thumbnailImageView)
                            .load(mainMenuModel.imageUrl)
                            .into(binding.thumbnailImageView)
                }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemMainmenuBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    //필요 없을거라고 예상
    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<MainMenuModel>(){
            override fun areItemsTheSame(oldItem: MainMenuModel, newItem: MainMenuModel): Boolean {
                //왜 createdAt이 오류 나는 지 모르겠음
                //return oldItem.createdAt == newItem.createdAt
                return true
            }

            override fun areContentsTheSame(oldItem: MainMenuModel, newItem: MainMenuModel): Boolean {
                return oldItem == newItem
            }

        }
    }

}