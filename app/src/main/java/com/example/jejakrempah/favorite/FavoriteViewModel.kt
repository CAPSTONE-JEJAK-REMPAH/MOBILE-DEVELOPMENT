package com.example.jejakrempah.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.jejakrempah.database.AppDatabase
import com.example.jejakrempah.database.FavoriteEvent
import kotlinx.coroutines.launch

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {
    private val favoriteDao = AppDatabase.getDatabase(application).favoriteEventDao()

    val favoriteEvents: LiveData<List<FavoriteEvent>> = favoriteDao.getAllFavorites()

    fun addFavoriteEvent(event: FavoriteEvent) {
        viewModelScope.launch {
            favoriteDao.insertFavorite(event)
        }
    }

    fun deleteFavoriteEvent(event: FavoriteEvent) {
        viewModelScope.launch {
            favoriteDao.deleteFavorite(event)
        }
    }
}
