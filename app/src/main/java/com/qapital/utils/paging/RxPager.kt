package com.qapital.utils.paging

import com.qapital.base.state.State
import com.qapital.data.TimestampItem
import com.qapital.utils.twoWeeksAgo
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.*

class RxPager<E : TimestampItem, T : State>(onLoadNextPage: (Pair<Date, Date>) -> Observable<PagePartialState<E, T>>) {
    private var firstPage = generateFirstPageRange()
    private val pages = Pages(firstPageToLoad = firstPage, firstPage.first.twoWeeksAgo() to firstPage.first)

    private val pageSubject = PublishSubject.create<Pair<Date, Date>>().toSerialized()
    private var pageLoading = false

    val observable: Observable<PagePartialState<E, T>> = pageSubject.hide()
        .observeOn(Schedulers.io())
        .filter { !pageLoading }
        .doOnNext {
            pageLoading = true
        }
        .switchMap(onLoadNextPage)
        .doOnNext { partialPageState ->
            partialPageState.nextPage?.also { page ->
                pages.nextPageToLoad = pages.nextPageToLoad.first.twoWeeksAgo() to pages.nextPageToLoad.first
                pages.hasMorePages = pages.nextPageToLoad.second.after(pages.oldest)
            }
        }
        .doOnNext {
            pageLoading = it.nextPageLoading
        }
        .doOnNext { page ->
            if (page.nextPage?.isEmpty() == true) {
                onNextPage()
            }
        }

    private fun generateFirstPageRange(): Pair<Date, Date> {
        val toDate = Calendar.getInstance().time
        return toDate.twoWeeksAgo() to toDate
    }

    fun getFirstPage() = pages.firstPageToLoad

    fun onNextPage() {
        if (pageSubject.hasObservers() && pages.hasMorePages) {
            if (!pageLoading) {
                pageSubject.onNext(pages.nextPageToLoad)
            }
        }
    }

    fun setOldest(oldest: Date) {
        pages.oldest = oldest
    }

    fun reset() {
        firstPage = generateFirstPageRange()
        pages.nextPageToLoad = firstPage.first.twoWeeksAgo() to firstPage.first
        pages.hasMorePages = true
        pageLoading = false
    }
}