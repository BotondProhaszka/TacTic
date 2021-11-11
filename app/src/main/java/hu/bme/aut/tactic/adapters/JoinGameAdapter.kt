package hu.bme.aut.tactic.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.tactic.databinding.JoinGameRowBinding
import hu.bme.aut.tactic.model.OnlineHostLobby

class JoinGameAdapter (private val listener: JoinGameClickListener) :
    RecyclerView.Adapter<JoinGameAdapter.JoinGameViewHolder>(){

    private val items = mutableListOf<OnlineHostLobby>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = JoinGameViewHolder(JoinGameRowBinding.inflate(
        LayoutInflater.from(parent.context), parent, false)
    )


    override fun onBindViewHolder(holder: JoinGameViewHolder, position: Int) {
        val onlineHostLobby = items[position]
        holder.binding.tvLobbyName.text = onlineHostLobby.lobbyName
        holder.binding.tvWidth.text = onlineHostLobby.width.toString()
        holder.binding.tvHeight.text = onlineHostLobby.height.toString()
        holder.binding.joinRow.setOnClickListener {
            listener.onOnlineHostLobbyClicked(onlineHostLobby)
        }
    }

    fun addOnlineHostLobby(onlineHostLobby: OnlineHostLobby){
        items.add(onlineHostLobby)
        notifyDataSetChanged()
    }

    fun addOnlineHostLobbies(onlineHostLobbies: List<OnlineHostLobby>){
        items.addAll(onlineHostLobbies)
        notifyDataSetChanged()
    }

    fun update(onlineHostLobbies: List<OnlineHostLobby>){
        items.clear()
        items.addAll(onlineHostLobbies)
        notifyDataSetChanged()
    }

    fun remove(onlineHostLobby: OnlineHostLobby){
        items.remove(onlineHostLobby)
        notifyDataSetChanged()
    }

    fun clear(){
        items.clear()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size

    interface JoinGameClickListener{
        fun onOnlineHostLobbyClicked(onlineHostLobby: OnlineHostLobby)
    }

    inner class JoinGameViewHolder(val binding: JoinGameRowBinding) : RecyclerView.ViewHolder(binding.root)



}