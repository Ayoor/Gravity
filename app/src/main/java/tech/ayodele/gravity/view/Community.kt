package tech.ayodele.gravity.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import tech.ayodele.gravity.CommunityAdapter
import tech.ayodele.gravity.R
import tech.ayodele.gravity.TopicForum
import tech.ayodele.gravity.databinding.ActivityCommunityBinding
import tech.ayodele.gravity.model.CommunityItems
import tech.ayodele.gravity.viewmodel.CommunityViewModel
import java.util.*

class Community : AppCompatActivity(), CommunityAdapter.OnItemClickListener {

    private lateinit var binding: ActivityCommunityBinding
    private lateinit var viewModel: CommunityViewModel
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

        // Initialize ViewModel
        viewModel = ViewModelProvider(this).get(CommunityViewModel::class.java)

        //bottom nav
        val bottomNavigation = binding.bottomNavigation
        bottomNavigation.selectedItemId = R.id.community
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.dashboard_home -> {
                    startActivity(Intent(this@Community, Dashboard::class.java))
                    finish()
                    return@setOnItemSelectedListener true
                }
                R.id.community -> true
                R.id.liveHelp -> {
                    startActivity(Intent(this@Community, LiveHelp::class.java))
                    finish()
                    return@setOnItemSelectedListener true
                }
                R.id.insightIcon -> {
                    startActivity(Intent(this@Community, InsightsActivity::class.java))
                    finish()
                    return@setOnItemSelectedListener true
                }
                R.id.dieting -> {
                    startActivity(Intent(this@Community, Diet::class.java))
                    finish()
                    return@setOnItemSelectedListener true
                }
                else -> false
            }
        }

        // Initialize the RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.communityRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = CommunityAdapter(emptyList(), this)
        recyclerView.adapter = adapter

        // Fetch community items
        viewModel.getCommunityItems { items ->
            adapter.updateItems(items)
        }
    }

    override fun onItemClick(item: CommunityItems) {
        val intent = Intent(this@Community, TopicForum::class.java)
        intent.putExtra("topic", item.topic)
        startActivity(intent)
    }
}
