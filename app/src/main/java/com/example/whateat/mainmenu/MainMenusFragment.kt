package com.example.whateat.mainmenu

import android.content.Context
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
import com.example.whateat.databinding.FragmentMainmenuBinding
import com.example.whateat.detail.DetailViewActivity
import com.example.whateat.model.MenuDTO
import com.google.firebase.firestore.FirebaseFirestore
import io.grpc.internal.JsonUtil.getList
import kotlinx.android.synthetic.main.fragment_mainmenu.*
import kotlinx.android.synthetic.main.fragment_mainmenu.view.*
import kotlinx.android.synthetic.main.item_mainmenu.view.*

class MainMenusFragment : Fragment() {


    var menuDTOsArray : ArrayList<MenuDTO> = arrayListOf()
    var firestore: FirebaseFirestore? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        var view = LayoutInflater.from(activity).inflate(R.layout.fragment_mainmenu,
            container,
            false)

        firestore = FirebaseFirestore.getInstance()

        menuDTOsArray = ArrayList()
        firestore?.collection("Meun")?.addSnapshotListener{ querySnapshot, _ ->
            menuDTOsArray.clear()
            if(querySnapshot == null) return@addSnapshotListener

            for(snapshot in querySnapshot.documents){
                var item = snapshot.toObject(MenuDTO::class.java)
                menuDTOsArray.add(item!!)
            }
        }


        var mianMenuAdapter = MainMenuRecyclerViewAdapter(menuDTOsArray as ArrayList<MenuDTO>)

        view.mainMenu_searchEditText.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                mianMenuAdapter.getFilter().filter(p0)
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })


        view.mainMenuRecyclerView.adapter = mianMenuAdapter
        view.mainMenuRecyclerView.layoutManager = LinearLayoutManager(activity)


        return view
    }



    inner class MainMenuRecyclerViewAdapter(private val menuDTOsArrayList: ArrayList<MenuDTO>): RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable{

        var menuDTOs : ArrayList<MenuDTO> = arrayListOf()
        var menuDetailList : ArrayList<String> = arrayListOf()

        private var searchList: ArrayList<MenuDTO>? = null

        init {
            firestore?.collection("Meun")?.addSnapshotListener{ querySnapshot, _ ->
                menuDTOs.clear()
                menuDetailList.clear()

                if(querySnapshot == null) return@addSnapshotListener

                for(snapshot in querySnapshot.documents){
                    var item = snapshot.toObject(MenuDTO::class.java)
                    menuDTOs.add(item!!)
                    menuDetailList.add(snapshot.id)
                }
                notifyDataSetChanged()
            }
            this.searchList = menuDTOsArrayList
        }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var view = LayoutInflater.from(parent.context).inflate(R.layout.item_mainmenu,
                parent, false)
            return  CustomViewHolder(view)
        }

        inner class  CustomViewHolder(view: View) : RecyclerView.ViewHolder(view)


        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            var viewHolder = (holder as CustomViewHolder).itemView

            Glide.with(holder.itemView.context).load(searchList!![position].imageUrl).into(
                viewHolder.thumbnailImageView)

            viewHolder.titleTextView.text = searchList!![position].title

            //viewHolder.ingredientsTextView.text = filteredList[position].ingredients

            viewHolder.setOnClickListener{
                var intent = Intent(context, DetailViewActivity::class.java)
                intent.putExtra("menutitle", menuDetailList[position])
                intent.putExtra("title", searchList!![position].title)
                intent.putExtra("explanation", searchList!![position].explanation)
                intent.putExtra("imageUrl", searchList!![position].imageUrl)
                intent.putExtra("ingredients", searchList!![position].ingredients)
                intent.putExtra("hyperlink", searchList!![position].hyperlink)
                intent.putExtra("ingredientOne", searchList!![position].ingredientOne)
                intent.putExtra("ingredientTwo", searchList!![position].ingredientTwo)
                intent.putExtra("ingredientThree", searchList!![position].ingredientThree)
                intent.putExtra("ingredientOnetoex", searchList!![position].ingredientOnetoex)
                intent.putExtra("ingredientTwotoex", searchList!![position].ingredientTwotoex)
                intent.putExtra("ingredientThreetoex", searchList!![position].ingredientThreetoex)


                startActivity(intent)
            }
        }

        override fun getItemCount(): Int {
            return searchList!!.size
        }

        override fun getFilter(): Filter {
            return object : Filter(){
                override fun performFiltering(constraint: CharSequence?): FilterResults {
                    val charString = constraint.toString()
                     if(charString.isEmpty()){
                        searchList = menuDTOsArrayList
                    } else {
                        var filteringList = ArrayList<MenuDTO>()
                        for(item in menuDTOsArrayList){
                            if(item.title!!.toLowerCase().contains(charString.toLowerCase())) {
                                filteringList.add(item)
                            }
                        }
                        searchList = filteringList
                    }
                    val filterResults = FilterResults()
                    filterResults.values = searchList
                    return filterResults
                }

                override fun publishResults(constraint: CharSequence?, results: FilterResults?){
                    searchList = results?.values as ArrayList<MenuDTO>
                    notifyDataSetChanged()
                }
            }
        }

    }


}