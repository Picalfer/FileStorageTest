package com.landfathich.filsedatastorageapptest

import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.landfathich.filsedatastorageapptest.databinding.ActivityMainBinding
import java.io.BufferedReader
import java.io.DataInputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var externalFile: File
    private val filename = "file.txt"
    private val filepath = "files"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }

        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            Toast.makeText(
                this,
                "External storage not available on this device..",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            externalFile = File(getExternalFilesDir(filepath), filename)
        }

        binding.btnSave.setOnClickListener {
            if (binding.etMessage.text.isEmpty()) {
                Toast.makeText(this, "Please enter your message", Toast.LENGTH_SHORT).show()
            } else {
                try {
                    val fileOutputStream = FileOutputStream(externalFile)
                    fileOutputStream.write(binding.etMessage.text.toString().toByteArray())
                    fileOutputStream.close()
                    Toast.makeText(this, "File saved to external storage..", Toast.LENGTH_SHORT)
                        .show()
                } catch (e: IOException) {
                    e.printStackTrace();
                }
            }
        }

        binding.btnShow.setOnClickListener {
            var fileData = ""
            try {
                val fileInputStream = FileInputStream(externalFile)
                val dataInputSteam = DataInputStream(fileInputStream)
                val bufferedReader = BufferedReader(InputStreamReader(dataInputSteam))
                var strLine: String
                while (bufferedReader.readLine().also { strLine = it } != null) {
                    fileData = fileData + strLine
                }
                binding.tvHint.text = fileData
                dataInputSteam.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun isExternalStorageReadOnly(): Boolean {
        val externalStorageState = Environment.getExternalStorageState()
        if (Environment.MEDIA_MOUNTED_READ_ONLY == externalStorageState) {
            return true
        }
        return false
    }

    private fun isExternalStorageAvailable(): Boolean {
        val externalStorageState = Environment.getExternalStorageState()
        if (Environment.MEDIA_MOUNTED == externalStorageState) {
            return true
        }
        return false
    }
}