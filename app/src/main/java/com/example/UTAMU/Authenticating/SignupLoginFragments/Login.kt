package com.example.UTAMU.Authenticating.SignupLoginFragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.NoConnectionError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.UTAMU.Activities.HomeActivity
import com.example.UTAMU.R
import com.example.UTAMU.SharePreforproject.RememberMe
import com.example.UTAMU.SharePreforproject.Uerdetails
import kotlinx.android.synthetic.main.login.*
import kotlinx.android.synthetic.main.login.view.*
import org.json.JSONException
import org.json.JSONObject

class Login : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.login, container, false)

//
        view.loginButton.setOnClickListener {
            val uname = loginuname.editText!!.text.toString()
            val upasswd = loginpasswd.editText!!.text.toString()
            posting(uname, upasswd)

        }
        return view
    }

    private fun posting(uname: String, upasword: String) {
        val requestQueue = Volley.newRequestQueue(activity)
        val parameters = JSONObject()
        try {
            parameters.put("username", uname)
            parameters.put("password", upasword)
            //            parameters.put("email", uemail);
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val jsonObjReq =
            JsonObjectRequest(
                Request.Method.POST,
                ROOT_URL_POST,
                parameters,
                Response.Listener { response ->
                    try {
                        val loggedin = response.getString("token") as String
                        if (loggedin.isNotEmpty()) {
                            Toast.makeText(activity, "loggedin", Toast.LENGTH_SHORT).show()
                            val unamepref = activity?.let { it1 -> Uerdetails(it1) }
                            unamepref?.save(uname, loggedin)

                            if (rememberme.isChecked) {
                                activity?.let { RememberMe(it).remember(uname, loggedin) }
                                Toast.makeText(activity, "You'll be remembered", Toast.LENGTH_SHORT)
                                    .show()
                            }

                            startActivity(Intent(activity, HomeActivity::class.java))
                            activity?.finish()
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener { error ->
                    when (error) {
                        is NoConnectionError -> Toast.makeText(
                            activity,
                            error.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                        else -> Toast.makeText(activity, error.toString(), Toast.LENGTH_SHORT)
                            .show()
                    }


                })

        requestQueue.add(jsonObjReq)
    }

    companion object {
        private const val ROOT_URL_POST = "http://192.168.43.87:5000/utamuapi/login/"
    }
}