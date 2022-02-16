package com.example.whateat.mainmenu


import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.nfc.Tag
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.whateat.databinding.ItemMainmenuBinding
import com.example.whateat.detail.DetailViewActivity
import com.example.whateat.model.MainMenuEntity


class MainMenuAdapter(val mainMenuList: ArrayList<MainMenuModel>, private val context: Context): ListAdapter<MainMenuModel, MainMenuAdapter.ViewHolder>(diffUtil) {

//class MainMenuAdapter(private val onClickListener:  (MainMenuModel) -> (Unit) ): ListAdapter<MainMenuModel, MainMenuAdapter.ViewHolder>(diffUtil) {
    inner class ViewHolder(private val binding: ItemMainmenuBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(mainMenuModel: MainMenuModel){
            binding.titleTextView.text = mainMenuModel.title
            binding.ingredientsTextView.text = mainMenuModel.ingredients

            if (mainMenuModel.imageUrl.isNotEmpty()){
                Glide.with(binding.thumbnailImageView)
                    .load(mainMenuModel.imageUrl)
                    .into(binding.thumbnailImageView)
            }

            binding.root.setOnClickListener{
//                Log.d(TAG, "클릭함")
//                Intent(context, DetailActivity::class.java).apply {
//                    putExtra("data", mainMenuModel)
//                }.run { context.startActivity(this) }
//                onClickListener(mainMenuModel)
            }




        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemMainmenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)).apply {
            itemView.setOnClickListener {
                val curPos: Int = adapterPosition
                val menulist: MainMenuModel = mainMenuList.get(curPos)
//                val intent = Intent(context, DetailActivity::class.java)
//                intent.putExtra("mainMenu", menulist)
                Intent(context, DetailViewActivity::class.java).apply {
                        //putExtra("data", menulist)
                }.run { context.startActivity(this) }
//                startActivity(intent)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    //필요 없을거라고 예상
    companion object {
        val TAG = "MainMenuAdapter"
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