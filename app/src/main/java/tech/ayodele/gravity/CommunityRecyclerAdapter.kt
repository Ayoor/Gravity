package tech.ayodele.gravity

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tech.ayodele.gravity.databinding.CommunityItemsBinding

class CommunityAdapter(private val items: List<CommunityItems>, private val listener: OnItemClickListener) : RecyclerView.Adapter<CommunityAdapter.CommunityViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(item: CommunityItems)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CommunityItemsBinding.inflate(inflater, parent, false)
        return CommunityViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: CommunityViewHolder, position: Int) {
        val currentItem = items[position]
        holder.bind(currentItem)
    }

    override fun getItemCount() = items.size

    inner class CommunityViewHolder(private val binding: CommunityItemsBinding, private val listener: OnItemClickListener) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            binding.root.setOnClickListener(this)
        }

        fun bind(item: CommunityItems) {
            binding.apply {
                topic.text = item.topic
                content.text = item.description
                commentCount.text = item.commentCount.toString()
            }
        }

        override fun onClick(view: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val item = items[position]
                listener.onItemClick(item)

            }
        }
    }
}
