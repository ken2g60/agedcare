package com.knightshell.agedcare

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class PostDetail : AppCompatActivity() {

    lateinit var primaryTextView: TextView
    lateinit var supportingTextView: TextView
    lateinit var mediaImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)

        primaryTextView = findViewById(R.id.primary_text)
        supportingTextView = findViewById(R.id.supporting_text)
        mediaImageView = findViewById(R.id.media_image)

        var bundle :Bundle ?=intent.extras
        var message = bundle!!.getString("title")
        var description: String? = bundle!!.getString("description")
//        var image: String? = bundle!!.getString("image")
//        Picasso.get().load(image).placeholder(R.drawable.ic_error).error(R.drawable.ic_insert_photo).into(mediaImageView);

        primaryTextView.text = message
        supportingTextView.text = description
    }
}
