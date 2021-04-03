package com.example.appnews.adapters

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.telecom.Call
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appnews.GlobalClass.Companion.personId
import com.example.appnews.R
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.*


private lateinit var database: FirebaseDatabase
private lateinit var referance: DatabaseReference
private lateinit var ref2: DatabaseReference
private var favs = mutableListOf<Int>()
private var valores = mutableListOf<String>()




class RecyclerAdapter(private var titles: List<String>,
                      private var details: List<String>,
                      private var images: List<String>,
                      private var links: List<String>
) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {



    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val itemTitle: TextView = itemView.findViewById(R.id.tv_title)
        val itemDetail: TextView = itemView.findViewById(R.id.tv_description)
        val itemPicture: ImageView = itemView.findViewById(R.id.iv_image)
        val itemHeart: Button = itemView.findViewById(R.id.toggleButton)
        val buttonShare: Button = itemView.findViewById(R.id.buttonShare)

        init{
            itemView.setOnClickListener{ v:View->
                val position: Int = adapterPosition

                val intent= Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(links[position])
                startActivity(itemView.context, intent, null)

            }
            itemHeart.setOnClickListener{v:View->
                Log.d("POSICION", adapterPosition.toString())
                Log.d("valor", favs[adapterPosition].toString())

                if(favs[adapterPosition] == 0){
                    favs[adapterPosition]=1
                    itemHeart.setBackgroundResource(R.drawable.ic_baseline_favorite_24_2)
                    var pushkey = referance.child(personId).push().key.toString()
                    Log.d("PUSHKEY", pushkey)
                    var ref1= referance.child(personId+"/"+pushkey)

                    ref1.child("Title").setValue(titles[adapterPosition])
                    ref1.child("Description").setValue(details[adapterPosition])
                    ref1.child("URL").setValue(links[adapterPosition])
                    ref1.child("Image").setValue(images[adapterPosition])

                    valores[adapterPosition] = pushkey


                }

                else{
                    Log.d("else", "bocata de shorizo")
                    //favs[adapterPosition]=0
                    //itemHeart.setBackgroundResource(R.drawable.ic_baseline_favorite_border_24)
                    favs[adapterPosition]=0
                    itemHeart.setBackgroundResource(R.drawable.ic_baseline_favorite_border_24)
                    //getData(adapterPosition)
                    referance.child(personId+"/"+valores[adapterPosition]).removeValue()




                }

            }

            buttonShare.setOnClickListener { v: View ->
                Log.d("array", favs.toString())
                /*val position: Int = adapterPosition
                if(favs[position] ==1){
                    Log.d("BOCATA", "de choriso")
                }




                var ref1= referance.child(personId).push()

                ref1.child("Title").setValue(titles[position])
                ref1.child("Description").setValue(details[position])
                ref1.child("URL").setValue(links[position])
                ref1.child("Image").setValue(images[position])*/
            }

        }


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.ViewHolder {
        val v= LayoutInflater.from(parent.context).inflate(R.layout.item_layout,parent,false)

        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return titles.size
    }

    override fun onBindViewHolder(holder: RecyclerAdapter.ViewHolder, position: Int) {
        favs.add(0)
        valores.add("")

        database = FirebaseDatabase.getInstance()
        referance = database.getReference()

        ref2 = referance.child(personId)
        var found = 0

        ref2.addValueEventListener(object: ValueEventListener {


            override fun onDataChange(snapshot: DataSnapshot) {

                for(dsp: DataSnapshot in snapshot.getChildren()){
                    //addToList(dsp.child("Title").getValue().toString(), dsp.child("Description").getValue().toString(), dsp.child("Image").getValue().toString(), dsp.child("URL").getValue().toString())

                    if(dsp.child("Title").getValue().toString().equals(titles[position]) && dsp.child("Description").getValue().toString().equals(details[position])){
                        holder.itemHeart.setBackgroundResource(R.drawable.ic_baseline_favorite_24_2)
                        favs[position] = 1
                        valores[position]=dsp.key.toString()
                        found = 1
                    }

                }
                if(found == 0){
                    favs[position]=0
                }



            }



            override fun onCancelled(error: DatabaseError) {
                Log.d("Cancelled", "cancelado")
            }

        })


        holder.itemTitle.text = titles[position]
        holder.itemDetail.text = details[position]

        Glide.with(holder.itemPicture)
                .load(images[position])
                .into(holder.itemPicture)


        Log.d("RA", "llega")
    }
}