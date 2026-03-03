package org.delcom.pam_p4_ifs23054.network.skincares.data

import kotlinx.serialization.Serializable

@Serializable
data class ResponseSkincares(
    val skincares: List<ResponseSkincareData>
)

@Serializable
data class ResponseSkincare(
    val skincare: ResponseSkincareData
)

@Serializable
data class ResponseSkincareAdd(
    val skincareId: String
)

@Serializable
data class ResponseSkincareData(
    val id: String,
    val nama: String,
    val deskripsi: String,
    val manfaat: String,
    val efekSamping: String,
    val createdAt: String,
    val updatedAt: String
)
