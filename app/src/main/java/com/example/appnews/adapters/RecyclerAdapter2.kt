package com.example.appnews.adapters

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appnews.GlobalClass
import com.example.appnews.GlobalClass.Companion.ready
import com.example.appnews.GlobalClass.Companion.ready2
import com.example.appnews.R
import com.google.firebase.database.*

private lateinit var database: FirebaseDatabase
private lateinit var referance: DatabaseReference
private var valores = mutableListOf<String>()
private lateinit var  recyclerView: RecyclerView



class RecyclerAdapter2 (private var titles: MutableList<String>,
                        private var details: MutableList<String>,
                        private var images: MutableList<String>,
                        private var links: MutableList<String>
) : RecyclerView.Adapter<RecyclerAdapter2.ViewHolder>(){

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val itemTitle: TextView = itemView.findViewById(R.id.tv_title)
        val itemDetail: TextView = itemView.findViewById(R.id.tv_description)
        val itemPicture: ImageView = itemView.findViewById(R.id.iv_image)
        val itemHeart: Button = itemView.findViewById(R.id.toggleButton)
        val buttonShare: Button = itemView.findViewById(R.id.buttonShare)
        val card:RelativeLayout = itemView.findViewById(R.id.rl_wrapper)



        init{
            itemView.setOnClickListener{ v: View ->
                val position: Int = adapterPosition

                val intent= Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(links[position])
                ContextCompat.startActivity(itemView.context, intent, null)


            }

            itemHeart.setOnClickListener{v:View->
                referance.child(GlobalClass.personId +"/"+valores[adapterPosition]).removeValue()
                /*titles.removeAt(adapterPosition)
                images.removeAt(adapterPosition)
                links.removeAt(adapterPosition)
                details.removeAt(adapterPosition)


                Log.d("titles", titles.toString())
                Log.d("details", details.toString())
                Log.d("images", images.toString())
                Log.d("links", links.toString())*/
                ourRemoveAt(adapterPosition)
                Log.d("TAMANIO", titles.size.toString())
                ready2 = true

            }

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter2.ViewHolder {
        val v= LayoutInflater.from(parent.context).inflate(R.layout.item_layout,parent,false)
        database = FirebaseDatabase.getInstance()
        referance = database.getReference()
        recyclerView = parent.findViewById(R.id.rv_recyclerViewFav)
        ready2 = false


        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return titles.size
    }

    override fun onBindViewHolder(holder: RecyclerAdapter2.ViewHolder, position: Int) {

        valores.add("")
        referance.child(GlobalClass.personId).addValueEventListener(object: ValueEventListener {


            override fun onDataChange(snapshot: DataSnapshot) {

                for(dsp: DataSnapshot in snapshot.getChildren()){
                    //addToList(dsp.child("Title").getValue().toString(), dsp.child("Description").getValue().toString(), dsp.child("Image").getValue().toString(), dsp.child("URL").getValue().toString())

                    if(ready2 == false){
                        if(dsp.child("Title").getValue().toString().equals(titles[position]) && dsp.child("Description").getValue().toString().equals(details[position])){
                            holder.itemHeart.setBackgroundResource(R.drawable.ic_baseline_favorite_24_2)
                            valores[position]=dsp.key.toString()
                        }

                    }


                }


            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Cancelled", "cancelado")
            }

        })
        Log.d("BCOADIOLO", "de shorizo")
        holder.itemHeart.setBackgroundResource(R.drawable.ic_baseline_favorite_24_2)
        holder.itemTitle.text = titles[position]
        holder.itemDetail.text = details[position]

        Glide.with(holder.itemPicture)
                .load(images[position])
                .into(holder.itemPicture)

        Log.d("RA", "llega")
    }

    fun ourRemoveAt(position: Int){
        titles.removeAt(position)
        images.removeAt(position)
        links.removeAt(position)
        details.removeAt(position)
        valores.removeAt(position)

        notifyItemRemoved(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, titles.size)
        notifyItemRangeChanged(position, images.size)
        notifyItemRangeChanged(position, links.size)
        notifyItemRangeChanged(position, details.size)
        notifyDataSetChanged()

    }


}