package com.qapital.ui.activities.vm

import android.content.res.Resources
import android.os.Build
import android.text.Html
import android.text.format.DateFormat
import android.text.format.DateUtils
import com.qapital.R
import com.qapital.base.vm.ItemViewModel
import com.qapital.data.model.RichActivity

class ActivityItemViewModel(val activity: RichActivity) : ItemViewModel {

    val message = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(activity.activity.message, Html.FROM_HTML_MODE_COMPACT)
    } else {
        Html.fromHtml(activity.activity.message)
    }

    val timestamp = DateUtils.getRelativeTimeSpanString(activity.activity.timestamp.time)
    val amount = "$%.2f".format(activity.activity.amount)

    val avatarUrl = activity.user.avatarUrl

    override fun itemId(): Long {
        return activity.hashCode().toLong()
    }
}