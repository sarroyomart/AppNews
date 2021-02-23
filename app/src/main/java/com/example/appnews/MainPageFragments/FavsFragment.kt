package com.example.appnews.MainPageFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.appnews.R

class FavsFragment: Fragment() {
    companion object {
        fun newInstance(): FavsFragment = FavsFragment()
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_favs, container, false)

    }
}