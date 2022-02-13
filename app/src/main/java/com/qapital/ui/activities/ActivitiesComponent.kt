package com.qapital.ui.activities

import com.qapital.di.scope.FragmentScope
import dagger.Subcomponent

@FragmentScope
@Subcomponent
interface ActivitiesComponent {

    fun inject(activitiesFragment: ActivitiesFragment)

    @Subcomponent.Builder
    interface Builder {

        fun build(): ActivitiesComponent
    }
}