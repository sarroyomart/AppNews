package com.example.appnews.MainPageFragments

import android.content.res.Resources
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appnews.AuxNews.APIRequest
import com.example.appnews.R
import com.example.appnews.Adapters.RecyclerAdapter
import com.example.appnews.GlobalClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception


const val BASE_URL="https://api.currentsapi.services"
class HomeFragment : Fragment(){
    lateinit var countDownTimer: CountDownTimer
    private var titlesList = mutableListOf<String>()
    private var descList = mutableListOf<String>()
    private var imagesList = mutableListOf<String>()
    private var linksList = mutableListOf<String>()

    lateinit var rv_recyclerView: RecyclerView
    lateinit var v_blackScreen:View
    lateinit var progressBar: ProgressBar


    companion object {
        fun newInstance(): HomeFragment = HomeFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        val view: View = inflater.inflate(R.layout.fragment_home, container, false)
        rv_recyclerView=view.findViewById(R.id.rv_recyclerView)
        v_blackScreen=view.findViewById(R.id.v_blackScreen)
        progressBar=view.findViewById(R.id.progressBar)
        makeAPIRequest()

        return view;


    }
    private fun fadeInFromBlack(){
        v_blackScreen.animate().apply{
            alpha(0f)
            duration = 3000
        }.start()
    }

    private fun setUpRecyclerView(){
        rv_recyclerView.layoutManager = LinearLayoutManager(activity?.applicationContext)
        rv_recyclerView.adapter = RecyclerAdapter(titlesList, descList, imagesList, linksList)
    }

    fun addToList(title: String, description: String, image: String, link: String){
        titlesList.add(title)
        descList.add(description)
        imagesList.add(image)
        linksList.add(link)
    }
    private fun makeAPIRequest(){
        progressBar.visibility=View.VISIBLE

        val certificatePinner = CertificatePinner.Builder().add("api.currentsapi.services", "sha256/WD3/UZLGTPHg1VlcSUpolhJ4aSYxC6LKKvHZFiCnvM4=").build()

        val okHttpClient = OkHttpClient.Builder().certificatePinner(certificatePinner).build()

        val api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(APIRequest::class.java)

        GlobalScope.launch(Dispatchers.IO) {
            try{

                val response = api.getNews(GlobalClass.url)
                for(article in response.news){
                    Log.i("HomeFragment", "Result=$article")
                    addToList(article.title, article.description, article.image, article.url)
                }
                withContext(Dispatchers.Main){
                    setUpRecyclerView()
                    fadeInFromBlack()
                    progressBar.visibility=View.GONE
                }
            }catch(e: Exception){

                withContext(Dispatchers.Main){
                    attemptRequestAgain()
                }
            }
        }
    }

    private fun attemptRequestAgain(){
        countDownTimer = object:CountDownTimer(5*1000, 1000){
            override fun onTick(millisUntilFinished: Long) {
                Log.i("HomeFragment", "Could not retrieve data... Trying again in ${millisUntilFinished/1000} seconds")
            }

            override fun onFinish() {
                makeAPIRequest()
                countDownTimer.cancel()

            }

        }
        countDownTimer.start()
    }
}