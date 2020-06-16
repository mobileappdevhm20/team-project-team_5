package edu.hm.foodweek.util.amplify

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class FoodWeekClient {
    fun getFoodWeekServiceClient(): FoodWeekService {
        val backendApiClient = Retrofit.Builder()
            .baseUrl("http://foodweek-env.eba-qy49mda5.eu-central-1.elasticbeanstalk.com")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create()).build()
        return backendApiClient.create(FoodWeekService::class.java)
    }
}