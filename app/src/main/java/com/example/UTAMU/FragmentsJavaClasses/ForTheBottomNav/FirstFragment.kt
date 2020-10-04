package com.example.UTAMU.FragmentsJavaClasses.ForTheBottomNav

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.UTAMU.AdaptersJavaClasses.Carousel
import com.example.UTAMU.AdaptersJavaClasses.MostViewedAdapter
import com.example.UTAMU.AdaptersJavaClasses.RCVForHorizontalDisplay
import com.example.UTAMU.AdaptersJavaClasses.RestApiRCVA
import com.example.UTAMU.DataObjects.ForRest
import com.example.UTAMU.DataObjects.MostViewed
import com.example.UTAMU.R
import com.example.UTAMU.SharePreforproject.Uerdetails
import kotlinx.android.synthetic.main.frag1.*
import kotlinx.android.synthetic.main.frag1.view.*
import org.json.JSONArray
import java.util.*

class FirstFragment : Fragment() {

    private val items = listOf(
        "Mon 6/23 - Sunny - 31/17",
        "Tue 6/24 - Foggy - 21/8",
        "Wed 6/25 - Cloudy - 22/17",
        "Thurs 6/26 - Rainy - 18/11"
    )
    private val wtslideImages = listOf(
        R.drawable.grey1,
        R.drawable.grey6,
        R.drawable.grey4,
        R.drawable.grey9
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.frag1, container, false)
        val layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            activity!!.setActionBar(toolbarfrag1 as Toolbar?)
//        }
        val forRestArrayList: ArrayList<ForRest> = ArrayList<ForRest>()

        val imageList = intArrayOf(
            R.drawable.astudio21,
            R.drawable.astudio52,
            R.drawable.astudio45,
            R.drawable.astudio49,
            R.drawable.astudio45,
            R.drawable.astudio49,
            R.drawable.astudio49
        )
        val nameList = arrayOf("syd", "edge", "java", "major", "edge", "java", "major")
        val rcvForHorizontalDisplay =
            activity?.let { RCVForHorizontalDisplay(it, imageList, nameList) }
        itemDisplayRCV.adapter = rcvForHorizontalDisplay
        itemDisplayRCV.layoutManager = layoutManager


        val carousel = Carousel(items, wtslideImages)
        view.newsFlipper?.adapter = carousel

        val morerelatednewslayoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
//        view.morerelatednews?.adapter=carousel
//        view.morerelatednews?.layoutManager=morerelatednewslayoutManager

        val mostViewedArrayList: ArrayList<MostViewed> = ArrayList<MostViewed>()

        val layoutManager1 = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)

        val requestQueue: RequestQueue? = Volley.newRequestQueue(context)

        //Token from logging in
        val unamepref = activity?.let { Uerdetails(it) }
        val tokenfrompref: String = unamepref?.getValueString("KEY_TOKEN").toString()
        val credentials = tokenfrompref


        // Make a volley custom json object request with basic authentication
        val request = CustomJsonObjectRequestBasicAuth(
            Request.Method.GET,
            ROOT_URL,
            null,
            Response.Listener { response ->
                try {
                    for (i in 0 until response.length()) {
                        val jsonObject = response.getJSONObject(i)
                        val author =
                            jsonObject.getString("title") as String
                        val title =
                            jsonObject.getString("body") as String
                        val postimage =
                            jsonObject.getString("image") as String
                        val post =
                            jsonObject.getString("creationDate") as String
                        mostViewedArrayList.add(MostViewed(author, title, post, postimage))

                    }
                    val mostViewedAdapter =
                        activity?.let { MostViewedAdapter(it, mostViewedArrayList) }

                    view.latestviews.adapter = mostViewedAdapter
                    view.latestviews.layoutManager = layoutManager1

                    Toast.makeText(activity, "response.length()", Toast.LENGTH_SHORT)
                        .show()

                } catch (e: Exception) {
                    Toast.makeText(activity, "error ocurred $e", Toast.LENGTH_SHORT)
                        .show()
                    println(e)

                }
            },
            Response.ErrorListener {
                Toast.makeText(activity, "error ocurred ", Toast.LENGTH_SHORT)
                    .show()
                println("e")
            },
            credentials
        )
        requestQueue?.add(request)

        // Make a volley custom json object request2 with basic authentication
        val request2 = CustomJsonObjectRequestBasicAuth(
            Request.Method.GET,
            ROOT_URL2,
            null,
            Response.Listener { response ->
                try {
                    for (i in 0 until 6) {
                        val jsonObject = response.getJSONObject(i)
                        val title =
                            jsonObject.getString("post_title") as String
                        val body =
                            jsonObject.getString("post_body") as String
                        val datepost =
                            jsonObject.getString("posting_date") as String
                        forRestArrayList.add(ForRest(title, body, datepost))
                        val restApiRCVA =
                            activity?.let { RestApiRCVA(it, forRestArrayList) }
                        view.morerelatednews?.adapter = restApiRCVA
                        view.morerelatednews?.layoutManager = morerelatednewslayoutManager
                    }
//                    Toast.makeText(activity, "response.length()", Toast.LENGTH_SHORT)
//                        .show()

                } catch (e: Exception) {
                    Toast.makeText(activity, "error ocurred $e", Toast.LENGTH_SHORT)
                        .show()
                    println(e)

                }
            },
            Response.ErrorListener {
                Toast.makeText(activity, "error ocurred ", Toast.LENGTH_SHORT)
                    .show()
                println("e")
            },
            credentials
        )
        requestQueue?.add(request2)
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    class CustomJsonObjectRequestBasicAuth(
        method: Int, url: String,
        jsonArray: JSONArray?,
        listener: Response.Listener<JSONArray>,
        errorListener: Response.ErrorListener,
        credential_token: String
    ) : JsonArrayRequest(method, url, jsonArray, listener, errorListener) {

        private var mCredentials: String = credential_token

        @Throws(AuthFailureError::class)
        override fun getHeaders(): Map<String, String> {
            val headers = HashMap<String, String>()
            headers["Content-Type"] = "application/json"
            headers["Authorization"] = "Token $mCredentials"
            return headers
        }
    }

    companion object {
        const val ROOT_URL = "http://192.168.43.87:5000/utamuapi/carouseldisplay"
        const val ROOT_URL2 = "http://192.168.43.87:5000/utamuapi/newsposts"


    }
}


//AdapterViewFlipper's code is below this comment
//val featuresAdapterViewFlipper:AdapterViewFlipper=view.findViewById(R.id.featuresAdapterViewFlipper)
//val imageListFoAdapterView = intArrayOf(
//    R.drawable.asui61,
//    R.drawable.asui61,
//    R.drawable.asui61,
//    R.drawable.asui61,
//    R.drawable.asui61,
//    R.drawable.asui61,
//    R.drawable.asui61
//)
//view.featuresAdapterViewFlipper?.adapter = activity?.let {
//    MyAdapterViewFlippper(
//        it,
//        nameList,
//        imageListFoAdapterView
//    )
//}
//view.featuresAdapterViewFlipper?.isAutoStart = true
//view.featuresAdapterViewFlipper?.flipInterval = 5000
