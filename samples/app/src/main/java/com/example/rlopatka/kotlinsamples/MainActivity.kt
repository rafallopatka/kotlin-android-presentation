package com.example.rlopatka.kotlinsamples

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import android.R.attr.duration
import android.widget.Toast



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // implicit property for java getters and setters
        txtTest.text = getString(R.string.TxtTest)

        // access layout elements directly without inflating or butterknife
        btnTest.setOnClickListener {
            // lambda instead of event listener implementations
            val toast = Toast.makeText(this, "Test", Toast.LENGTH_SHORT)
            toast.show()
        }

        // call java classes from kotlin
        btnDialog.setOnClickListener {
            val dialog = JavaDialog()
            dialog.show(this)
        }
    }
}
