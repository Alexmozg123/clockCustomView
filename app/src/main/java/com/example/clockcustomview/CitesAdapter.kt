package com.example.clockcustomview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class CitesAdapter(
    private val listCites: List<String>,
) : ListAdapter<String, CitesAdapter.CiteHolder>(CiteComparator()) {

    class CiteHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val cite: TextView by lazy { itemView.findViewById(R.id.textView) }
        private val clock: ClockView by lazy { itemView.findViewById(R.id.clockView) }

        fun bind(citeName: String) {
            cite.text = citeName
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CiteHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_cite, parent, false)
        return CiteHolder(view)
    }

    override fun onBindViewHolder(holder: CiteHolder, position: Int) {
        holder.bind(listCites[position])
    }

    class CiteComparator : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: String, newItem: String) =
            oldItem == newItem
    }
}