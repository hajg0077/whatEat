package com.example.whateat.cookbook

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.whateat.R
import com.example.whateat.detail.DetailViewActivity
import com.example.whateat.model.MenuDTO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_cookbook.*
import kotlinx.android.synthetic.main.fragment_cookbook.view.*
import kotlinx.android.synthetic.main.fragment_mainmenu.view.*
import kotlinx.android.synthetic.main.item_mainmenu.view.*

class CookBookFragment: Fragment()  {

    var cookbookArray : ArrayList<MenuDTO> = arrayListOf()
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


        cookbookArray = ArrayList()

        firestore?.collection("Meun")?.addSnapshotListener{ querySnapshot, _ ->
            cookbookArray.clear()

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

                                if(itemB == null){
                                    cookbookArray.add(item!!)
                                    break
                                } else{
                                    for (ingredientSnapshotB in QuerySnapshot.documents) {

                                        var ingredientB = ingredientSnapshotB.getString("ingredientName")
                                        var ingredientHaveB = ingredientSnapshotB.getBoolean("have")

                                        if (ingredientHaveB == true && ingredientB == itemB){

                                            if (itemC == null){
                                                cookbookArray.add(item!!)
                                                break
                                            } else {
                                                for (ingredientSnapshotC in QuerySnapshot.documents){

                                                    var ingredientC = ingredientSnapshotC.getString("ingredientName")
                                                    var ingredientHaveC = ingredientSnapshotC.getBoolean("have")

                                                    if (ingredientHaveC == true && ingredientC == itemC){
                                                        cookbookArray.add(item!!)
                                                        break
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
        }


        var cookbookAdapter = CookbookRecyclerViewAdapter(cookbookArray as ArrayList<MenuDTO>)

        view.cookbook_searchEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                cookbookAdapter.getFilter().filter(p0)
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })


        view.cookbookRecyclerView.adapter = cookbookAdapter
        //view.cookbookRecyclerView.adapter = CookbookRecyclerViewAdapter()
        view.cookbookRecyclerView.layoutManager = LinearLayoutManager(activity)


        return view
    }

    inner class CookbookRecyclerViewAdapter(private val cookbookArray: ArrayList<MenuDTO>): RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable{

        var cookMenuDTOs : ArrayList<MenuDTO> = arrayListOf()
        var cookMenuDetailList : ArrayList<String> = arrayListOf()

        private var searchCookbookList: ArrayList<MenuDTO>? = null

        init {
            firestore?.collection("Meun")?.addSnapshotListener{ querySnapshot, _ ->
                cookMenuDTOs.clear()
                cookMenuDetailList.clear()

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

                                    if(itemB == null){
                                        cookMenuDTOs.add(item!!)
                                        cookMenuDetailList.add(snapshot.id)
                                        break
                                    } else{
                                        for (ingredientSnapshotB in QuerySnapshot.documents) {

                                            var ingredientB = ingredientSnapshotB.getString("ingredientName")
                                            var ingredientHaveB = ingredientSnapshotB.getBoolean("have")

                                            if (ingredientHaveB == true && ingredientB == itemB){

                                                if (itemC == null){
                                                    cookMenuDTOs.add(item!!)
                                                    cookMenuDetailList.add(snapshot.id)
                                                    break
                                                } else {
                                                    for (ingredientSnapshotC in QuerySnapshot.documents){

                                                        var ingredientC = ingredientSnapshotC.getString("ingredientName")
                                                        var ingredientHaveC = ingredientSnapshotC.getBoolean("have")

                                                        if (ingredientHaveC == true && ingredientC == itemC){
                                                            cookMenuDTOs.add(item!!)
                                                            cookMenuDetailList.add(snapshot.id)
                                                            break
                                                        }
                                                    }
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
            this.searchCookbookList = cookbookArray
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var view = LayoutInflater.from(parent.context).inflate(R.layout.item_mainmenu, parent, false)
            return  CustomViewHolder(view)
        }

        inner class  CustomViewHolder(view: View) : RecyclerView.ViewHolder(view)

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            var viewHolder = (holder as CustomViewHolder).itemView

            Glide.with(holder.itemView.context).load(searchCookbookList!![position].imageUrl).into(viewHolder.thumbnailImageView)

            viewHolder.titleTextView.text = searchCookbookList!![position].title

            //viewHolder.ingredientsTextView.text = cookMenuDTOs[position].ingredients

            viewHolder.setOnClickListener{
                var intent = Intent(context, DetailViewActivity::class.java)
                intent.putExtra("menutitle", cookMenuDetailList[position])
                intent.putExtra("title", searchCookbookList!![position].title)
                intent.putExtra("explanation", searchCookbookList!![position].explanation)
                intent.putExtra("imageUrl", searchCookbookList!![position].imageUrl)
                intent.putExtra("ingredients", searchCookbookList!![position].ingredients)
                intent.putExtra("hyperlink", searchCookbookList!![position].hyperlink)
                intent.putExtra("ingredientOne", searchCookbookList!![position].ingredientOne)
                intent.putExtra("ingredientTwo", searchCookbookList!![position].ingredientTwo)
                intent.putExtra("ingredientThree", searchCookbookList!![position].ingredientThree)
                intent.putExtra("ingredientOnetoex", searchCookbookList!![position].ingredientOnetoex)
                intent.putExtra("ingredientTwotoex", searchCookbookList!![position].ingredientTwotoex)
                intent.putExtra("ingredientThreetoex", searchCookbookList!![position].ingredientThreetoex)
                startActivity(intent)
            }
        }

        override fun getItemCount(): Int {
            return searchCookbookList!!.size
        }

        override fun getFilter(): Filter {
            return object : Filter(){
                override fun performFiltering(constraint: CharSequence?): FilterResults {
                    val charString = constraint.toString()
                    if(charString.isEmpty()){
                        searchCookbookList = cookbookArray
                    } else {
                        var filteringList = ArrayList<MenuDTO>()
                        for(item in cookbookArray){
                            if(item.title!!.toLowerCase().contains(charString.toLowerCase())) {
                                filteringList.add(item)
                            }
                        }
                        searchCookbookList = filteringList
                    }
                    val filterResults = FilterResults()
                    filterResults.values = searchCookbookList
                    return filterResults
                }

                override fun publishResults(constraint: CharSequence?, results: FilterResults?){
                    searchCookbookList = results?.values as ArrayList<MenuDTO>
                    notifyDataSetChanged()
                }
            }
        }

    }

}