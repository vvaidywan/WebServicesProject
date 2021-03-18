package com.example.webservicesproject.retrofit

import com.example.webservicesproject.MyApplication
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitApiClient {

    private const val BASE_URL = "https://storage.googleapis.com/network-security-conf-codelab.appspot.com/"
    private const val TAG = "APIClient"
    private const val HEADER_CACHE_CONTROL = "Cache-Control"
    private const val HEADER_PRAGMA = "Pragma"
    private const val cacheSize = (5 * 1024 * 1024).toLong()

    /**
     * return client
     */
    val getClient: RetrofitApiInterface
        get() {
            return retrofit().create(RetrofitApiInterface::class.java)
        }

    /**
     * Prepare and return Gson
     */
    private fun gson(): Gson {
        return GsonBuilder()
            .setLenient()
            .create()
    }

    /**
     * Prepare and return retrofit
     */
    private fun retrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client())
            .addConverterFactory(GsonConverterFactory.create(gson()))
            .build()
    }

    /**
     * Prepare and return OkHttpClient
     */
    private fun client(): OkHttpClient {
        return OkHttpClient.Builder()
            .cache(myCache())
            .addInterceptor(httpLoggingInterceptor())
            .addInterceptor(offlineInterceptor())
            .addInterceptor(networkInterceptor())
            .build()
    }

    /**
     * This interceptor will be called both if the network is available and if the network is not available
     * @return offlineInterceptor
     */
    private fun offlineInterceptor(): Interceptor {
        return Interceptor { chain ->
            var request = chain.request()

            // prevent caching when network is on. For that we use the "networkInterceptor"
            if (!MyApplication().hasNetwork()) {
                val cacheControl = CacheControl.Builder()
                    .maxStale(7, TimeUnit.DAYS)
                    .build()

                request = request.newBuilder()
                    .removeHeader(HEADER_CACHE_CONTROL)
                    .removeHeader(HEADER_PRAGMA)
                    .cacheControl(cacheControl)
                    .build()
            }
            chain.proceed(request)
        }
    }

    /**
     * This interceptor will be called ONLY if the network is available
     * @return networkInterceptor
     */
    private fun networkInterceptor(): Interceptor {
        return Interceptor { chain ->
            val response = chain.proceed(chain.request())

            val cacheControl = CacheControl.Builder()
                .maxAge(5, TimeUnit.SECONDS)
                .build()

            response.newBuilder()
                .removeHeader(HEADER_CACHE_CONTROL)
                .header(HEADER_CACHE_CONTROL, cacheControl.toString())
                .removeHeader(HEADER_PRAGMA)
                .build()
        }
    }

    /**
     * prepare and return cache
     */
    private fun myCache(): Cache {
        return Cache(MyApplication.instance!!.cacheDir, cacheSize)
    }

    /**
     * httpLoggingInterceptor instance
     */
    private fun httpLoggingInterceptor(): Interceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }
}