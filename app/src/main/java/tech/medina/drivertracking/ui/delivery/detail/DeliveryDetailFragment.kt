package tech.medina.drivertracking.ui.delivery.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import tech.medina.drivertracking.R
import tech.medina.drivertracking.databinding.FragmentItemDetailBinding
import tech.medina.drivertracking.domain.model.*
import tech.medina.drivertracking.ui.base.BaseFragment
import tech.medina.drivertracking.ui.delivery.DeliveryViewModel
import tech.medina.drivertracking.ui.utils.Constants
import tech.medina.drivertracking.ui.utils.Constants.BACKGROUND_LOCATION_PERMISSION
import tech.medina.drivertracking.ui.utils.Constants.LOCATION_PERMISSION
import tech.medina.drivertracking.ui.utils.Utils
import tech.medina.drivertracking.ui.utils.Utils.isAndroidQOrHigher
import tech.medina.drivertracking.ui.utils.getExtra
import tech.medina.drivertracking.ui.utils.toYesOrNo

class DeliveryDetailFragment : BaseFragment() {

    companion object {

        fun createWithExtras(extras: Bundle, onDetailSet: ((Delivery) -> Unit)? = null) : DeliveryDetailFragment {
            return DeliveryDetailFragment().apply {
                arguments = extras
                detailSetListener = onDetailSet
            }
        }

    }

    private var detailSetListener: ((Delivery) -> Unit)? = null
    private lateinit var binding: FragmentItemDetailBinding
    override val viewModel: DeliveryViewModel by viewModels()

    private val requestLocationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            managePermissionResult(isGranted)
        }


    override fun getBindingRoot(inflater: LayoutInflater, container: ViewGroup?): View {
        binding = FragmentItemDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initView(savedInstanceState: Bundle?) {
        initObservers()
        arguments.getExtra<Long>(Constants.INTENT_EXTRA_DELIVERY_ID) {
            viewModel.getDeliveryDetailWithId(it)
        }
    }

    private fun initObservers() {
        viewModel.deliveryDetailState.observe(this) {
            it?.let { state ->
                when (state) {
                    is DataState.Loading -> showLoader()
                    is DataState.Success -> {
                        hideLoader()
                        onDeliveryDetailSuccess(state.result)
                    }
                    is DataState.Error -> {
                        hideLoader()
                        onError(state.error)
                    }
                }
            }
        }
    }

    private fun onDeliveryDetailSuccess(delivery: Delivery) {
        detailSetListener?.invoke(delivery)
        if (Utils.isTablet(requireContext())) {
            binding.customerName.apply {
                text = getString(R.string.delivery_customer_name, delivery.customerName)
                visibility = View.VISIBLE
            }
        }
        binding.address.text = getString(R.string.delivery_address, delivery.address)
        binding.requiresSignature.text = getString(R.string.delivery_requires_signature,
            delivery.requiresSignature.toYesOrNo(requireContext()))
        binding.specialInstructions.text = getString(R.string.delivery_special_instructions,
            delivery.specialInstructions)
        binding.button.apply {
            visibility = View.VISIBLE
            text = delivery.status.toButtonString(requireContext())
            isEnabled = delivery.status.toButtonEnable()
            setOnClickListener(onButtonClicked)
        }
    }

    private val onButtonClicked = View.OnClickListener {
        checkPermissions()
    }

    private fun checkPermissions() {
        when {
            !hasPermissions(arrayOf(LOCATION_PERMISSION)) -> askPermission(LOCATION_PERMISSION)
            isAndroidQOrHigher() && !hasPermissions(arrayOf(BACKGROUND_LOCATION_PERMISSION)) -> askPermission(BACKGROUND_LOCATION_PERMISSION)
            else -> performActionOnDelivery()
        }
    }

    private fun askPermission(permisssion: String) {
        if (shouldShowRequestPermissionRationale(permisssion)) {
            //showSnackbar(getString(R.string.location_permission_rationale))
            requestLocationPermission.launch(permisssion)
            //todo show dialogs
        } else {
            requestLocationPermission.launch(permisssion)
        }
    }

    private fun managePermissionResult(granted: Boolean) {
        if (granted) {
            checkPermissions()
        } else {
            showSnackbar(getString(R.string.location_permission_denied))
            //todo show dialogs
        }
    }

    private fun performActionOnDelivery() {
        viewModel.performActionOnDeliveryDetail()
        showMessage("perform Action")
    }

}