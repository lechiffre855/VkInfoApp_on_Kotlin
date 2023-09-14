package com.example.vkinfokotlin

import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONException
import org.json.JSONObject
import utils.NetworkUtils.Companion.generateURL
import utils.NetworkUtils.Companion.getResponseFromURL
import java.io.IOException
import java.net.URL

class MainActivity : AppCompatActivity() {

    private var searchField: EditText? = null
    private var searchButton: Button? = null
    companion object {
        private var result: TextView? = null
        private var errorMessage: TextView? = null
        private var userNotExistMessage: TextView? = null
        private var progressBar: ProgressBar? = null


        private fun showResultTextView() {
            result?.visibility = View.VISIBLE
            errorMessage?.visibility = View.INVISIBLE
            userNotExistMessage?.visibility = View.INVISIBLE
        }

        private fun showErrorTextView() {
            result?.setVisibility(View.INVISIBLE)
            errorMessage?.setVisibility(View.VISIBLE)
            userNotExistMessage?.visibility = View.INVISIBLE
        }

        private fun showUserNotExist() {
            result?.setVisibility(View.INVISIBLE)
            errorMessage?.visibility = View.INVISIBLE
            userNotExistMessage?.visibility = View.VISIBLE
        }
    }

    class VkQueryTask: AsyncTask<URL, Void, String>() {

        override fun onPreExecute() {
            progressBar?.visibility = View.VISIBLE
        }

        override fun doInBackground(vararg urls: URL?): String? {
            var response: String? = null
            try {
                response = getResponseFromURL(urls[0])
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return response
        }

        override fun onPostExecute(response: String?) {
            var firstName: String? = null
            var lastName: String? = null

            if (response != null && !response.equals("")) {
                try {
                    var jsonObject1 = JSONObject(response)
                    var jsonArray = jsonObject1.getJSONArray("response")
                    var userInfo = jsonArray.getJSONObject(0)
                    firstName = userInfo.getString("first_name")
                    lastName = userInfo.getString("last_name")
                } catch (e: JSONException ) {
                    e.printStackTrace()
                }
                if (firstName != null && lastName != null) {
                    var resultString: String = ("Имя: $firstName\nФамилия: $lastName")
                    result?.text = resultString
                    showResultTextView()
                } else {
                    showUserNotExist()
                }
            } else {
                showErrorTextView()
            }
            progressBar?.visibility = View.INVISIBLE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        searchField = findViewById(R.id.et_search_field)
        searchButton = findViewById(R.id.b_search_vk)
        result = findViewById(R.id.tv_result)
        errorMessage = findViewById(R.id.tv_error_message)
        userNotExistMessage = findViewById(R.id.tv_userNotExist_message)
        progressBar = findViewById(R.id.pb_loading_indicator)

        var onClickListener: View.OnClickListener = object : View.OnClickListener {
            @Override
            override fun onClick(view: View) {
                var generatedURL: URL? = generateURL(searchField?.text.toString())
                VkQueryTask().execute(generatedURL)
            }
        }
        searchButton?.setOnClickListener(onClickListener)
    }
}