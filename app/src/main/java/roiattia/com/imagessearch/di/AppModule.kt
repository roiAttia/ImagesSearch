package roiattia.com.imagessearch.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import roiattia.com.imagessearch.core.Constants.SharedPreferences.FILE_NAME
import roiattia.com.imagessearch.data.repositories.ImageRepositoryImpl
import roiattia.com.imagessearch.data.repositories.ImagesRepository
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class AppModule {

    @Binds
    abstract fun provideImagesRepository(repository: ImageRepositoryImpl): ImagesRepository

    companion object {

        @Singleton
        @JvmStatic
        @Provides
        fun providePreferences(@ApplicationContext appContext: Context): SharedPreferences =
            appContext.getSharedPreferences(
                FILE_NAME,
                Context.MODE_PRIVATE
            )
    }

}