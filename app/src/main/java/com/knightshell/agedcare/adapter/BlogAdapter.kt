package com.knightshell.agedcare.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.knightshell.agedcare.PostDetail
import com.knightshell.agedcare.R
import com.knightshell.agedcare.model.BlogModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_blog.view.*


class BlogAdapter(val blogList: ArrayList<BlogModel>): RecyclerView.Adapter<BlogAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlogAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_blog, parent, false)

        return ViewHolder(view)
    }



    override fun onBindViewHolder(holder: BlogAdapter.ViewHolder, position: Int) {
        holder.itemView.blogTitle.text = blogList[position].title
        val context = holder.itemView.context
        holder.bindItems(blogList[position])

        holder.itemView.blogTitle.setOnClickListener {
            val intent = Intent(context, PostDetail::class.java)
            intent.putExtra("title", blogList[position].title)
            intent.putExtra("description", blogList[position].description)
            intent.putExtra("image", blogList[position].image)
            intent.putExtra("created_at", blogList[position].created_at)

            // images
//            val bitmap: String = intent.getStringExtra("images")
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int = blogList.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(blog: BlogModel) {
            val textViewTitle = itemView.findViewById(R.id.blogTitle) as TextView
            val textViewDate  = itemView.findViewById(R.id.blogDate) as TextView
            val blogImageView: ImageView = itemView.findViewById(R.id.image)
            val textViewdDate: TextView = itemView.findViewById(R.id.date)
            textViewTitle.text = blog.title
            textViewDate.text = blog.description
            textViewdDate.text = blog.created_at
            Picasso.get().load(blog.image).placeholder(R.drawable.ic_error).error(R.drawable.ic_insert_photo).into(blogImageView);


        }


    }

}

