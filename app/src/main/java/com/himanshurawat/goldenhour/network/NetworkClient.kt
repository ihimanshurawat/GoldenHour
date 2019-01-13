package com.himanshurawat.goldenhour.network

import com.himanshurawat.goldenhour.utils.Constant
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkClient {

    companion object {
        private var INSTANCE: Retrofit? = null

        public fun getNetworkClient(): Retrofit{
            if(INSTANCE == null){
                INSTANCE = Retrofit.Builder().
                    addConverterFactory(GsonConverterFactory.create()).
                    baseUrl(Constant.BASE_URL).build()
            }

            return INSTANCE as Retrofit
        }
    }
}