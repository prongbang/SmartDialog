package com.prongbang.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.FrameLayout
import androidx.annotation.DrawableRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import kotlinx.android.synthetic.main.dialog_fragment_smart.*

class SmartDialogFragment : DialogFragment(), DialogInterface.OnKeyListener {

	@SmartIcon.Icon
	private var iconType: Int = SmartIcon.NO_ICON

	@DrawableRes
	private var iconDrawable: Int = 0

	private var title: String? = null
	private var message: String? = null
	private var messageSecondary: String? = null
	private var positiveButtonText: String? = null
	private var negativeButtonText: String? = null
	private var onPositiveButtonClick: () -> Unit = {}
	private var onNegativeButtonClick: () -> Unit = {}
	private var supportFragmentManager: FragmentManager? = null

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
	                          savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.dialog_fragment_smart, container, false)
	}

	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
		return activity?.let { activity ->
			val view: View = FrameLayout(activity)
			view.layoutParams = FrameLayout.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.MATCH_PARENT
			)
			Dialog(activity).apply {
				window?.requestFeature(Window.FEATURE_NO_TITLE)
				window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
				window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
				setContentView(view)
				setOnKeyListener(this@SmartDialogFragment)
				setCanceledOnTouchOutside(false)
			}
		} ?: super.onCreateDialog(savedInstanceState)
	}

	override fun onKey(dialog: DialogInterface?, keyCode: Int, event: KeyEvent?): Boolean {
		return keyCode == KeyEvent.KEYCODE_BACK && event?.action == KeyEvent.ACTION_UP
	}

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)
		initArgument()
		initView()
	}

	private fun initArgument() {
		arguments?.let {
			iconType = it.getInt(ICON_KEY)
			iconDrawable = it.getInt(ICON_DRAWABLE_KEY)
			title = it.getString(TITLE_KEY)
			message = it.getString(MESSAGE_KEY)
			messageSecondary = it.getString(MESSAGE_SECONDARY_KEY)
			positiveButtonText = it.getString(POSITIVE_BUTTON_KEY)
			negativeButtonText = it.getString(NEGATIVE_BUTTON_KEY)
		}
	}

	private fun initView() {
		processVisibility()
		mappingIcon()
		updateText()
		addListener()
	}

	private fun processVisibility() {
		val isVisibility: (Any?) -> Int = { value ->
			if (value == null) View.GONE else View.VISIBLE
		}

		val isNoIcon: (Int) -> Int = { value ->
			if (value == SmartIcon.NO_ICON) View.GONE else View.VISIBLE
		}

		view?.apply {
			iconImage.visibility = isNoIcon(iconType)
			titleText.visibility = isVisibility(title)
			messageText.visibility = isVisibility(message)
			messageSecondaryText.visibility = isVisibility(messageSecondary)
			positiveButton.visibility = isVisibility(positiveButtonText)
			negativeButton.visibility = isVisibility(negativeButtonText)
		}
	}

	private fun mappingIcon() {
		view?.apply {
			when (iconType) {
				SmartIcon.SUCCESS -> {
					iconImage.setImageDrawable(
							context.getDrawableOrEmpty(R.drawable.ic_success))
				}
				SmartIcon.ERROR, SmartIcon.WARNING, SmartIcon.TIMEOUT, SmartIcon.NO_INTERNET -> {
					iconImage.setImageDrawable(
							context.getDrawableOrEmpty(R.drawable.ic_error))
				}
				SmartIcon.NO_DATA -> {
					iconImage.setImageDrawable(
							context.getDrawableOrEmpty(R.drawable.ic_no_data))
				}
				SmartIcon.CUSTOM -> {
					if (iconDrawable != 0) {
						iconImage.setImageDrawable(context.getDrawableOrEmpty(iconDrawable))
					}
				}
			}
		}
	}

	private fun updateText() {
		view?.apply {
			titleText.text = title ?: ""
			messageText.text = message ?: ""
			messageSecondaryText.text = messageSecondary ?: ""
			positiveButton.text = positiveButtonText ?: ""
			negativeButton.text = negativeButtonText ?: ""
		}
	}

	private fun addListener() {
		view?.apply {
			positiveButton.setOnClickListener {
				onPositiveButtonClick.invoke()
				dismiss()
			}

			negativeButton.setOnClickListener {
				onNegativeButtonClick.invoke()
				dismiss()
			}
		}
	}

	override fun show(manager: FragmentManager, tag: String?) {
		try {
			manager.beginTransaction()
					.remove(this)
					.commit()
			super.show(manager, tag)
		} catch (e: Exception) {
			Log.e("SmartDialogFragment", "${e.message}")
		}
	}

	fun show() {
		supportFragmentManager?.let { show(it, SmartDialogFragment::class.java.simpleName) }
	}

	class Builder(private val fragmentManager: FragmentManager) {
		private var arguments: Bundle = Bundle()
		private var onPositiveButtonClickListener: () -> Unit = {}
		private var onNegativeButtonClickListener: () -> Unit = {}

		fun setIcon(@SmartIcon.Icon icon: Int): Builder {
			arguments.putInt(ICON_KEY, icon)
			return this
		}

		fun setIconDrawable(@DrawableRes icon: Int): Builder {
			arguments.putInt(ICON_DRAWABLE_KEY, icon)
			return this
		}

		fun setTitle(title: CharSequence?): Builder {
			arguments.putCharSequence(TITLE_KEY, title)
			return this
		}

		fun setMessage(message: CharSequence?): Builder {
			arguments.putCharSequence(MESSAGE_KEY, message)
			return this
		}

		fun setMessageSecondary(message: CharSequence?): Builder {
			arguments.putCharSequence(MESSAGE_SECONDARY_KEY, message)
			return this
		}

		fun setPositiveButton(text: CharSequence?, onClickListener: () -> Unit = {}): Builder {
			arguments.putCharSequence(POSITIVE_BUTTON_KEY, text)
			onPositiveButtonClickListener = {
				onClickListener.invoke()
			}
			return this
		}

		fun setNegativeButton(text: CharSequence?, onClickListener: () -> Unit = {}): Builder {
			arguments.putCharSequence(NEGATIVE_BUTTON_KEY, text)
			onNegativeButtonClickListener = {
				onClickListener.invoke()
			}
			return this
		}

		fun build(): SmartDialogFragment {
			val fragment = SmartDialogFragment()
			fragment.supportFragmentManager = fragmentManager
			fragment.onPositiveButtonClick = {
				onPositiveButtonClickListener.invoke()
			}
			fragment.onNegativeButtonClick = {
				onNegativeButtonClickListener.invoke()
			}
			fragment.arguments = arguments
			return fragment
		}
	}

	companion object {
		private const val ICON_KEY = "icon"
		private const val ICON_DRAWABLE_KEY = "icon_drawable"
		private const val TITLE_KEY = "title"
		private const val MESSAGE_KEY = "message"
		private const val MESSAGE_SECONDARY_KEY = "message_secondary"
		private const val POSITIVE_BUTTON_KEY = "positive_button"
		private const val NEGATIVE_BUTTON_KEY = "negative_button"
	}
}