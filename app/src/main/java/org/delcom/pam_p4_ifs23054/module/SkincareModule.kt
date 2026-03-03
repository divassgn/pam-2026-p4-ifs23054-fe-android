package org.delcom.pam_p4_ifs23054.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.delcom.pam_p4_ifs23054.network.skincares.service.ISkincareAppContainer
import org.delcom.pam_p4_ifs23054.network.skincares.service.ISkincareRepository
import org.delcom.pam_p4_ifs23054.network.skincares.service.SkincareAppContainer
import org.delcom.pam_p4_ifs23054.network.skincares.service.SkincareRepository

@Module
@InstallIn(SingletonComponent::class)
object SkincareModule {
    @Provides
    fun provideSkincareContainer(): ISkincareAppContainer {
        return SkincareAppContainer()
    }

    @Provides
    fun provideSkincareRepository(container: ISkincareAppContainer): ISkincareRepository {
        return container.skincareRepository
    }
}