package org.delcom.pam_p4_ifs23054.ui.viewmodels

import androidx.annotation.Keep
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.delcom.pam_p4_ifs23054.network.skincares.data.ResponseSkincareData
import org.delcom.pam_p4_ifs23054.network.skincares.service.ISkincareRepository
import javax.inject.Inject

sealed interface SkincaresUIState {
    data class Success(val data: List<ResponseSkincareData>) : SkincaresUIState
    data class Error(val message: String) : SkincaresUIState
    object Loading : SkincaresUIState
}

sealed interface SkincareUIState {
    data class Success(val data: ResponseSkincareData) : SkincareUIState
    data class Error(val message: String) : SkincareUIState
    object Loading : SkincareUIState
}

sealed interface SkincareActionUIState {
    data class Success(val message: String) : SkincareActionUIState
    data class Error(val message: String) : SkincareActionUIState
    object Loading : SkincareActionUIState
}

data class UIStateSkincare(
    val skincares: SkincaresUIState = SkincaresUIState.Loading,
    var skincare: SkincareUIState = SkincareUIState.Loading,
    var skincareAction: SkincareActionUIState = SkincareActionUIState.Loading
)

@HiltViewModel
@Keep
class SkincareViewModel @Inject constructor(
    private val repository: ISkincareRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(UIStateSkincare())
    val uiState = _uiState.asStateFlow()

    fun getAllSkincares(search: String? = null) {
        viewModelScope.launch {
            _uiState.update { it.copy(skincares = SkincaresUIState.Loading) }
            _uiState.update { it ->
                val tmpState = runCatching {
                    repository.getAllSkincares(search)
                }.fold(
                    onSuccess = {
                        if (it.status == "success") SkincaresUIState.Success(it.data!!.skincares)
                        else SkincaresUIState.Error(it.message)
                    },
                    onFailure = { SkincaresUIState.Error(it.message ?: "Unknown error") }
                )
                it.copy(skincares = tmpState)
            }
        }
    }

    fun postSkincare(
        nama: RequestBody,
        deskripsi: RequestBody,
        manfaat: RequestBody,
        efekSamping: RequestBody,
        file: MultipartBody.Part
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(skincareAction = SkincareActionUIState.Loading) }
            _uiState.update { it ->
                val tmpState = runCatching {
                    repository.postSkincare(nama, deskripsi, manfaat, efekSamping, file)
                }.fold(
                    onSuccess = {
                        if (it.status == "success") SkincareActionUIState.Success(it.data!!.skincareId)
                        else SkincareActionUIState.Error(it.message)
                    },
                    onFailure = { SkincareActionUIState.Error(it.message ?: "Unknown error") }
                )
                it.copy(skincareAction = tmpState)
            }
        }
    }

    fun getSkincareById(skincareId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(skincare = SkincareUIState.Loading) }
            _uiState.update { it ->
                val tmpState = runCatching {
                    repository.getSkincareById(skincareId)
                }.fold(
                    onSuccess = {
                        if (it.status == "success") SkincareUIState.Success(it.data!!.skincare)
                        else SkincareUIState.Error(it.message)
                    },
                    onFailure = { SkincareUIState.Error(it.message ?: "Unknown error") }
                )
                it.copy(skincare = tmpState)
            }
        }
    }

    fun putSkincare(
        skincareId: String,
        nama: RequestBody,
        deskripsi: RequestBody,
        manfaat: RequestBody,
        efekSamping: RequestBody,
        file: MultipartBody.Part?
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(skincareAction = SkincareActionUIState.Loading) }
            _uiState.update { it ->
                val tmpState = runCatching {
                    repository.putSkincare(skincareId, nama, deskripsi, manfaat, efekSamping, file)
                }.fold(
                    onSuccess = {
                        if (it.status == "success") SkincareActionUIState.Success(it.message)
                        else SkincareActionUIState.Error(it.message)
                    },
                    onFailure = { SkincareActionUIState.Error(it.message ?: "Unknown error") }
                )
                it.copy(skincareAction = tmpState)
            }
        }
    }

    fun deleteSkincare(skincareId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(skincareAction = SkincareActionUIState.Loading) }
            _uiState.update { it ->
                val tmpState = runCatching {
                    repository.deleteSkincare(skincareId)
                }.fold(
                    onSuccess = {
                        if (it.status == "success") SkincareActionUIState.Success(it.message)
                        else SkincareActionUIState.Error(it.message)
                    },
                    onFailure = { SkincareActionUIState.Error(it.message ?: "Unknown error") }
                )
                it.copy(skincareAction = tmpState)
            }
        }
    }
}
