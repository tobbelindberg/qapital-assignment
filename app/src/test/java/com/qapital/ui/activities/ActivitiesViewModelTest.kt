package com.qapital.ui.activities

import android.text.format.DateFormat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import com.qapital.AndroidMocks
import com.qapital.RxRule
import com.qapital.TestFileLoader
import com.qapital.di.GenericViewModelFactory
import com.qapital.di.TestAppModule
import com.qapital.ui.activities.vm.ActivityItemViewModel
import org.junit.*
import org.junit.runner.RunWith
import org.powermock.core.classloader.annotations.PowerMockIgnore
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import java.util.*
import java.util.concurrent.CountDownLatch
import javax.inject.Inject


@RunWith(PowerMockRunner::class)
@PrepareForTest(DateFormat::class)
@PowerMockIgnore("javax.net.ssl.*")
class ActivitiesViewModelTest {

    @Rule
    @JvmField
    val rxRules = RxRule()

    val androidMocks = AndroidMocks()

    private var okHttpCountDownLatch: CountDownLatch = CountDownLatch(1)

    private lateinit var testAppModule: TestAppModule

    @Inject
    lateinit var viewModelFactory: GenericViewModelFactory<ActivitiesViewModel>

    private val viewModelStore = ViewModelStore()
    private val viewModel: ActivitiesViewModel by lazy {
        ViewModelProvider(viewModelStore, viewModelFactory)[ActivitiesViewModel::class.java]
    }

    @Before
    fun setup() {
        okHttpCountDownLatch = CountDownLatch(1)
        testAppModule = TestAppModule(
            androidMocks.mockApplicationContext,
            {
                okHttpCountDownLatch.countDown()
            },
            "/search/repositories?o=desc&s=stars&q=stars:%3E=60000&page=1&per_page=20" to TestFileLoader.readJsonFileFromAssets(
                "top_repositories.json"
            )
        )
        com.qapital.di.DaggerTestAppComponent.builder().appModule(testAppModule).build().inject(this)
    }

    @After
    fun tearDown() {
        viewModelStore.clear()
        testAppModule.shutDownServer()
    }

    @Test
    fun testAmountOfRepositories() {
        viewModel.initStateObservable()
        okHttpCountDownLatch.await()

        Assert.assertEquals(
            " The expected amount of repositories does not match",
            20,
            viewModel.items.get()!!.size
        )
    }

    @Test
    fun testStarCountAtIndexThree() {
        viewModel.initStateObservable()
        okHttpCountDownLatch.await()

        Assert.assertEquals(
            " The expected amount of stars does not match",
            "205545",
            (viewModel.items.get()!![3] as ActivityItemViewModel).starCount
        )
    }

    @Test
    fun testOwnerAtIndexZero() {
        viewModel.initStateObservable()
        okHttpCountDownLatch.await()

        Assert.assertEquals(
            "The expected owner does not match",
            "freeCodeCamp",
            (viewModel.items.get()!![0] as ActivityItemViewModel).owner
        )
    }

    @Test
    fun testRepositoryNameAtIndexTwo() {
        viewModel.initStateObservable()
        okHttpCountDownLatch.await()

        Assert.assertEquals(
            "The expected repository name does not match",
            "free-programming-books",
            (viewModel.items.get()!![2] as ActivityItemViewModel).title
        )
    }

    @Test
    fun testCorrectDateParsedAtIndexOne() {
        viewModel.initStateObservable()
        okHttpCountDownLatch.await()

        val expected = Calendar.getInstance().apply {
            set(2022, 0, 31, 12, 52, 45)
            timeZone = TimeZone.getTimeZone("CET")
        }

        val actual = Calendar.getInstance()
        actual.time = (viewModel.items.get()!![1] as ActivityItemViewModel).activity.updatedAt


        Assert.assertTrue(
            "The expected updated at was: ${expected.time}. but the actual was: ${actual.time}",
            expected.get(Calendar.YEAR) == actual.get(Calendar.YEAR)
                    && expected.get(Calendar.DAY_OF_YEAR) == actual.get(Calendar.DAY_OF_YEAR)
                    && expected.get(Calendar.HOUR_OF_DAY) == actual.get(Calendar.HOUR_OF_DAY)
                    && expected.get(Calendar.MINUTE) == actual.get(Calendar.MINUTE)
        )
    }

}