package com.example.jejakrempah.data

import com.google.gson.annotations.SerializedName

data class JejakRempah(

	@field:SerializedName("JejakRempah")
	val jejakRempah: List<JejakRempahItem>
)

data class JejakRempahItem(

	@field:SerializedName("Rempah")
	val rempah: String,

	@field:SerializedName("ImageURLs")
	val imageURLs: List<String>,

	@field:SerializedName("Manfaat")
	val manfaat: String,

	@field:SerializedName("Funfact 2")
	val funfact2: String,

	@field:SerializedName("Funfact 1")
	val funfact1: String,

	@field:SerializedName("No ")
	val no: Int,

	@field:SerializedName("Link")
	val link: String
)
