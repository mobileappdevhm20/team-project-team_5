package edu.hm.foodweek.util.amplify

import android.content.res.Resources
import edu.hm.foodweek.R
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FoodWeekClient {
    fun getFoodWeekServiceClient(): FoodWeekService {
        val backendApiClient = Retrofit.Builder()
            .baseUrl(Resources.getSystem().getString(R.string.backend_url))
            .addConverterFactory(GsonConverterFactory.create()).build()
        return backendApiClient.create(FoodWeekService::class.java)
    }
}