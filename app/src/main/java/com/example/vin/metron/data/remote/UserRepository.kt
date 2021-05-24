package com.example.vin.metron.data.remote

class UserRepository private constructor(private val remoteDataSource:RemoteDataSource) {
    companion object {
        @Volatile
        private var instance: UserRepository? = null

        fun getInstance(
            remoteData: RemoteDataSource,
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(remoteData)
            }
    }
}