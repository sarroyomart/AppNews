package com.example.appnews.MainPageFragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appnews.GlobalClass.Companion.personId
import com.example.appnews.GlobalClass.Companion.ready
import com.example.appnews.R
import com.example.appnews.adapters.RecyclerAdapter
import com.example.appnews.adapters.RecyclerAdapter2
import com.google.firebase.database.*

class FavsFragment: Fragment() {
    private lateinit var database: FirebaseDatabase
    private lateinit var referance: DatabaseReference

    private val titleList = mutableListOf<String>()
    private val descriptionList = mutableListOf<String>()
    private val imageList = mutableListOf<String>()
    private val urlList = mutableListOf<String>()


    lateinit var rv_recyclerView: RecyclerView


    companion object {
        fun newInstance(): FavsFragment = FavsFragment()
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_favs, container, false)
        database = FirebaseDatabase.getInstance()
        referance = database.getReference()
        var ref = referance.child(personId)
        rv_recyclerView=view.findViewById(R.id.rv_recyclerViewFav)
        ready = false



        ref.addValueEventListener(object: ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {

                for(dsp:DataSnapshot in snapshot.getChildren()){
                    if (ready == false){
                        addToList(dsp.child("Title").getValue().toString(), dsp.child("Description").getValue().toString(), dsp.child("Image").getValue().toString(), dsp.child("URL").getValue().toString())
                    }


                }
                if (ready == false){
                    Log.d("achiou", "bocatita")
                    Log.d("titles", titleList.toString())
                    setUpRecyclerView()
                }




            }



            override fun onCancelled(error: DatabaseError) {
                Log.d("Cancelled", "cancelado")
            }

        })





        return view
    }

    fun addToList(title: String, description: String, image: String, link: String){
        titleList.add(title)
        descriptionList.add(description)
        imageList.add(image)
        urlList.add(link)
    }

    private fun setUpRecyclerView(){
        rv_recyclerView.layoutManager = LinearLayoutManager(activity?.applicationContext)//CREO QUE ES ASI, ANTES ESTABA ApplicationContext
        rv_recyclerView.adapter = RecyclerAdapter2(titleList, descriptionList, imageList, urlList)
        ready = true
    }


}