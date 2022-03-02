package com.example.whateat.cookbook

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.whateat.R
import com.example.whateat.detail.DetailViewActivity
import com.example.whateat.mainmenu.MainMenusFragment
import com.example.whateat.model.MenuDTO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.fragment_cookbook.view.*
import kotlinx.android.synthetic.main.fragment_mainmenu.view.*
import kotlinx.android.synthetic.main.item_mainmenu.view.*

class CookBookFragment: Fragment()  {

    var firestore: FirebaseFirestore? = null
    var auth: FirebaseAuth? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = LayoutInflater.from(activity).inflate(R.layout.fragment_cookbook, container, false)

        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        view.cookbookRecyclerView.adapter = CookbookRecyclerViewAdapter()
        view.cookbookRecyclerView.layoutManager = LinearLayoutManager(activity)


        return view
    }

    inner class CookbookRecyclerViewAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>(){

        var menuDTOs : ArrayList<MenuDTO> = arrayListOf()
        var menuDetailList : ArrayList<String> = arrayListOf()

        init {
            firestore?.collection("Meun")?.addSnapshotListener{ querySnapshot, _ ->
                menuDTOs.clear()
                menuDetailList.clear()

                if(querySnapshot == null) return@addSnapshotListener

                firestore?.collection("User")!!
                    .document("${auth!!.currentUser?.uid}")
                    .collection("ingredient").addSnapshotListener{ QuerySnapshot, _ ->

                        if(QuerySnapshot == null) return@addSnapshotListener

                        for (snapshot in querySnapshot.documents){
                            var item = snapshot.toObject(MenuDTO::class.java)

                            for (ingredientSnapshotA in QuerySnapshot.documents){
                                var itemA = snapshot.getString("ingredientOne")
                                var itemB = snapshot.getString("ingredientTwo")
                                var itemC = snapshot.getString("ingredientThree")

                                var ingredientA = ingredientSnapshotA.getString("ingredientName")
                                var ingredientHaveA = ingredientSnapshotA.getBoolean("have")

                                if (ingredientHaveA == true && ingredientA == itemA){

                                    for (ingredientSnapshotB in QuerySnapshot.documents) {

                                        var ingredientB = ingredientSnapshotB.getString("ingredientName")
                                        var ingredientHaveB = ingredientSnapshotB.getBoolean("have")

                                        if (ingredientHaveB == true && ingredientB == itemB){

                                            for (ingredientSnapshotC in QuerySnapshot.documents){

                                                var ingredientC = ingredientSnapshotC.getString("ingredientName")
                                                var ingredientHaveC = ingredientSnapshotC.getBoolean("have")

                                                if (ingredientHaveC == true && ingredientC == itemC){

                                                    menuDTOs.add(item!!)
                                                    menuDetailList.add(snapshot.id)
                                                    break
                                                }
                                            }
                                        }
                                    }

                                }
                            }
                        }
                        notifyDataSetChanged()
                    }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var view = LayoutInflater.from(parent.context).inflate(R.layout.item_mainmenu, parent, false)
            return  CustomViewHolder(view)
        }

        inner class  CustomViewHolder(view: View) : RecyclerView.ViewHolder(view)

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            var viewHolder = (holder as CustomViewHolder).itemView

            Glide.with(holder.itemView.context).load(menuDTOs[position].imageUrl).into(viewHolder.thumbnailImageView)

            viewHolder.titleTextView.text = menuDTOs[position].title

            viewHolder.ingredientsTextView.text = menuDTOs[position].ingredients

            viewHolder.setOnClickListener{
                var intent = Intent(context, DetailViewActivity::class.java)
                intent.putExtra("menutitle", menuDetailList[position])
                intent.putExtra("title", menuDTOs[position].title)
                intent.putExtra("explanation", menuDTOs[position].explanation)
                intent.putExtra("imageUrl", menuDTOs[position].imageUrl)
                intent.putExtra("ingredients", menuDTOs[position].ingredients)
                intent.putExtra("hyperlink", menuDTOs[position].hyperlink)
                intent.putExtra("ingredientOne", menuDTOs[position].ingredientOne)
                intent.putExtra("ingredientTwo", menuDTOs[position].ingredientTwo)
                intent.putExtra("ingredientThree", menuDTOs[position].ingredientThree)
                startActivity(intent)
            }
        }

        override fun getItemCount(): Int {
            return menuDTOs.size
        }

    }

}