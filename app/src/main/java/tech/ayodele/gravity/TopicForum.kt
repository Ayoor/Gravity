package tech.ayodele.gravity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import tech.ayodele.gravity.databinding.ActivityTopicForumBinding
import tech.ayodele.gravity.model.UserDetails
import tech.ayodele.gravity.view.Community
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.util.Date
import java.util.Locale

//todo:
// 1. get dateandtime and set to bubble -> done
// 2. get topic and send it to Activity -> done
// 3. get real username and set it to bubble ->
// 4. adjust topic and arrow position / background -> done
// 5. disallow empty posts

class TopicForum : AppCompatActivity() {

    private lateinit var binding: ActivityTopicForumBinding
    private var allPosts: MutableList<ForumPost> = mutableListOf()
    private lateinit var prefs: SharedPreferences
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        prefs = getSharedPreferences("saveData", Context.MODE_PRIVATE)
        val topic = intent.getStringExtra("topic")
        binding = ActivityTopicForumBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.forumRecycler.visibility = View.VISIBLE
        binding.noPost.visibility = View.GONE

        binding.forumTopic.text = topic

        binding.sendButton.setOnClickListener {
            val messageText = binding.postEditText.text.toString()
            val timestamp = formattedDate()
            val name = retrieveUserName(prefs)
            val newPost = ForumPost(name, messageText, timestamp)
            uploadPost(newPost, topic!!)
            binding.postEditText.text.clear()
            retrievePosts(topic)
        }


        binding.backArrow.setOnClickListener{
            startActivity(Intent(this, Community::class.java))
            finish()
        }

        // Retrieve posts from Firebase
        retrievePosts(topic!!)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private  fun formattedDate(): String {
        // Convert LocalDate to LocalDateTime
        val currentDate = LocalDate.now()
        val localDateTime = LocalDateTime.of(currentDate, LocalTime.now())

        // Convert LocalDateTime to Date
        val dateObj = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant())

        // Define date and time format
        val dateFormat = SimpleDateFormat("dd-MM-yy", Locale.getDefault())
        val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

        // Format the date and time
        val formattedDate = dateFormat.format(dateObj)
        val formattedTime = timeFormat.format(dateObj)

        // Return the formatted string
        return "$formattedDate, $formattedTime"
    }

    private fun uploadPost(post: ForumPost, topic: String) {
        if(post.message.isEmpty()){
            Toast.makeText(this@TopicForum, "Please enter something to Post.", Toast.LENGTH_SHORT).show()
        }
        else{
            val database = FirebaseDatabase.getInstance()
            val postsRef = database.reference.child("Posts").child(topic)
            postsRef.push().setValue(post)
        }

    }

    private fun retrievePosts(topic: String) {
        val database = FirebaseDatabase.getInstance()
        val databaseReference = database.getReference("/Posts/$topic")

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

                if (allPosts.isNotEmpty()) {
                    // Set up the RecyclerView and adapter after retrieving posts
                    val recyclerView: RecyclerView = findViewById(R.id.forumRecycler)
                    recyclerView.layoutManager = LinearLayoutManager(this@TopicForum)
                    recyclerView.adapter = ChatAdapter(allPosts, this@TopicForum)
                    recyclerView.adapter?.notifyDataSetChanged()

                    // Show the RecyclerView and hide the "no post" message
                    binding.noPost.visibility = View.GONE
                    binding.forumRecycler.visibility = View.VISIBLE
                } else {
                    // Hide the RecyclerView and show the "no post" message
                    binding.noPost.visibility = View.VISIBLE
                    binding.forumRecycler.visibility = View.GONE
                }


            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("weightData", "Data retrieval cancelled: ${databaseError.message}")
            }
        })
    }

    private fun retrieveUserName(preferences: SharedPreferences): String {
        val existingUser = preferences.getBoolean("existingUser", false)
        val userDataJson = preferences.getString("userdata", null)
        val gson = Gson()
        val userData = gson.fromJson(userDataJson, UserDetails::class.java)
        return userData.name.toString()
    }
}
