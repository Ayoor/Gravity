package tech.ayodele.gravity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import tech.ayodele.gravity.databinding.ActivityCommunityBinding

class Community : AppCompatActivity(), CommunityAdapter.OnItemClickListener {

    private lateinit var binding: ActivityCommunityBinding
    private val items: MutableList<CommunityItems> = mutableListOf()
    private lateinit var adapter: CommunityAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(0, 0)
        enableEdgeToEdge()
        binding = ActivityCommunityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //bottom nav


        val bottomNavigation = binding.bottomNavigation
        bottomNavigation.selectedItemId = R.id.community
        bottomNavigation.setOnItemSelectedListener(object :
            NavigationBarView.OnItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.dashboard_home -> {
                        startActivity(Intent(this@Community, Dashboard::class.java))
                        finish()
                        return true
                    }

                    R.id.community -> {

                        return true
                    }

                    R.id.liveHelp -> {
                        startActivity(Intent(this@Community, LiveHelp::class.java))
                        finish()
                        return true
                    }

                    R.id.insightIcon -> {
                        startActivity(Intent(this@Community, Insights::class.java))
                        finish()
                        return true
                    }

                    R.id.dieting -> {
                        startActivity(Intent(this@Community, Diet::class.java))
                        finish()
                        return true
                    }
                    // Add more cases for other menu items as needed
                    else -> return false
                }
            }
        })

        // Initialize the RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.communityRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = CommunityAdapter(items, this)
        recyclerView.adapter = adapter

        // Initialize the list of items
        initCommunityItems()
    }

    override fun onItemClick(item: CommunityItems) {
        val intent = Intent(this@Community, TopicForum::class.java)
        intent.putExtra("topic", item.topic)
        startActivity(intent)
    }

    private fun initCommunityItems() {
        items.clear()
        items.add(CommunityItems("Exercise", "Useful insights on weight loss exercises", 0))
        items.add(CommunityItems("Diet", "Discuss healthy meals and improving eating habits with others", 0))
        items.add(CommunityItems("Challenges and Struggles", "You're not alone, share your struggles with others", 0))
        items.add(CommunityItems("Tips and Ideas", "Suggestions on how to make the journey better", 0))

        // Update post count for each item
        for (item in items) {
            getPostCount(item.topic) { count ->
                item.commentCount = count
                Log.d("PostCount", "The count for topic ${item.topic} is $count")
                adapter.notifyDataSetChanged() // Notify the adapter that data has changed
            }
        }
    }

    private fun getPostCount(topic: String, callback: (Int) -> Unit) {
        val database = FirebaseDatabase.getInstance()
        val databaseReference = database.getReference("/Posts/$topic")
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val count = dataSnapshot.childrenCount.toInt()
                callback(count)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("getPostCount", "Data retrieval cancelled: ${databaseError.message}")
            }
        })
    }
}
