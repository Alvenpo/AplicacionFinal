package com.example.aplicacionfinal

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class SuperHeroAdapter(var superheroList:List<SuperHeroItemResponse> /*= emptyList()*/) :
    RecyclerView.Adapter<SuperHeroViewHolder>() {

    /*fun updateList(superheroList: List<SuperHeroItemResponse>){
        this.superheroList = superheroList
        notifyDataSetChanged()
    }*/

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuperHeroViewHolder {
        return SuperHeroViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_superhero, parent, false)
        )
    }

    override fun getItemCount(): Int = superheroList.size

    override fun onBindViewHolder(holder: SuperHeroViewHolder, position: Int) {
        holder.bind(superheroList[position])
    }
}