package tech.ayodele.gravity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class InsightsViewModel : ViewModel() {
    private val _weeklySum = MutableLiveData<MetricsData>()
    val weeklySum: LiveData<MetricsData>
        get() = _weeklySum

    private val _insightPercentages = MutableLiveData<SavedInsightPercentage>()
    val insightPercentages: LiveData<SavedInsightPercentage>
        get() = _insightPercentages

    fun updateWeeklySum(weeklySum: MetricsData) {
        _weeklySum.value = weeklySum
    }

    fun updateInsightPercentages(insightPercentages: SavedInsightPercentage) {
        _insightPercentages.value = insightPercentages
    }
}
