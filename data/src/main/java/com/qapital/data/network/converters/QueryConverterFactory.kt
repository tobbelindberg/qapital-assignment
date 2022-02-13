package com.qapital.data.network.converters

import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class QueryConverterFactory(datePattern: String) : Converter.Factory() {

    private val dateQueryConverter = DateQueryConverter(datePattern)
    override fun stringConverter(type: Type, annotations: Array<Annotation>, retrofit: Retrofit): Converter<*, String>? {
        return if (type === Date::class.java) {
            dateQueryConverter
        } else null
    }

    private class DateQueryConverter(datePattern: String) : Converter<Date, String> {
        private val dateFormatter: ThreadLocal<DateFormat> = object : ThreadLocal<DateFormat>() {
            public override fun initialValue(): DateFormat {
                return SimpleDateFormat(datePattern, Locale.US)
            }
        }

        override fun convert(date: Date): String {
            return dateFormatter.get()!!.format(date)
        }
    }

    companion object {
        fun create(datePattern: String): QueryConverterFactory {
            return QueryConverterFactory(datePattern)
        }
    }
}