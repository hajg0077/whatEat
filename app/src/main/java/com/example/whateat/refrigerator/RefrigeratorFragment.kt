package com.example.whateat.refrigerator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.whateat.R
import com.example.whateat.model.MenuDTO
import com.example.whateat.model.RefrigeratorDTO
import com.example.whateat.model.UserDTO
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_mainmenu.view.*
import kotlinx.android.synthetic.main.fragment_mainmenu.view.mainMenuRecyclerView
import kotlinx.android.synthetic.main.fragment_refrigerator.view.*
import kotlinx.android.synthetic.main.item_ingredient.view.*

class RefrigeratorFragment: Fragment()  {

    var firestore: FirebaseFirestore? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = LayoutInflater.from(activity).inflate(R.layout.fragment_refrigerator, container, false)

        firestore = FirebaseFirestore.getInstance()

        view.refrigerator_RecyclerView.adapter = RefrigeratorRecyclerViewAdapter()
        view.refrigerator_RecyclerView.layoutManager = LinearLayoutManager(activity)


        return view
    }

    inner class RefrigeratorRecyclerViewAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>(){

        var refrigeratorDTOs : ArrayList<RefrigeratorDTO> = arrayListOf()
        var ingredientList : ArrayList<String> = arrayListOf()

        var userHaveDTOs : ArrayList<UserDTO> = arrayListOf()
        var userIngredientList : ArrayList<String> = arrayListOf()

        init {
            firestore?.collection("Ingredient")?.addSnapshotListener { querySnapshot, _ ->
                refrigeratorDTOs.clear()
                ingredientList.clear()

                if (querySnapshot == null) return@addSnapshotListener

                for (snapshot in querySnapshot.documents) {
                    var item = snapshot.toObject(RefrigeratorDTO::class.java)
                    refrigeratorDTOs.add(item!!)
                    ingredientList.add(snapshot.id)
                }
                notifyDataSetChanged()
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var view = LayoutInflater.from(parent.context).inflate(R.layout.item_ingredient, parent, false)
            return  CustomViewHolder(view)
        }

        inner class CustomViewHolder(view: View): RecyclerView.ViewHolder(view)

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            var viewHolder = (holder as CustomViewHolder).itemView

            viewHolder.checkbox.text = refrigeratorDTOs[position].ingredientName

            if (refrigeratorDTOs[position].have == true){
                !viewHolder.checkbox.isChecked
            } else {
                viewHolder.checkbox.isChecked
            }


        }

        override fun getItemCount(): Int {
            return refrigeratorDTOs.size
        }
    }





}