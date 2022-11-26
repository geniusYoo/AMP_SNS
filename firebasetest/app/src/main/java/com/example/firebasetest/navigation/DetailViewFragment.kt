package com.example.firebasetest.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.firebasetest.ContentDTO
import com.example.firebasetest.R
import com.google.firebase.firestore.FirebaseFirestore
import org.w3c.dom.Text

class DetailViewFragment : Fragment() {
    var fireStore : FirebaseFirestore? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = LayoutInflater.from(activity).inflate(R.layout.fragment_detail,container,false)
        fireStore = FirebaseFirestore.getInstance()

        view.findViewById<RecyclerView>(R.id.detailViewFragment_recyclerView).adapter = DetailViewRecyclerViewAdapter()
        view.findViewById<RecyclerView>(R.id.detailViewFragment_recyclerView).layoutManager = LinearLayoutManager(activity)
        return view
    }

    inner class DetailViewRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
        var contentDTOs : ArrayList<ContentDTO> = arrayListOf()
        var contentUidList : ArrayList<String> = arrayListOf()
        init {

            fireStore?.collection("images")?.orderBy("timestamp")?.addSnapshotListener { querySnapShot, firebaseFirestore ->
                contentDTOs.clear()
                contentUidList.clear()
                for(snapshot in querySnapShot!!.documents){
                    var item = snapshot.toObject(ContentDTO::class.java)
                    contentDTOs.add(item!!)
                    contentUidList.add(snapshot.id)
                }
                notifyDataSetChanged()
            }


        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var view = LayoutInflater.from(parent.context).inflate(R.layout.item_detail,parent,false)
            return CustomViewHolder(view)
        }

        inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view)

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            var viewHolder = (holder as CustomViewHolder).itemView

            viewHolder.findViewById<TextView>(R.id.detailViewItem_profile_textView).text = contentDTOs!![position].userId

            Glide.with(holder.itemView.context).load(contentDTOs!![position].imageUrl).into(viewHolder.findViewById<ImageView>(R.id.detailViewItem_imageView_content))

            viewHolder.findViewById<TextView>(R.id.detailViewItem_explain_tv).text = contentDTOs!![position].explain

            viewHolder.findViewById<TextView>(R.id.detailViewItem_favoriteCounter_tv).text="Likes "+contentDTOs!![position].favoriteCount

            Glide.with(holder.itemView.context).load(contentDTOs!![position].imageUrl).into(viewHolder.findViewById<ImageView>(R.id.detailViewItem_profile_img))


        }

        override fun getItemCount(): Int {
            return contentDTOs.size
        }


    }
}