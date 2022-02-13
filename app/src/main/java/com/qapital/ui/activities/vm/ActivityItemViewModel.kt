package com.qapital.ui.activities.vm

import android.content.res.Resources
import android.os.Build
import android.text.Html
import android.text.format.DateFormat
import com.qapital.R
import com.qapital.base.vm.ItemViewModel
import com.qapital.data.model.RichActivity

class ActivityItemViewModel(val activity: RichActivity, resources: Resources) : ItemViewModel {

    val message = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(activity.activity.message, Html.FROM_HTML_MODE_COMPACT)
    } else {
        Html.fromHtml(activity.activity.message)
    }

    val owner = activity.activity.amount.toString()
    val updatedAt = resources.getString(
        R.string.last_updated,
        DateFormat.format("MMM d, yyyy HH:mm", activity.timestamp)
    )

    val starCount = "5"

    val avatarUrl = activity.user.avatarUrl

    override fun itemId(): Long {
        return activity.hashCode().toLong()
    }
}