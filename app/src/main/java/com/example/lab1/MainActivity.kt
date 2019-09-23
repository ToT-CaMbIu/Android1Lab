package com.example.lab1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
//import androidx.databinding.DataBindingUtil
//import com.example.lab1.databinding.ActivityMainBinding
import androidx.databinding.ObservableField
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.app.AlertDialog
import android.content.Context
import android.telephony.TelephonyManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.lab1.BuildConfig

/*class MainActivity : AppCompatActivity() {

    //lateinit var binding: ActivityMainBinding

    var mainViewModel = MainViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        //binding.viewModel = mainViewModel
        //binding.executePendingBindings()
    }
}*/

class MainActivity : AppCompatActivity() {

    val appPermissions = arrayOf(Manifest.permission.READ_PHONE_STATE)
    private val RECORD_REQUEST_CODE = 101
    private var isDialogInUse = false

    private fun setClickerToButton() {
        val textView: TextView = findViewById(R.id.phone_id)

        val permission = ContextCompat.checkSelfPermission(
            this,
            appPermissions[0]
        )

        if (permission == PackageManager.PERMISSION_GRANTED) {
            val btn_id: Button = findViewById(R.id.button_phone_id)
            val tm = baseContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            btn_id.setOnClickListener {
                textView.text = tm.deviceId.toString() + " " + BuildConfig.VERSION_NAME.toString()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            setupPermissions()
            setClickerToButton()
            return
        } else {
            isDialogInUse = savedInstanceState.getBoolean("isDialogInUse")
            if (isDialogInUse) {
                setupPermissions()
            }
        }

        val textView: TextView = findViewById(R.id.phone_id)
        textView.text = savedInstanceState.getString("textView")
        setClickerToButton()
    }

    public override fun onSaveInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.putBoolean("isDialogInUse", isDialogInUse)
        val textView: TextView = findViewById(R.id.phone_id)
        savedInstanceState.putString("textView", textView.text.toString())
        super.onSaveInstanceState(savedInstanceState)
    }

    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(
            this,
            appPermissions[0]
        )

        /*if (permission != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, appPermissions[0])) {
                val builder = AlertDialog.Builder(this)
                builder.setMessage("This app needs " + appPermissions[0] + " to pass the lab1")
                    .setTitle("Permission required")
                isDialogInUse = true
                builder.setPositiveButton("OK") { _, _ -> makeRequest() }
                val dialog = builder.create()
                dialog.show()
            } else {
                makeRequest()
            }
        }*/

        if (permission != PackageManager.PERMISSION_GRANTED) {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("This app needs " + appPermissions[0] + " to pass the lab1")
                .setTitle("Permission required")
            isDialogInUse = true
            builder.setPositiveButton("OK") { _, _ -> makeRequest() }
            val dialog = builder.create()
            dialog.show()
        }
    }

    private fun makeRequest() {
        isDialogInUse = false
        ActivityCompat.requestPermissions(
            this,
            appPermissions,
            RECORD_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            RECORD_REQUEST_CODE -> {
                setClickerToButton()
            }
        }
    }

    class MainViewModel {
        var model: MainModel = MainModel()
        val text = ObservableField<String>()
    }

    class MainModel {}
}


