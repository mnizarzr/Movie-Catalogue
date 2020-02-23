package io.github.mnizarzr.moviecatalogue.data.model

data class ApiResponse(
    val page: Int = 0,
    val results: ArrayList<ItemResult> = arrayListOf(),
    val totalPages: Int = 0,
    val totalResults: Int = 0
)

