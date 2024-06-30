package com.example.project1

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.project1.contact.Frag1
import com.example.project1.databinding.ActivityMainBinding
import com.example.project1.diary.Frag3


class MainActivity : AppCompatActivity() {

    private var mBinding: ActivityMainBinding? = null
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.bottomNavi.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_airplane -> {
                    setFrag(0)
                }

                R.id.action_airport_shuttle -> {
                    setFrag(1)
                }

                R.id.action_bluetooth -> {
                    setFrag(2)
                }

                else -> {
                    false
                }
            }
        }
        setFrag(0)
    }

    private fun setFrag(fragNum: Int): Boolean {
        val ft = supportFragmentManager.beginTransaction()

        when (fragNum) {
            0 -> {
                ft.replace(R.id.main_frame, Frag1()).commit()
            }

            1 -> {
                ft.replace(R.id.main_frame, Frag2()).commit()
            }

            2 -> {
                ft.replace(R.id.main_frame, Frag3()).commit()
            }
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d("test", "permission granted")
        } else{
            Log.d("test", "permission denied")
            Toast.makeText(applicationContext, "권한이 거부되어 이 기능을 사용할 수 없어요. 설정에서 허락해 주세요", Toast.LENGTH_SHORT).show()
        }
    }
}