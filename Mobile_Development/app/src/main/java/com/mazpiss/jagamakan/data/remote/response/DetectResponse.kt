package com.mazpiss.jagamakan.data.remote.response

import com.google.gson.annotations.SerializedName

data class DetectResponse(

	@field:SerializedName("combined_info")
	val combinedInfo: CombinedInfo? = null,

	@field:SerializedName("combined_calories")
	val combinedCalories: String? = null,

	@field:SerializedName("total_calories")
	val totalCalories: Int? = null,

	@field:SerializedName("combined_quantity")
	val combinedQuantity: String,

	@field:SerializedName("image-url")
	val imageUrl: String? = null,

	@field:SerializedName("combined_name")
	val combinedName: String,

)

data class CombinedInfo(

	@field:SerializedName("telur_balado")
	val telurBalado: Int? = null,

	@field:SerializedName("tempe")
	val tempe: Int? = null,

	@field:SerializedName("mangkuk_bakso")
	val mangkukBakso: Int? = null
)
