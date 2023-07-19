package com.hehematch.android.hehematchandrodapp.ui.finder.core.repository

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.hehematch.android.hehematchandrodapp.core.shared.UiState
import com.hehematch.android.hehematchandrodapp.ui.finder.core.model.FindUserModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface ResultsRepository {
    fun loadUsers(): Flow<UiState<List<FindUserModel>>>
    fun saveDataList(data: List<FindUserModel>): Flow<UiState<String>>
}

class ResultsRepositoryImpl @Inject constructor(
    private val firebaseReference: DatabaseReference
) : ResultsRepository {
    override fun loadUsers(): Flow<UiState<List<FindUserModel>>> = flow {
        emit(UiState.Loading)
        try {
            val snapshot = firebaseReference.awaitSingleValueEvent()
            val userList = ArrayList<FindUserModel>()
            for (dataSnapshot in snapshot.children) {
                val data = dataSnapshot.getValue(FindUserModel::class.java)
                data?.let {
                    userList.add(it)
                }
            }
            emit(UiState.Success(userList.toList()))
        } catch (e: FirebaseNetworkException) {
            emit(UiState.Error("Connection"))
        } catch (e: Exception) {
            emit(UiState.Error(e.toString()))
        }
    }

    override fun saveDataList(data: List<FindUserModel>): Flow<UiState<String>> = flow {
        emit(UiState.Loading)
        try {
            val dataRef = firebaseReference.push()
            val dataId = dataRef.key
            if (dataId != null) {
                dataRef.setValue(data)
                    .await()
                emit(UiState.Success("Success"))
            } else {
                emit(UiState.Error("Failed to generate data ID"))
            }
        } catch (e: FirebaseNetworkException) {
            emit(UiState.Error("Connection"))
        } catch (e: Exception) {
            emit(UiState.Error(e.toString()))
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun DatabaseReference.awaitSingleValueEvent(): DataSnapshot {
        return suspendCancellableCoroutine { continuation ->
            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    continuation.resume(snapshot) {
                        it.printStackTrace()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    continuation.cancel(error.toException())
                }
            }
            addListenerForSingleValueEvent(listener)
            continuation.invokeOnCancellation { removeEventListener(listener) }
        }
    }
}
