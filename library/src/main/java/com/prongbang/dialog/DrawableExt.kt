package com.prongbang.dialog

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

fun Context?.getDrawableOrEmpty(@DrawableRes idDrawable: Int): Drawable? {
	return this?.let { ContextCompat.getDrawable(it, idDrawable) }
}