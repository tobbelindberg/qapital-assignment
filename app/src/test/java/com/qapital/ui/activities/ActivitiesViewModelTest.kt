package com.qapital.ui.activities

import android.text.Html
import android.text.format.DateUtils
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
@PrepareForTest(DateUtils::class, Html::class)
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
        okHttpCountDownLatch = CountDownLatch(2)
        testAppModule = TestAppModule(
            androidMocks.mockApplicationContext,
            {
                okHttpCountDownLatch.countDown()
            },
            "/activities" to TestFileLoader.readJsonFileFromAssets("activities.json"),
            "/users/1" to TestFileLoader.readJsonFileFromAssets("user1.json"),
            "/users/2" to TestFileLoader.readJsonFileFromAssets("user2.json"),
            "/users/3" to TestFileLoader.readJsonFileFromAssets("user3.json"),
            "/users/6" to TestFileLoader.readJsonFileFromAssets("user6.json")
        )
        com.qapital.di.DaggerTestAppComponent.builder().testAppModule(testAppModule)
            .build().inject(this)
    }

    @After
    fun tearDown() {
        viewModelStore.clear()
        testAppModule.shutDownServer()
    }

    @Test
    fun testNumberOfActivities() {
        viewModel.initStateObservable()
        okHttpCountDownLatch.await()

        Assert.assertEquals(
            " The expected amount of activities does not match",
            14,
            viewModel.items.get()!!.size
        )
    }

    @Test
    fun testAmountAtIndexThree() {
        viewModel.initStateObservable()
        okHttpCountDownLatch.await()

        Assert.assertEquals(
            " The expected amount does not match",
            "$0.23",
            (viewModel.items.get()!![3] as ActivityItemViewModel).amount
        )
    }

    @Test
    fun testAvatarUrlAtIndexZero() {
        viewModel.initStateObservable()
        okHttpCountDownLatch.await()

        Assert.assertEquals(
            "The expected avatarUrl does not match",
            "http://qapital-ios-testtask.herokuapp.com/avatars/henrik.jpg",
            (viewModel.items.get()!![0] as ActivityItemViewModel).avatarUrl
        )
    }

    @Test
    fun testCorrectTimestampParsedAtIndexOne() {
        viewModel.initStateObservable()
        okHttpCountDownLatch.await()

        val expected = Calendar.getInstance().apply {
            set(2022, 1, 13, 1, 0, 0)
            timeZone = TimeZone.getTimeZone("CET")
        }

        val actual = Calendar.getInstance()
        actual.time = (viewModel.items.get()!![1] as ActivityItemViewModel).activity.timestamp


        Assert.assertTrue(
            "The expected timestamp was: ${expected.time}. but the actual was: ${actual.time}",
            expected.get(Calendar.YEAR) == actual.get(Calendar.YEAR)
                    && expected.get(Calendar.DAY_OF_YEAR) == actual.get(Calendar.DAY_OF_YEAR)
                    && expected.get(Calendar.HOUR_OF_DAY) == actual.get(Calendar.HOUR_OF_DAY)
                    && expected.get(Calendar.MINUTE) == actual.get(Calendar.MINUTE)
        )
    }

}