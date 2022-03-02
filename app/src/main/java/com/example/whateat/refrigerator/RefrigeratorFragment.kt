package com.example.whateat.refrigerator

import android.os.Bundle
import android.util.Log
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_mainmenu.view.*
import kotlinx.android.synthetic.main.fragment_mainmenu.view.mainMenuRecyclerView
import kotlinx.android.synthetic.main.fragment_refrigerator.view.*
import kotlinx.android.synthetic.main.item_ingredient.*
import kotlinx.android.synthetic.main.item_ingredient.view.*

class RefrigeratorFragment: Fragment()  {

    var auth: FirebaseAuth? = null
    var firestore: FirebaseFirestore? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = LayoutInflater.from(activity).inflate(R.layout.fragment_refrigerator, container, false)

        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        view.refrigerator_RecyclerView.adapter = RefrigeratorRecyclerViewAdapter()
        view.refrigerator_RecyclerView.layoutManager = LinearLayoutManager(activity)


        return view
    }

    inner class RefrigeratorRecyclerViewAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>(){

        var refrigeratorDTOs : ArrayList<RefrigeratorDTO> = arrayListOf()
        var ingredientList : ArrayList<String> = arrayListOf()

        var userHaveDTOs : ArrayList<UserDTO.Ingredient> = arrayListOf()
        var userIngredientList : ArrayList<String> = arrayListOf()

        init {
            firestore!!
                .collection("User")
                .document("${auth!!.currentUser?.uid}")
                .collection("ingredient")
                .addSnapshotListener { querySnapshot, _ ->

                userHaveDTOs.clear()
                userIngredientList.clear()

                if (querySnapshot == null) return@addSnapshotListener

                for (snapshot in querySnapshot.documents) {
                    userHaveDTOs.add(snapshot.toObject(UserDTO.Ingredient::class.java)!!)
                    userIngredientList.add(snapshot.id)
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

            viewHolder.checkbox.text = userHaveDTOs[position].ingredientName

            viewHolder.checkbox.isChecked = userHaveDTOs[position].have == true

            var trueHave = hashMapOf<String, Any>(
                "have" to true
            )

            var falseHave = hashMapOf<String, Any>(
                "have" to false
            )


            viewHolder.checkbox.setOnClickListener{
                if (viewHolder.checkbox.isChecked){
                    firestore!!
                        .collection("User")
                        .document("${auth!!.currentUser?.uid}")
                        .collection("ingredient")
                        .document("${userHaveDTOs[position].ingredientName}")
                        .update(trueHave)
                }else {
                    firestore!!
                        .collection("User")
                        .document("${auth!!.currentUser?.uid}")
                        .collection("ingredient")
                        .document("${userHaveDTOs[position].ingredientName}")
                        .update(falseHave)
                }

            }
        }

        override fun getItemCount(): Int {
            return userHaveDTOs.size
        }
    }





}