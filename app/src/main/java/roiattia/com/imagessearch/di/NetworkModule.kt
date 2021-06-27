package roiattia.com.imagessearch.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import roiattia.com.imagessearch.utils.Constants.Network.BASE_URL
import roiattia.com.imagessearch.network.PixabayWebApi
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    @JvmStatic
    @Singleton
    @Provides
    fun provideRetrofitBuilder(converterFactory: Converter.Factory): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(converterFactory)
            .build()

    @JvmStatic
    @Singleton
    @Provides
    fun provideGson(): Gson = GsonBuilder().serializeNulls().setLenient().create()

    @JvmStatic
    @Singleton
    @Provides
    fun provideConverterFactory(gson: Gson): Converter.Factory = GsonConverterFactory.create(gson)

    @JvmStatic
    @Singleton
    @Provides
    fun providePixabayWebApi(retrofit: Retrofit): PixabayWebApi =
        retrofit.create(PixabayWebApi::class.java)

}