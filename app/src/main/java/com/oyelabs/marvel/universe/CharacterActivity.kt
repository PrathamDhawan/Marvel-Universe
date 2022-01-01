package com.oyelabs.marvel.universe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.bumptech.glide.Glide
import org.w3c.dom.Text
import java.math.BigInteger
import java.security.MessageDigest
import java.time.LocalDateTime

class CharacterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character)

        val characterId = intent.getStringExtra("Character ID")
        fetchCharacter(characterId.toString())

    }

    private fun fetchCharacter(id: String){

        val ts = LocalDateTime.now().toString()
        val hash = md5(ts+PRIVATE_KEY+PUBLIC_KEY)
        val url:String = ("$BASE_URL$id?ts=$ts&apikey=$PUBLIC_KEY&hash=$hash")

        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            {

                val data = it.getJSONObject("data")
                val characterData = data.getJSONArray("results")
                val characterObject = characterData.getJSONObject(0)

                val characterName = characterObject.getString("name")
                val characterDescription = characterObject.getString("description")
                val characterId = characterObject.getInt("id")

                val imageThumbnail = characterObject.getJSONObject("thumbnail")
                val path = imageThumbnail.getString("path")
                val pathExtension = "."+imageThumbnail.getString("extension")
                val fullPath = "$path$IMG_SIZE$pathExtension"

                val image: ImageView = findViewById(R.id.imageView)
                Glide.with(this).load(fullPath).into(image)

                val name: TextView = findViewById(R.id.nameView)
                name.text = characterName

                val description: TextView = findViewById(R.id.descriptionView)
                description.text = characterDescription

                val id: TextView = findViewById(R.id.idView)
                id.text = "Id - $characterId"

            }, { Toast.makeText(this, "Sorry", Toast.LENGTH_SHORT).show() })

        MySingleton.getInstance(this).addToRequestQueue(request)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun md5(input:String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }

   companion object{
        const val PUBLIC_KEY:String = "9110ea2ec9e42d3c8758f0fe1d7b2c2e"
        const val PRIVATE_KEY:String = "10272b5bddef2ff536f0118a3aed3710a53d724e"
        const val BASE_URL:String = "https://gateway.marvel.com/v1/public/characters/"
        const val IMG_SIZE:String = "/standard_amazing"
    }
}