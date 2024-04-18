package tech.ayodele.gravity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationBarView
import org.checkerframework.checker.units.qual.C
import tech.ayodele.gravity.databinding.ActivityCommunityBinding

class Community : AppCompatActivity(), CommunityAdapter.OnItemClickListener  {
    private lateinit var binding: ActivityCommunityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(0, 0)
        enableEdgeToEdge()
        setContentView(R.layout.activity_community)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
//        binding
        binding = ActivityCommunityBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Replace items with your list of data
        val items: List<CommunityItems> =
            listOf(
                CommunityItems(
                    "Exercise",
                    "Useful insights on weight loss exercises",
                    1
                ),
                CommunityItems(
                    "Diet",
                    "Discuss healthy meals and improving eating habits with others",
                    5
                ),
                        CommunityItems(
                        "Challenges and Struggles",
                "You're not alone, share your struggles with others",
                6),

                CommunityItems(
                    "Tips and Ideas",
                    "Suggestions on how to make the journey better",
                    22
                )
            )

// Recycler adapter
        val recyclerView: RecyclerView = findViewById(R.id.communityRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = CommunityAdapter(items, this)
        recyclerView.adapter = adapter




        //bottom nav
        val bottomNavigation = binding.bottomNavigation
        // Assuming bottomNavigationView is your BottomNavigationView reference
        bottomNavigation.selectedItemId = R.id.community

        bottomNavigation.setOnItemSelectedListener(object :
            NavigationBarView.OnItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.home -> {
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

                    R.id.dieting -> {
                        startActivity(Intent(this@Community, Diet::class.java))
                        return true
                    }

                    // Add more cases for other menu items as needed
                    else -> return false
                }
            }
        })
    }
    override fun onItemClick(item: CommunityItems) {
        val intent = Intent(this@Community, TopicForum::class.java)
        intent.putExtra("topic", item.topic)
        startActivity(intent)
        Log.i("item", item.toString())
    }

}