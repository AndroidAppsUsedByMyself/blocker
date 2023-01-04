/*
 * Copyright 2022 Blocker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.merxury.blocker.core.data.respository

import com.merxury.blocker.core.database.cmpdetail.ComponentDetailEntity
import com.merxury.blocker.core.network.model.NetworkComponentDetail
import com.merxury.blocker.core.result.Result
import kotlinx.coroutines.flow.Flow

interface ComponentDataRepository {
    suspend fun getNetworkComponentData(fullName: String): Flow<Result<NetworkComponentDetail>>

    suspend fun getLocalComponentData(fullName: String): ComponentDetailEntity?

    suspend fun getUserGeneratedComponentDetail(fullName: String): NetworkComponentDetail?

    suspend fun saveComponentAsCache(component: NetworkComponentDetail)

    suspend fun saveUserGeneratedComponentDetail(componentDetail: NetworkComponentDetail): Boolean
}