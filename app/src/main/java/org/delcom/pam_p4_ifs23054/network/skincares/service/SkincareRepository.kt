package org.delcom.pam_p4_ifs23054.network.skincares.service

import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.delcom.pam_p4_ifs23054.helper.SuspendHelper
import org.delcom.pam_p4_ifs23054.network.data.ResponseMessage
import org.delcom.pam_p4_ifs23054.network.skincares.data.ResponseSkincare
import org.delcom.pam_p4_ifs23054.network.skincares.data.ResponseSkincareAdd
import org.delcom.pam_p4_ifs23054.network.skincares.data.ResponseSkincares
import org.delcom.pam_p4_ifs23054.network.skincares.data.ResponseProfile

class SkincareRepository(private val skincareApiService: SkincareApiService) : ISkincareRepository {

    override suspend fun getProfile(): ResponseMessage<ResponseProfile?> {
        return SuspendHelper.safeApiCall { skincareApiService.getProfile() }
    }

    override suspend fun getAllSkincares(search: String?): ResponseMessage<ResponseSkincares?> {
        return SuspendHelper.safeApiCall { skincareApiService.getAllSkincares(search) }
    }

    override suspend fun postSkincare(
        nama: RequestBody,
        deskripsi: RequestBody,
        manfaat: RequestBody,
        efekSamping: RequestBody,
        file: MultipartBody.Part
    ): ResponseMessage<ResponseSkincareAdd?> {
        return SuspendHelper.safeApiCall {
            skincareApiService.postSkincare(
                nama = nama,
                deskripsi = deskripsi,
                manfaat = manfaat,
                efekSamping = efekSamping,
                file = file
            )
        }
    }

    override suspend fun getSkincareById(skincareId: String): ResponseMessage<ResponseSkincare?> {
        return SuspendHelper.safeApiCall { skincareApiService.getSkincareById(skincareId) }
    }

    override suspend fun putSkincare(
        skincareId: String,
        nama: RequestBody,
        deskripsi: RequestBody,
        manfaat: RequestBody,
        efekSamping: RequestBody,
        file: MultipartBody.Part?
    ): ResponseMessage<String?> {
        return SuspendHelper.safeApiCall {
            skincareApiService.putSkincare(
                skincareId = skincareId,
                nama = nama,
                deskripsi = deskripsi,
                manfaat = manfaat,
                efekSamping = efekSamping,
                file = file
            )
        }
    }

    override suspend fun deleteSkincare(skincareId: String): ResponseMessage<String?> {
        return SuspendHelper.safeApiCall { skincareApiService.deleteSkincare(skincareId) }
    }
}
