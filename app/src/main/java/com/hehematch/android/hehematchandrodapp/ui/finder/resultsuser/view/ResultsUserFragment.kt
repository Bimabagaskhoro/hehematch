package com.hehematch.android.hehematchandrodapp.ui.finder.resultsuser.view

import android.Manifest.permission.*
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.hehematch.android.hehematchandrodapp.core.shared.UiState
import com.hehematch.android.hehematchandrodapp.databinding.FragmentResultsUserBinding
import com.hehematch.android.hehematchandrodapp.ui.finder.core.model.FindUserModel
import com.hehematch.android.hehematchandrodapp.ui.finder.resultsuser.adapter.ResultsUserAdapter
import com.hehematch.android.hehematchandrodapp.ui.finder.resultsuser.vm.ResultsUserViewModel
import com.hehematch.android.hehematchandrodapp.utils.CustomToast
import com.hehematch.android.hehematchandrodapp.utils.calculateDistance
import kotlinx.coroutines.launch
import java.util.*

class ResultsUserFragment : Fragment() {
    private val viewModel: ResultsUserViewModel by viewModels()
    private var _binding: FragmentResultsUserBinding? = null
    private val binding get() = _binding!!
    private val adapterResults by lazy {
        ResultsUserAdapter { data ->
            Toast.makeText(requireContext(), data.username, Toast.LENGTH_SHORT).show()
        }
    }

    //location
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var myLat = 0.0
    private var myLong = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultsUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvUserResults.apply {
            adapter = adapterResults
        }
        fetchData()
        setPermissionLocation()
    }

    private fun fetchData() {
        lifecycleScope.launch {
            viewModel.allData.collect { uiState ->
                when (uiState) {
                    is UiState.Loading -> {

                    }
                    is UiState.Success -> {
                        showDataWithRadius(uiState.data)
                        CustomToast.showSuccess(requireContext())
                    }
                    is UiState.Error -> {
                        if (uiState.message == "Connection") {
                            CustomToast.showNoInternet(requireContext())
                        } else {
                            CustomToast.showError(requireContext())
                        }
                    }
                }
            }
        }
    }


    private fun showDataWithRadius(data: List<FindUserModel>) {
        val latitude = binding.myLatHelper.text.toString().toDouble()
        val longitude = binding.myLongHelper.text.toString().toDouble()
        val radiusSet = 5.0
        val usersWithinRadius = findUsersWithinRadius(data, latitude, longitude, radiusSet)
        adapterResults.submitList(usersWithinRadius)
    }


    private fun findUsersWithinRadius(
        users: List<FindUserModel>,
        latitude: Double,
        longitude: Double,
        radius: Double
    ): List<FindUserModel> {
        val usersWithinRadius = mutableListOf<FindUserModel>()
        for (user in users) {
            val distance =
                calculateDistance(
                    latitude,
                    longitude,
                    user.latitude ?: continue,
                    user.longitude ?: continue
                )
            if (distance <= radius) {
                usersWithinRadius.add(user)
            }
        }

        return usersWithinRadius
    }


    private fun setPermissionLocation() {
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                requireActivity(), REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        if (ContextCompat.checkSelfPermission(
                requireContext(), ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(ACCESS_FINE_LOCATION), PERMISSION_LOCATION
            )
        } else {
            try {
                Timer().scheduleAtFixedRate(object : TimerTask() {
                    override fun run() {
                        actionLocationNow()
                    }
                }, 50000, 50000)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun actionLocationNow() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(), ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(), ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    myLat = latitude
                    myLong = longitude
                    myLocationNow(latitude, longitude)
                }
            }
    }

    private fun myLocationNow(latitude: Double, longitude: Double) {
        binding.apply {
            myLatHelper.text = latitude.toString()
            myLongHelper.text = longitude.toString()
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(CAMERA)
        const val REQUEST_CODE_PERMISSIONS = 10
        const val PERMISSION_LOCATION = 100
    }
}