package com.qapital.ui.activities

import android.content.res.Resources
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.qapital.base.vm.BaseViewModel
import com.qapital.base.vm.ItemViewModel
import com.qapital.base.vm.LoadingFooterItemViewModel
import com.qapital.ui.activities.state.ActivitiesState
import com.qapital.ui.activities.vm.ActivityItemViewModel
import com.qapital.utils.ConsumableState
import com.qapital.utils.loge
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class ActivitiesViewModel
@Inject constructor(
    private val interactor: ActivitiesInteractor,
    private val resources: Resources
) : BaseViewModel() {

    val errorVisible = ObservableBoolean(false)
    val loadingVisible = ObservableBoolean(false)
    val swipeLoading = ObservableBoolean(false)
    val items = ObservableField<List<ItemViewModel>>(listOf())
    private val paginationError = MutableLiveData<ConsumableState<Throwable>>()
    val paginationErrorData: LiveData<ConsumableState<Throwable>>
        get() = paginationError

    override fun initializeSubscriptions() {
        interactor.stateObservable()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onNext = ::render, onError = { it.loge() })
            .addTo(subscriptions)
    }

    private fun render(state: ActivitiesState) {
        if (state.pageError != null) {
            state.pageError.loge()
        }

        val repositories = mutableListOf<ItemViewModel>()
        state.activities.mapTo(repositories) { repository ->
            ActivityItemViewModel(repository, resources)
        }
        if (state.nextPageLoading) {
            repositories.add(LoadingFooterItemViewModel)
        }

        items.set(repositories)

        loadingVisible.set(state.loading)

        swipeLoading.set(state.swipeLoading)

        errorVisible.set(state.pageError != null && !state.loading)

        state.nextPageError.consume {
            paginationError.value = ConsumableState.of(it)
        }
    }

    fun onRefresh(swipeRefreshing: Boolean = false) {
        interactor.onRefresh(swipeRefreshing)
    }

    fun onLoadNextPage() {
        interactor.onLoadNextPage()
    }
}


