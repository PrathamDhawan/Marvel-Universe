package com.oyelabs.marvel.universe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import java.math.BigInteger
import java.security.MessageDigest
import java.time.LocalDateTime

class MainActivity : AppCompatActivity(), CharacterClicked {

    private lateinit var mAdapter: CharacterAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fetchCharacters()

        val view: RecyclerView = findViewById(R.id.recyclerView)
        view.layoutManager = GridLayoutManager(this, 2)
        mAdapter = CharacterAdapter(this)
        view.adapter = mAdapter
    }

    private fun fetchCharacters(){

        val list: ArrayList<MCharacter> = ArrayList()
        val ts = LocalDateTime.now().toString()
        val hash = md5(ts+PRIVATE_KEY+PUBLIC_KEY)
        val url:String = ("$BASE_URL?ts=$ts&apikey=$PUBLIC_KEY&hash=$hash")

        val request = JsonObjectRequest(Request.Method.GET, url, null,
            {

            val data = it.getJSONObject("data")
            val characters = data.getJSONArray("results")
3
            for(i in 0 until characters.length()) {

                val character = characters.getJSONObject(i)
                val characterId = character.getString("id")

                val imageThumbnail = character.getJSONObject("thumbnail")
                val path = imageThumbnail.getString("path")
                if(path.contains("image_not_available")){
                    continue
                }
                val pathExtension = "."+imageThumbnail.getString("extension")
                val fullPath = "$path$IMG_SIZE$pathExtension"

                list.add(MCharacter(fullPath, characterId))
               }

            mAdapter.updateCharacters(list)

            }, { Toast.makeText(this, "Sorry", Toast.LENGTH_SHORT).show() })

        MySingleton.getInstance(this).addToRequestQueue(request)
    }

    private fun md5(input:String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }

    override fun onClick(characterId: String) {

        val intent = Intent(this, CharacterActivity::class.java)
        intent.putExtra("Character ID", characterId)
        startActivity(intent)
    }

    companion object {
        const val PUBLIC_KEY:String = "9110ea2ec9e42d3c8758f0fe1d7b2c2e"
        const val PRIVATE_KEY:String = "10272b5bddef2ff536f0118a3aed3710a53d724e"
        const val BASE_URL:String = "https://gateway.marvel.com/v1/public/characters"
        const val IMG_SIZE:String = "/standard_amazing"
    }
}