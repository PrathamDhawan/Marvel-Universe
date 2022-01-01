package com.oyelabs.marvel.universe

import android.view.LayoutInflater
import android.view.OnReceiveContentListener
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class CharacterAdapter(private val listener: CharacterClicked) : RecyclerView.Adapter<CharacterViewHolder>() {

    private var mCharacters: ArrayList<MCharacter> = ArrayList()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CharacterViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.marvel_characters, parent,
            false)

        return CharacterViewHolder(view)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        Glide.with(holder.characterImage.context)
            .load(mCharacters[position].imageUrl).into(holder.characterImage)
        holder.characterImage.setOnClickListener {
            listener.onClick(mCharacters[position].characterId)
        }
    }

    override fun getItemCount(): Int {
        return mCharacters.size
    }

    fun updateCharacters(characters:ArrayList<MCharacter>){
        mCharacters.clear()
        mCharacters.addAll(characters)

        notifyDataSetChanged()
    }

}
class CharacterViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val characterImage: ImageView = view.findViewById(R.id.imageView)
}

interface CharacterClicked{

    fun onClick(characterId: String){
    }
}