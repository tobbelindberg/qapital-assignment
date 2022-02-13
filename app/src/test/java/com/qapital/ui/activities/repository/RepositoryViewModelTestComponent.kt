package com.qapital.ui.activities.repository

import com.qapital.di.scope.FragmentScope
import com.qapital.domain.model.Activity
import dagger.BindsInstance
import dagger.Subcomponent

@FragmentScope
@Subcomponent
interface RepositoryViewModelTestComponent {

    fun inject(postDetailViewModelTest: RepositoryViewModelTest)

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun setRepository(repository: Activity): Builder

        fun build(): RepositoryViewModelTestComponent

    }

}