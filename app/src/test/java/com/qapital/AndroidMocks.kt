package com.qapital

import android.content.Context
import android.content.res.Resources
import android.text.Html
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.format.DateFormat
import android.text.format.DateUtils
import org.mockito.*
import org.powermock.api.mockito.PowerMockito
import java.util.*

class AndroidMocks {

    @Mock
    lateinit var mockApplicationContext: Context

    @Mock
    private lateinit var mockContextResources: Resources

    init {
        MockitoAnnotations.initMocks(this)

        PowerMockito.mockStatic(DateUtils::class.java)
        Mockito.`when`(
            DateUtils.getRelativeTimeSpanString(ArgumentMatchers.anyLong())
        ).thenReturn("Apr 25, 2020 13:37")

        PowerMockito.mockStatic(Html::class.java)
        Mockito.mock(SpannableStringBuilder::class.java)
        Mockito.`when`(
            Html.fromHtml(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyInt()
            )
        ).thenReturn(SpannableStringBuilder())
        Mockito.`when`(
            Html.fromHtml(
                ArgumentMatchers.anyString())
        ).thenReturn(SpannableStringBuilder())

        Mockito.`when`(mockApplicationContext.resources).thenReturn(mockContextResources)
        Mockito.`when`(
            mockContextResources.getQuantityString(
                Matchers.anyInt(),
                Matchers.anyInt(),
                Matchers.anyInt()
            )
        ).thenReturn("Some string with a quantity in it")
        Mockito.`when`(
            mockContextResources.getString(ArgumentMatchers.anyInt())
        ).thenReturn("Some string")
        Mockito.`when`(
            mockContextResources.getString(ArgumentMatchers.anyInt(), ArgumentMatchers.any())
        ).thenReturn("Some string with a parameter in it")
    }

}