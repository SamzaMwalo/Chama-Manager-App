package com.chamamanager104.core.data

import com.chamamanager104.core.model.UserSession
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Singleton
class SessionStore @Inject constructor() {
    private val _currentUser = MutableStateFlow<UserSession?>(null)
    val currentUser: StateFlow<UserSession?> = _currentUser

    fun update(session: UserSession?) {
        _currentUser.value = session
    }
}
