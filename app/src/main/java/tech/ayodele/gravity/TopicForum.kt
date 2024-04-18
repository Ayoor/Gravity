package tech.ayodele.gravity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import tech.ayodele.gravity.databinding.ActivityTopicForumBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TopicForum : AppCompatActivity() {

    private lateinit var binding: ActivityTopicForumBinding
    private var allPosts: MutableList<ForumPost> = mutableListOf()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTopicForumBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.forumRecycler.visibility = View.VISIBLE
        binding.noPost.visibility = View.GONE

        binding.sendButton.setOnClickListener {
            val messageText = binding.postEditText.text.toString()
            val timestamp = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
            val newPost = ForumPost("Gregg", messageText, timestamp)
            uploadPost(newPost)
//            allPosts.add(newPost)
            binding.postEditText.text.clear()
            retrievePosts()
        }

        // Retrieve posts from Firebase
        retrievePosts()
    }

    private fun uploadPost(post: ForumPost) {
        val database = FirebaseDatabase.getInstance()
        val postsRef = database.reference.child("Posts").child("Exercise")
        postsRef.push().setValue(post)
    }

    private fun retrievePosts() {
        val database = FirebaseDatabase.getInstance()
        val databaseReference = database.getReference("/Posts/Exercise")

        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val posts: MutableList<ForumPost> = mutableListOf()
                for (postSnapshot in dataSnapshot.children) {
                    val post = postSnapshot.getValue(ForumPost::class.java)
                    post?.let {
                        posts.add(it)
                    }
                }
                allPosts = posts.reversed().toMutableList()

                // Set up the RecyclerView and adapter after retrieving posts
                val recyclerView: RecyclerView = findViewById(R.id.forumRecycler)
                recyclerView.layoutManager = LinearLayoutManager(this@TopicForum)
                recyclerView.adapter = ChatAdapter(allPosts, this@TopicForum)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("weightData", "Data retrieval cancelled: ${databaseError.message}")
            }
        })
    }
}
