package tech.medina.drivertracking.ui.delivery

import android.content.Context
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import tech.medina.drivertracking.R
import tech.medina.drivertracking.domain.model.DataState
import tech.medina.drivertracking.domain.model.Delivery
import tech.medina.drivertracking.domain.model.DeliveryStatus
import tech.medina.drivertracking.domain.usecase.GetDeliveriesUseCase
import tech.medina.drivertracking.domain.usecase.GetDeliveryDetailUseCase
import javax.inject.Inject

@HiltViewModel
class DeliveryViewModel @Inject constructor(
    state: SavedStateHandle,
    @ApplicationContext private val context: Context,
    private val dispatcher: CoroutineDispatcher,
    private val getDeliveriesUseCase: GetDeliveriesUseCase,
    private val getDeliveryDetailUseCase: GetDeliveryDetailUseCase
) : ViewModel() {

    private val _deliveryListState: MutableLiveData<DataState<List<Delivery>>> = MutableLiveData()
    val deliveryListState: LiveData<DataState<List<Delivery>>> get() = _deliveryListState

    private val _deliveryDetailState: MutableLiveData<DataState<Delivery>> = MutableLiveData()
    val deliveryDetailState: LiveData<DataState<Delivery>> get() = _deliveryDetailState

    private val _deliveryActionState: MutableLiveData<DataState<Int>> = MutableLiveData()
    val deliveryActionState: LiveData<DataState<Int>> get() = _deliveryActionState


    fun getDeliveryList(forceUpdate: Boolean = false) {
        viewModelScope.launch {
            _deliveryListState.value = DataState.Loading
            _deliveryListState.value = try {
                withContext(dispatcher) {
                    DataState.Success(getDeliveriesUseCase(forceUpdate = forceUpdate))
                }
            } catch(e: Exception) {
                DataState.Error(e)
            }
        }
    }

    fun getDeliveryDetailWithId(id: Long) {
      viewModelScope.launch {
          _deliveryDetailState.value = DataState.Loading
          _deliveryDetailState.value = try {
              withContext(dispatcher) {
                  DataState.Success(getDeliveryDetailUseCase(id))
              }
          } catch (e: Exception) {
              DataState.Error(e)
          }
      }
    }

    fun performActionOnDeliveryDetail() {
        viewModelScope.launch {
            _deliveryActionState.value = DataState.Loading
            val detailState = _deliveryDetailState.value
            if (detailState !is DataState.Success) {
                _deliveryActionState.value = DataState.Error(context.getString(R.string.delivery_action_error_incorrect_state))
            }
            detailState as DataState.Success
            val delivery = detailState.result
            when(delivery.status) {
                DeliveryStatus.DEFAULT -> { //todo must activate

                }
                DeliveryStatus.ACTIVE -> { //todo must stop

                }
                DeliveryStatus.COMPLETED ->
                    _deliveryActionState.value = DataState.Error(context.getString(R.string.delivery_action_error_completed))
            }
        }
    }

}