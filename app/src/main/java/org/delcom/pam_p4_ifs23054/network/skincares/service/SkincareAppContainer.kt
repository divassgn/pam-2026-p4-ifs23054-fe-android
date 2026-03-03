package org.delcom.pam_p4_ifs23054.network.skincares.service

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import org.delcom.pam_p4_ifs23054.BuildConfig
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class SkincareAppContainer : ISkincareAppContainer {

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BASIC
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }

    // Trust manager yang menerima semua sertifikat (untuk dev/self-signed cert)
    private val trustAllCerts: Array<TrustManager> = arrayOf(
        object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}
            override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
            override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
        }
    )

    private val sslContext = SSLContext.getInstance("SSL").apply {
        init(null, trustAllCerts, SecureRandom())
    }

    val okHttpClient = OkHttpClient.Builder().apply {
        // Bypass SSL verification
        sslSocketFactory(sslContext.socketFactory, trustAllCerts[0] as X509TrustManager)
        hostnameVerifier { _, _ -> true }

        if (BuildConfig.DEBUG) {
            addInterceptor(loggingInterceptor)
        }

        connectTimeout(2, TimeUnit.MINUTES)
        readTimeout(2, TimeUnit.MINUTES)
        writeTimeout(2, TimeUnit.MINUTES)
    }.build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL_PANTS_API)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    private val retrofitService: SkincareApiService by lazy {
        retrofit.create(SkincareApiService::class.java)
    }

    override val skincareRepository: ISkincareRepository by lazy {
        SkincareRepository(retrofitService)
    }
}