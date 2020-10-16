package com.prongbang.dialog

import androidx.annotation.IntDef

class SmartIcon {

	@IntDef(
			SUCCESS,
			ERROR,
			WARNING,
			NO_INTERNET,
			TIMEOUT,
			NO_DATA,
			CUSTOM,
			NO_ICON
	)
	@Retention(AnnotationRetention.SOURCE)
	annotation class Icon

	companion object {
		const val SUCCESS = 0
		const val ERROR = 1
		const val WARNING = 2
		const val NO_INTERNET = 3
		const val TIMEOUT = 4
		const val NO_DATA = 5
		const val CUSTOM = 6
		const val NO_ICON = 7
	}
}