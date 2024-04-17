package tech.ayodele.gravity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import tech.ayodele.gravity.databinding.ActivityTopicForumBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TopicForum : AppCompatActivity() {
    //todo:
    // 1. fetch and save chat in database
    // 2. get and add user name to chat
    // 3. add new chat to the bottom of screen
    // 4. arrange post by latest

    private lateinit var binding: ActivityTopicForumBinding
    private val chatMessages = emptyList<ChatMessage>().toMutableList()
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_topic_forum)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //        binding
        binding = ActivityTopicForumBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.forumRecycler.visibility = View.VISIBLE
        binding.noPost.visibility = View.GONE


        // Recycler adapter
        val recyclerView: RecyclerView = findViewById(R.id.forumRecycler)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = ChatAdapter(chatMessages, this)
        recyclerView.adapter = adapter


        binding.sendButton.setOnClickListener{

                // Get the text from the input EditText
                val messageText = binding.postEditText.text.toString()

                // Generate a timestamp for the message (you can use SimpleDateFormat for this)
                val timestamp = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())

                // Create a new ChatMessage object
                val chatMessage = ChatMessage("YourName", messageText, timestamp)

                // Add the chat message to the dataset used by the adapter
                chatMessages.add(chatMessage)

                // Notify the adapter of the dataset change
                adapter.notifyDataSetChanged()
val chatET = findViewById<EditText>(R.id.postEditText)
            chatET.text.clear()

        }
    }
}