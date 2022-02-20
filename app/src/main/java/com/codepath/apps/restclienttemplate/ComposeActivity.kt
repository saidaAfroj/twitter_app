package com.codepath.apps.restclienttemplate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.codepath.apps.restclienttemplate.models.Tweet
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers

class ComposeActivity : AppCompatActivity() {

    lateinit var etCompose : EditText
    lateinit var btnTweet : Button
     lateinit var client: TwitterClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compose)

        etCompose = findViewById(R.id.etTweetCompose)
        btnTweet= findViewById(R.id.btnTweet)
        client= TwitterApplication.getRestClient(this)

        //handing the user click on tweet button
        btnTweet.setOnClickListener{
            // 1st thing we need to grab the content of the editText(etCompose)

            val TweetContent = etCompose.text.toString()
            //Make sure the tweet is not empty
            if(TweetContent.isEmpty()){
                Toast.makeText(this,"Empty Tweets are not allowed",Toast.LENGTH_SHORT).show()
            }

            //make sure the tweet is under charecter count
           else if(TweetContent.length >140){
                Toast.makeText(this,"Tweet is too long ! Limit is 140 charecters",Toast.LENGTH_SHORT).show()
            }
            else {
                client.publishTweet(TweetContent,object :JsonHttpResponseHandler(){
                    override fun onFailure(
                        statusCode: Int,
                        headers: Headers?,
                        response: String?,
                        throwable: Throwable?
                    ) {
                       Log.i(TAG, "Failed",throwable)
                    }

                    override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                       //back to timeline activity to show the tweets
                        Log.i(TAG,"Published Tweets !")

                        val tweet = Tweet.fromJson(json.jsonObject)
                        val intent = Intent()
                        intent.putExtra("tweet", tweet)
                        setResult(RESULT_OK,intent)
                        finish()
                    }

                })

            }
            //Grab the API call to publish tweet
        }

    }
    companion object {
        val TAG = "ComposeActivity"
    }
}

