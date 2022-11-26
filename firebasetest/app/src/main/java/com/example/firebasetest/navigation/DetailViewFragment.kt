package com.example.firebasetest.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.firebasetest.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class DetailViewFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = LayoutInflater.from(activity).inflate(R.layout.fragment_detail,container,false)
        return view
    }


}