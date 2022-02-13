package com.qapital.ui.activities

import com.qapital.base.state.PartialState
import com.qapital.data.model.RichActivity
import com.qapital.data.repositories.ActivitiesRepo
import com.qapital.data.repositories.UserRepo
import com.qapital.ui.activities.state.*
import com.qapital.utils.paging.PagePartialState
import com.qapital.utils.paging.RxPager
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.*
import javax.inject.Inject

class ActivitiesInteractor
@Inject constructor(
    private val activitiesRepo: ActivitiesRepo,
    private val userRepo: UserRepo
) {

    private val refresh = PublishSubject.create<Boolean>()
    private val pager = RxPager(::nextPageObservable)

    fun stateObservable(): Observable<ActivitiesState> {
        return Observable.merge(
            page(),
            refresh(),
            nextPage()
        )
            .scan(ActivitiesState(), ::reduce)
    }

    fun reduce(
        previousState: ActivitiesState,
        partialState: PartialState<ActivitiesState>
    ): ActivitiesState {
        return partialState.reduceState(previousState)
    }

    fun onRefresh(swipeRefreshing: Boolean) {
        refresh.onNext(swipeRefreshing)
    }

    private fun page(swipeRefreshing: Boolean = false): Observable<PartialState<ActivitiesState>> {
        val firstPageRange = pager.getFirstPage()
        return activitiesRepo.getActivities(firstPageRange.first, firstPageRange.second)
            .subscribeOn(Schedulers.io())
            .doOnNext { pager.setOldest(it.oldest) }
            .concatMapIterable { it.activities }
            .concatMap { activity ->
                userRepo.getUser(activity.userId)
                    .map { user ->
                        RichActivity(activity, user, activity.timestamp)
                    }
            }
            .toList().toObservable()
            .doOnNext {
                if (it.isEmpty()) {
                    pager.onNextPage()
                }
            }
            .map<PartialState<ActivitiesState>> { PageLoaded(it) }
            .onErrorReturn { PageError(it) }
            .startWithItem(PageLoading(swipeRefreshing))
    }

    private fun refresh(): Observable<PartialState<ActivitiesState>> {
        return refresh.switchMap { swipeRefreshing ->
            pager.reset()
            page(swipeRefreshing)
        }
    }

    fun onLoadNextPage() {
        pager.onNextPage()
    }

    private fun nextPage(): @NonNull Observable<out PartialState<ActivitiesState>> {
        return pager.observable
    }

    private fun nextPageObservable(range: Pair<Date, Date>): Observable<PagePartialState<RichActivity, ActivitiesState>> {
        return activitiesRepo.getActivities(range.first, range.second)
            .subscribeOn(Schedulers.io())
            .concatMapIterable { it.activities }
            .concatMap { activity ->
                userRepo.getUser(activity.userId)
                    .map { user ->
                        RichActivity(activity, user, activity.timestamp)
                    }
            }
            .toList().toObservable()
            .map<PagePartialState<RichActivity, ActivitiesState>> { NextPageLoaded(it) }
            .onErrorReturn { NextPageError(it) }
            .startWithItem(NextPageLoading())
    }

}
