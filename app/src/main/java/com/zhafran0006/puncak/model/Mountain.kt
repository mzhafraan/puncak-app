package com.zhafran0006.puncak.model

data class Mountain(
    val id: String,
    val name: String,
    val elevation: String,
    val location: String
)

object DataProvider {
    val mountains = listOf(
        Mountain("1", "Gunung Ciremai", "3.078 MDPL", "Via Apuy"),
        Mountain("2", "Gunung Pangradinan", "1.234 MDPL", "Jawa Barat")
    )
}