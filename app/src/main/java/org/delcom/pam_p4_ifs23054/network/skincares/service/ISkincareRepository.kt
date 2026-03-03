package org.delcom.pam_p4_ifs23054.network.skincares.service

import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.delcom.pam_p4_ifs23054.network.data.ResponseMessage
import org.delcom.pam_p4_ifs23054.network.skincares.data.ResponseSkincare
import org.delcom.pam_p4_ifs23054.network.skincares.data.ResponseSkincareAdd
import org.delcom.pam_p4_ifs23054.network.skincares.data.ResponseSkincares
import org.delcom.pam_p4_ifs23054.network.skincares.data.ResponseProfile

interface ISkincareRepository {
    suspend fun getProfile(): ResponseMessage<ResponseProfile?>

    suspend fun getAllSkincares(search: String? = null): ResponseMessage<ResponseSkincares?>

    suspend fun postSkincare(
        nama: RequestBody,
        deskripsi: RequestBody,
        manfaat: RequestBody,
        efekSamping: RequestBody,
        file: MultipartBody.Part
    ): ResponseMessage<ResponseSkincareAdd?>

    suspend fun getSkincareById(skincareId: String): ResponseMessage<ResponseSkincare?>

    suspend fun putSkincare(
        skincareId: String,
        nama: RequestBody,
        deskripsi: RequestBody,
        manfaat: RequestBody,
        efekSamping: RequestBody,
        file: MultipartBody.Part? = null
    ): ResponseMessage<String?>

    suspend fun deleteSkincare(skincareId: String): ResponseMessage<String?>
}
