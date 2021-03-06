package com.example.wocombo.core.presentation.ui.reminders

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.wocombo.common.functional.SingleLiveData
import com.example.wocombo.core.domain.models.Reminder
import com.example.wocombo.core.domain.usecases.AddReminderUseCase
import com.example.wocombo.core.domain.usecases.DeleteReminderUseCase
import com.example.wocombo.core.domain.usecases.GetAllRemindersUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class RemindersViewModel(
    private val addReminderUseCase: AddReminderUseCase,
    private val deleteReminderUseCase: DeleteReminderUseCase,
    private val getAllRemindersUseCase: GetAllRemindersUseCase,
) : ViewModel() {

    private val _addReminderLiveData: SingleLiveData<AddReminderUseCase.Result> by lazy { SingleLiveData() }
    val addReminderLiveData: LiveData<AddReminderUseCase.Result>
        get() = _addReminderLiveData

    private val _deleteReminderLiveData: SingleLiveData<DeleteReminderUseCase.Result> by lazy { SingleLiveData() }
    val deleteReminderLiveData: LiveData<DeleteReminderUseCase.Result>
        get() = _deleteReminderLiveData

    fun addReminder(reminder: Reminder) {
        viewModelScope.launch(Dispatchers.Default) {
            val request = AddReminderUseCase.Request(reminder)
            val result = addReminderUseCase.execute(request)
            _addReminderLiveData.postValue(result)
        }
    }

    fun deleteReminder(id: Int) {
        viewModelScope.launch(Dispatchers.Default) {
            val request = DeleteReminderUseCase.Request(id)
            val result = deleteReminderUseCase.execute(request)
            _deleteReminderLiveData.postValue(result)
        }
    }

    fun getReminders(): Flow<PagingData<Reminder>> {
        val request = GetAllRemindersUseCase.Request()
        return requireNotNull(getAllRemindersUseCase.execute(request).pagingData) {
            "Paging data cannot be null"
        }
    }
}