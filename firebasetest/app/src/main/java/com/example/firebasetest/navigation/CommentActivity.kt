package com.example.firebasetest.navigation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.firebasetest.ContentDTO
import com.example.firebasetest.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CommentActivity : AppCompatActivity() {
    var contentUid : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)
        contentUid = intent.getStringExtra("contentUid")

        findViewById<Button>(R.id.comment_btn_send)?.setOnClickListener {
            var comment = ContentDTO.Comment()
            comment.userId = FirebaseAuth.getInstance().currentUser?.email
            comment.uid = FirebaseAuth.getInstance().currentUser?.uid
            comment.comment = findViewById<EditText>(R.id.comment_edit_message).text.toString()
            comment.timestamp = System.currentTimeMillis()

            FirebaseFirestore.getInstance().collection("images").document(contentUid!!)
                .collection("comments").document().set(comment)
            findViewById<EditText>(R.id.comment_edit_message).setText("")
        }
    }
}