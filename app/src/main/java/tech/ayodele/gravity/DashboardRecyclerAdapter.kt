package tech.ayodele.gravity

import DashboardViewHolder
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import tech.ayodele.gravity.databinding.DashboardItemsBinding

class DashboardRecyclerAdapter(private val dashboarddata: DashboardData) : RecyclerView.Adapter<DashboardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DashboardItemsBinding.inflate(inflater, parent, false)
        return DashboardViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return 1
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
//        val item = dashboarddata[position]
//        holder.binding.name.text = item.name
//        holder.binding.quote.text = item.quote
        // Bind other data as needed

        val binding = holder.binding
        val context = binding.root.context

        // Set progress for each indicator
        binding.waterProgressIndicator.setProgress(dashboarddata.waterProgress, true)
        binding.stepsProgressIndicator.setProgress(dashboarddata.stepsProgress, true)
        binding.caloryProgressIndicator.setProgress(dashboarddata.caloryProgress, true)
        binding.caloryProgressIndicator2.setProgress(dashboarddata.caloryProgress2, true)
        binding.exerciseProgressIndicator.setProgress(dashboarddata.exerciseProgress, true)
        binding.exerciseProgressIndicator2.setProgress(dashboarddata.exerciseProgress2, true)

        // Set colors for indicators
        val whiteColor = ContextCompat.getColor(context, R.color.white)
        val purpleColor = ContextCompat.getColor(context, R.color.purple)
        binding.caloryProgressIndicator.trackColor = whiteColor
        binding.caloryProgressIndicator.setIndicatorColor(purpleColor)
        binding.caloryProgressIndicator2.trackColor = whiteColor
        binding.caloryProgressIndicator2.setIndicatorColor(purpleColor)
        binding.exerciseProgressIndicator.trackColor = whiteColor
        binding.exerciseProgressIndicator.setIndicatorColor(purpleColor)
        binding.exerciseProgressIndicator2.trackColor = whiteColor
        binding.exerciseProgressIndicator2.setIndicatorColor(purpleColor)

        // Set user name
        binding.name.text = dashboarddata.userName
    }


}