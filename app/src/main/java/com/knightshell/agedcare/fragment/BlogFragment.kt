package com.knightshell.agedcare.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.knightshell.agedcare.Endpoints
import com.knightshell.agedcare.R
import com.knightshell.agedcare.VolleyService
import com.knightshell.agedcare.adapter.BlogAdapter
import com.knightshell.agedcare.model.BlogModel
import io.sentry.core.Sentry
import org.json.JSONArray
import org.json.JSONException


class BlogFragment : Fragment() {


    lateinit var recyclerView: RecyclerView
    lateinit var refresh: SwipeRefreshLayout
    var blogList = ArrayList<BlogModel>()
    lateinit var adapter: BlogAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? { // Inflate the layout for this fragment

        val view: View = inflater.inflate(R.layout.fragment_blog, container, false)

        recyclerView = view.findViewById(R.id.blog_recycleview)
        recyclerView.layoutManager = LinearLayoutManager(context)


        blogList = ArrayList<BlogModel>()

        // refresh
        refresh = view.findViewById(R.id.refresh)
        refresh.setOnRefreshListener {
            loadBlogData()
            refresh.isRefreshing = false
        }


        adapter = BlogAdapter(blogList)
        recyclerView.adapter = adapter



        loadBlogData()

        return view
    }

    fun loadBlogData(){

        val stringRequest = StringRequest(Request.Method.GET, Endpoints.BLOG_JSON_URL,
            Response.Listener<String> { response ->

                try{
                    val jsonarray = JSONArray(response)

                    for (k in 0 until jsonarray.length()) {
                        val jsonobject = jsonarray.getJSONObject(k)
                        val blog = BlogModel(jsonobject.getString("title"), jsonobject.getString("description"), jsonobject.getString("image"), jsonobject.getString("created_at"))
                        blogList.add(blog)

                    }

                    adapter!!.notifyDataSetChanged()

                } catch(e: JSONException){
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                // Handle error
                // textView.text = "ERROR: %s".format(error.toString())
                Toast.makeText(context, "Error in Internet Connection", Toast.LENGTH_LONG).show()
                Sentry.captureMessage(error.localizedMessage)
                refresh.isRefreshing = false
            })



        VolleyService.requestQueue.add(stringRequest)
        VolleyService.requestQueue.start()
    }


}
