package org.delcom.pam_p4_ifs23054.network.skincares.service

import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.delcom.pam_p4_ifs23054.network.data.ResponseMessage
import org.delcom.pam_p4_ifs23054.network.skincares.data.ResponseSkincare
import org.delcom.pam_p4_ifs23054.network.skincares.data.ResponseSkincareAdd
import org.delcom.pam_p4_ifs23054.network.skincares.data.ResponseSkincares
import org.delcom.pam_p4_ifs23054.network.skincares.data.ResponseProfile
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface SkincareApiService {
    // Ambil profile developer
    @GET("profile")
    suspend fun getProfile(): ResponseMessage<ResponseProfile?>

    // Ambil semua data skincare
    @GET("skincares")
    suspend fun getAllSkincares(
        @Query("search") search: String? = null
    ): ResponseMessage<ResponseSkincares?>

    // Tambah data skincare
    @Multipart
    @POST("/skincares")
    suspend fun postSkincare(
        @Part("nama") nama: RequestBody,
        @Part("deskripsi") deskripsi: RequestBody,
        @Part("manfaat") manfaat: RequestBody,
        @Part("efekSamping") efekSamping: RequestBody,
        @Part file: MultipartBody.Part
    ): ResponseMessage<ResponseSkincareAdd?>

    // Ambil data skincare berdasarkan ID
    @GET("skincares/{skincareId}")
    suspend fun getSkincareById(
        @Path("skincareId") skincareId: String
    ): ResponseMessage<ResponseSkincare?>

    // Ubah data skincare
    @Multipart
    @PUT("/skincares/{skincareId}")
    suspend fun putSkincare(
        @Path("skincareId") skincareId: String,
        @Part("nama") nama: RequestBody,
        @Part("deskripsi") deskripsi: RequestBody,
        @Part("manfaat") manfaat: RequestBody,
        @Part("efekSamping") efekSamping: RequestBody,
        @Part file: MultipartBody.Part? = null
    ): ResponseMessage<String?>

    // Hapus data skincare
    @DELETE("skincares/{skincareId}")
    suspend fun deleteSkincare(
        @Path("skincareId") skincareId: String
    ): ResponseMessage<String?>
}
