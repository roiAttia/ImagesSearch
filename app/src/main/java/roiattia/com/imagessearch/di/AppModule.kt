package roiattia.com.imagessearch.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import roiattia.com.imagessearch.data.repositories.ImageRepositoryImpl
import roiattia.com.imagessearch.data.repositories.ImagesRepository

@InstallIn(SingletonComponent::class)
@Module
abstract class AppModule {

    @Binds
    abstract fun provideImagesRepository(repository: ImageRepositoryImpl): ImagesRepository

}