package com.example.smart_city

object ReportData {
    val wards = listOf(
        "Ward 1", "Ward 2", "Ward 3", "Ward 4",
        "Ward 5", "Ward 6", "Ward 7", "Ward 8"
    )
    val issueOptions = mapOf(
        "Garbage" to listOf(
            "Overflowing Bin",
            "Uncollected Waste",
            "Illegal Dumping",
            "Construction Waste",
            "Other"
        ),
        "Road" to listOf(
            "Pothole",
            "Broken Road",
            "Road Crack",
            "Drainage Damage",
            "Sidewalk Damage",
            "Other"
        ),
        "Traffic" to listOf(
            "Accident",
            "Congestion",
            "Signal Malfunction",
            "Illegal Parking",
            "Other"
        ),
        "Others" to listOf(
            "Street Light",
            "Water Supply",
            "Sewerage",
            "Public Property Damage",
            "Noise Complaint",
            "Other"
        )
    )
}
