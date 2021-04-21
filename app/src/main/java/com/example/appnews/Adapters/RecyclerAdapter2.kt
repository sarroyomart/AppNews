package com.example.appnews.Adapters

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
import com.example.appnews.Cypher.CypherPol
import com.example.appnews.GlobalClass
import com.example.appnews.GlobalClass.Companion.ready2
import com.example.appnews.R
import com.google.firebase.database.*

private lateinit var database: FirebaseDatabase
private lateinit var referance: DatabaseReference
private var valores = mutableListOf<String>()
private lateinit var  recyclerView: RecyclerView

private lateinit var personId:String



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



        init{
            itemView.setOnClickListener{ v: View ->
                val position: Int = adapterPosition

                val intent= Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(links[position])
                ContextCompat.startActivity(itemView.context, intent, null)
            }

            itemHeart.setOnClickListener{v:View->
                referance.child(personId +"/"+valores[adapterPosition]).removeValue()
                ourRemoveAt(adapterPosition)
                ready2 = true

            }
            buttonShare.setOnClickListener { v:View->
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, links[adapterPosition])
                    type = "text/plain"
                }

                val shareIntent = Intent.createChooser(sendIntent, null)
                ContextCompat.startActivity(v.context, shareIntent, null)
            }

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter2.ViewHolder {
        val v= LayoutInflater.from(parent.context).inflate(R.layout.item_layout,parent,false)
        personId = CypherPol.decrypt(GlobalClass.key, GlobalClass.personId)
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
        referance.child(personId).addValueEventListener(object: ValueEventListener {


            override fun onDataChange(snapshot: DataSnapshot) {

                for(dsp: DataSnapshot in snapshot.getChildren()){
                    if(ready2 == false){
                        if(dsp.child("Title").getValue().toString().equals(titles[position]) && dsp.child("Description").getValue().toString().equals(details[position])){
                            holder.itemHeart.setBackgroundResource(R.drawable.ic_baseline_favorite_24_2)
                            valores[position]=dsp.key.toString()
                        }

                    }


                }


            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Cancelled", "Cancelled")
            }

        })
        holder.itemHeart.setBackgroundResource(R.drawable.ic_baseline_favorite_24_2)
        holder.itemTitle.text = titles[position]
        holder.itemDetail.text = details[position]

        Glide.with(holder.itemPicture)
                .load(images[position])
                .into(holder.itemPicture)

    }

    fun ourRemoveAt(position: Int){
        titles.removeAt(position)
        images.removeAt(position)
        links.removeAt(position)
        details.removeAt(position)
        valores.removeAt(position)

        notifyItemRemoved(position)
        notifyItemRangeChanged(position, titles.size)
        notifyItemRangeChanged(position, images.size)
        notifyItemRangeChanged(position, links.size)
        notifyItemRangeChanged(position, details.size)
        notifyDataSetChanged()

    }


}