package com.example.whateat.refrigerator

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.whateat.R
import com.example.whateat.model.MenuDTO
import com.example.whateat.model.RefrigeratorDTO
import com.example.whateat.model.UserDTO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import kotlinx.android.synthetic.main.fragment_mainmenu.view.*
import kotlinx.android.synthetic.main.fragment_mainmenu.view.mainMenuRecyclerView
import kotlinx.android.synthetic.main.fragment_refrigerator.view.*
import kotlinx.android.synthetic.main.item_ingredient.*
import kotlinx.android.synthetic.main.item_ingredient.view.*

class RefrigeratorFragment: Fragment()  {

    var userHaveDTOArray : ArrayList<UserDTO.Ingredient> = arrayListOf()
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


        userHaveDTOArray = ArrayList()
        firestore!!
            .collection("User")
            .document("${auth!!.currentUser?.uid}")
            .collection("ingredient")
            .addSnapshotListener { querySnapshot, _ ->

                userHaveDTOArray.clear()

                if (querySnapshot == null) return@addSnapshotListener

                for (snapshot in querySnapshot.documents) {
                    userHaveDTOArray.add(snapshot.toObject(UserDTO.Ingredient::class.java)!!)
                }
            }

        var refrigeratorAdapter = RefrigeratorRecyclerViewAdapter(userHaveDTOArray as ArrayList<UserDTO.Ingredient>)

        view.refrigerator_searchEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                refrigeratorAdapter.getFilter().filter(p0)
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

        view.refrigerator_RecyclerView.adapter = refrigeratorAdapter
        view.refrigerator_RecyclerView.layoutManager = LinearLayoutManager(activity)


        return view
    }

    inner class RefrigeratorRecyclerViewAdapter(private val userHaveDTOsArrayList: ArrayList<UserDTO.Ingredient>): RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable{

        private var searchHaveList: ArrayList<UserDTO.Ingredient>? = null

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
            this.searchHaveList = userHaveDTOsArrayList
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var view = LayoutInflater.from(parent.context).inflate(R.layout.item_ingredient, parent, false)
            return  CustomViewHolder(view)
        }

        inner class CustomViewHolder(view: View): RecyclerView.ViewHolder(view)

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            var viewHolder = (holder as CustomViewHolder).itemView

            viewHolder.checkbox.text = searchHaveList!![position].ingredientName

            viewHolder.checkbox.isChecked = searchHaveList!![position].have == true

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
                        .document("${searchHaveList!![position].ingredientName}")
                        .update(trueHave)
                }else {
                    firestore!!
                        .collection("User")
                        .document("${auth!!.currentUser?.uid}")
                        .collection("ingredient")
                        .document("${searchHaveList!![position].ingredientName}")
                        .update(falseHave)
                }

            }
        }

        override fun getItemCount(): Int {
            return searchHaveList!!.size
        }

        override fun getFilter(): Filter {
            return object : Filter(){
                override fun performFiltering(constraint: CharSequence?): FilterResults {
                    val charString = constraint.toString()
                    if(charString.isEmpty()){
                        searchHaveList = userHaveDTOsArrayList
                    } else {
                        var filteringList = ArrayList<UserDTO.Ingredient>()
                        for(item in userHaveDTOsArrayList){
                            if(item.ingredientName!!.toLowerCase().contains(charString.toLowerCase())) {
                                filteringList.add(item)
                            }
                        }
                        searchHaveList = filteringList
                    }
                    val filterResults = FilterResults()
                    filterResults.values = searchHaveList
                    return filterResults
                }

                override fun publishResults(constraint: CharSequence?, results: FilterResults?){
                    searchHaveList = results?.values as ArrayList<UserDTO.Ingredient>
                    notifyDataSetChanged()
                }
            }
        }
    }





}