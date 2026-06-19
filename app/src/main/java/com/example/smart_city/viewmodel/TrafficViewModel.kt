package com.example.smart_city.viewmodel



import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.smart_city.model.TrafficModel
import com.example.smart_city.repo.TrafficRepository

class TrafficViewModel(
    private val repository: TrafficRepository =
        TrafficRepository()
) : ViewModel() {

    var trafficList by mutableStateOf(
        emptyList<TrafficModel>()
    )

    fun fetchTraffic() {

        repository.getTrafficData { list ->

            android.util.Log.d(
                "TRAFFIC_DEBUG",
                "Traffic Size = ${list.size}"
            )

            trafficList = list
        }
    }
}