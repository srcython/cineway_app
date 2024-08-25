package com.srcython.cineway.utils.extensions

import android.text.Html
import android.text.Spanned

fun String.toSpannedText(): Spanned {
    return Html.fromHtml(this, Html.FROM_HTML_MODE_COMPACT)
}
