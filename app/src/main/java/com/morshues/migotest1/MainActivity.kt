package com.morshues.migotest1

import android.net.ConnectivityManager
import android.net.Network
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.morshues.migotest1.databinding.ActivityMainBinding
import com.morshues.migotest1.api.MigoApi
import com.morshues.migotest1.api.HTTPResult
import com.morshues.migotest1.util.NetworkUtils
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var mApi = MigoApi()

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            mApi.setPublic(true)
        }

        override fun onLost(network: Network) {
            mApi.setPublic(false)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        setContentView(binding.root)

        NetworkUtils.registerWifiCallback(this, networkCallback)
    }

    override fun onDestroy() {
        NetworkUtils.unregisterWifiCallback(this, networkCallback)
        super.onDestroy()
    }

    private fun initBinding() {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(this),
            R.layout.activity_main,
            null,
            false
        )

        updateResult()
    }

    private fun updateResult() {
        binding.response = "Fetching"
        lifecycleScope.launch {
            when(val result = mApi.getStatus()) {
                is HTTPResult.Success -> {
                    binding.response = result.data
                }
                is HTTPResult.Failure -> {
                    binding.response = result.msg
                }
            }
        }
    }

}
