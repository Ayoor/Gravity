package tech.ayodele.gravity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView



class ChatAdapter(private val forumPosts: MutableList<ForumPost>, topicForum: TopicForum) :
    RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.forum_bubble, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chatMessage = forumPosts[position]
        holder.bind(chatMessage)
    }

    override fun getItemCount(): Int {
        return forumPosts.size
    }

    inner class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val senderNameTextView: TextView = itemView.findViewById(R.id.senderNameTextView)
        private val messageTextView: TextView = itemView.findViewById(R.id.messageTextView)
        private val timestampTextView: TextView = itemView.findViewById(R.id.timestampTextView)

        fun bind(forumPost: ForumPost) {
            senderNameTextView.text = forumPost.senderName
            messageTextView.text = forumPost.message
            timestampTextView.text = forumPost.timestamp
        }
    }
}
