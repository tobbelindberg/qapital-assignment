package com.qapital.ui.activities.repository

import android.text.format.DateFormat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import com.qapital.RxRule
import com.qapital.TestFileLoader
import com.qapital.di.GenericViewModelFactory
import com.qapital.di.TestAppModule
import com.qapital.domain.model.User
import com.qapital.domain.model.Activity
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
class RepositoryViewModelTest {

    @Rule
    @JvmField
    val rxRules = RxRule()

    val androidMocks = com.qapital.AndroidMocks()

    private var okHttpCountDownLatch: CountDownLatch = CountDownLatch(1)

    private lateinit var testAppModule: TestAppModule

    @Inject
    lateinit var viewModelFactory: GenericViewModelFactory<RepositoryViewModel>

    private val viewModelStore = ViewModelStore()
    private val viewModel: RepositoryViewModel by lazy {
        ViewModelProvider(viewModelStore, viewModelFactory)[RepositoryViewModel::class.java]
    }

    @Before
    fun setup() {

        okHttpCountDownLatch = CountDownLatch(1)
        testAppModule = TestAppModule(
            androidMocks.mockApplicationContext,
            Runnable {
                okHttpCountDownLatch.countDown()
            },
            "/repos/freeCodeCamp/freeCodeCamp/pulls?sort=updated&direction=desc&state=all" to TestFileLoader.readJsonFileFromAssets(
                "pull_requests.json"
            )
        )
        com.qapital.di.DaggerTestAppComponent.builder().appModule(testAppModule).build()
            .repositoryViewModelTestBuilder()
            .setRepository(
                Activity(
                    28457823,
                    "freeCodeCamp",
                    "freeCodeCamp",
                    310313,
                    23985,
                    310313,
                    245,
                    "JavaScript",
                    Calendar.getInstance().time
                )
            ).build().inject(this)
    }

    @After
    fun tearDown() {
        viewModelStore.clear()
        testAppModule.shutDownServer()
    }

    @Test
    fun testAmountOfPullRequests() {
        viewModel.initStateObservable()
        okHttpCountDownLatch.await()

        Assert.assertEquals(
            " The expected amount of pull requests does not match",
            30,
            viewModel.items.get()!!.size
        )
    }

    @Test
    fun testUserNameAtIndexThree() {
        viewModel.initStateObservable()
        okHttpCountDownLatch.await()

        Assert.assertEquals(
            " The expected user name does not match",
            "cslv",
            viewModel.items.get()!![3].userName
        )
    }

    @Test
    fun testTitleAtIndexZero() {
        viewModel.initStateObservable()
        okHttpCountDownLatch.await()

        Assert.assertEquals(
            "The expected title does not match",
            "Update README.md",
            viewModel.items.get()!![0].title
        )
    }

    @Test
    fun testPRStateAtIndexTwo() {
        viewModel.initStateObservable()
        okHttpCountDownLatch.await()

        Assert.assertEquals(
            "The expected PR state name does not match",
            User.State.CLOSED,
            viewModel.items.get()!![2].pullRequest.state
        )
    }

}