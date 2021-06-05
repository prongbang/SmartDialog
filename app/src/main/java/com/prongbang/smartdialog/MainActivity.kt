package com.prongbang.smartdialog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.prongbang.dialog.SmartDialogFragment
import com.prongbang.dialog.SmartIcon

class MainActivity : AppCompatActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		initView()
	}

	private fun initView() {
		findViewById<Button>(R.id.showDialogButton).setOnClickListener {
			SmartDialogFragment.Builder(supportFragmentManager)
					.setIcon(SmartIcon.SUCCESS)
					.setTitle("SUCCESS")
					.setMessage("Camera granted, You can access camera to take a picture")
					.setPositiveButton("CLOSE") {
						Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT)
								.show()
					}
					.build()
					.show()
		}
	}
}