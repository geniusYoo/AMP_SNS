package com.example.firebasetest.navigation

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toolbar
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.firebasetest.ContentDTO
import com.example.firebasetest.LoginActivity
import com.example.firebasetest.MainActivity
import com.example.firebasetest.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UserFragment : Fragment() {
    var fragmentView : View? = null
    var firestore : FirebaseFirestore? = null
    var uid : String? = null
    var auth : FirebaseAuth? = null
    var currentUserUid : String? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentView = LayoutInflater.from(activity).inflate(R.layout.fragment_user,container,false)
        uid = arguments?.getString("destinationUid")
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        currentUserUid = auth?.currentUser?.uid

        if(uid == currentUserUid) {
            fragmentView?.findViewById<Button>(R.id.account_btn_follow_signout)?.text = getString(R.string.signout)
            fragmentView?.findViewById<Button>(R.id.account_btn_follow_signout)?.setOnClickListener {
                activity?.finish()
                startActivity(Intent(activity, LoginActivity::class.java))
                auth?.signOut()
            }
        }
        else {
            fragmentView?.findViewById<Button>(R.id.account_btn_follow_signout)?.text = getString(R.string.follow)
            var mainActivity = (activity as MainActivity)
            mainActivity?.findViewById<TextView>(R.id.toolbar_username)?.text = arguments?.getString("userId")
            mainActivity?.findViewById<Button>(R.id.toolbar_btn_back)?.setOnClickListener {
                mainActivity.findViewById<BottomNavigationView>(R.id.bottom_navigation).selectedItemId = R.id.action_home
            }
            mainActivity?.findViewById<TextView>(R.id.toolbar_title_text)?.visibility = View.GONE
            mainActivity?.findViewById<TextView>(R.id.toolbar_username)?.visibility = View.VISIBLE
            mainActivity?.findViewById<Button>(R.id.toolbar_btn_back)?.visibility = View.VISIBLE
        }
        fragmentView?.findViewById<RecyclerView>(R.id.account_recyclerview)?.adapter = UserFragmentRecyclerViewAdapter()
        fragmentView?.findViewById<RecyclerView>(R.id.account_recyclerview)?.layoutManager = GridLayoutManager(requireActivity(), 3)
        return fragmentView
    }

    inner class UserFragmentRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        var contentDTOs : ArrayList<ContentDTO> = arrayListOf()
        init {
            firestore?.collection("images")?.whereEqualTo("uid",uid)?.addSnapshotListener{querySnapshot, firebaseFirestoreException ->
                if(querySnapshot == null) return@addSnapshotListener

                for(snapshot in querySnapshot.documents) {
                    contentDTOs.add(snapshot.toObject(ContentDTO::class.java)!!)
                }
                fragmentView?.findViewById<TextView>(R.id.account_tv_post_count)?.text = contentDTOs.size.toString()
                notifyDataSetChanged()
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var width = resources.displayMetrics.widthPixels / 3
            var imageView = ImageView(parent.context)
            imageView.layoutParams = LinearLayoutCompat.LayoutParams(width,width)
            return CustomViewHolder(imageView)
        }

        inner class CustomViewHolder(var imageView: ImageView) : RecyclerView.ViewHolder(imageView) {

        }

        override fun getItemCount(): Int {
            return contentDTOs.size
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            var imageView = (holder as CustomViewHolder).imageView
            Glide.with(holder.imageView.context).load(contentDTOs[position].imageUrl).apply(RequestOptions().centerCrop()).into(imageView)
        }
    }
}