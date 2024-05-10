package tech.ayodele.gravity.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import tech.ayodele.gravity.model.CommunityItems

class CommunityViewModel : ViewModel() {

    fun getCommunityItems(callback: (List<CommunityItems>) -> Unit) {
        val items = mutableListOf<CommunityItems>()
        // Initialize the list of items
        val exerciseResources = "https://www.webmd.com/fitness-exercise/ss/slideshow-exercises-weightloss"
        val dietResources = "https://www.nutrition.org.uk/health-conditions/obesity-healthy-weight-loss-and-nutrition/"
        val challengesResources = "https://www.womenshealthnetwork.com/weight-loss/overcome-weight-loss-resistance/"
        val tipsAndIdeasResources = "https://www.nhs.uk/live-well/healthy-weight/managing-your-weight/tips-to-help-you-lose-weight/"
        items.add(CommunityItems("Exercise", "Useful insights on weight loss exercises", 0, exerciseResources))
        items.add(CommunityItems("Diet", "Discuss healthy meals and improving eating habits with others", 0, dietResources))
        items.add(CommunityItems("Challenges and Struggles", "You're not alone, share your struggles with others", 0, challengesResources))
        items.add(CommunityItems("Tips and Ideas", "Suggestions on how to make the journey better", 0, tipsAndIdeasResources))

        // Update post count for each item
        for (item in items) {
            getPostCount(item.topic) { count ->
                item.commentCount = count
                callback(items)
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
                // Handle error
            }
        })
    }
}
