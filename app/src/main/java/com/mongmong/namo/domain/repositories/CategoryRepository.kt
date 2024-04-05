package com.mongmong.namo.domain.repositories

import com.mongmong.namo.data.local.entity.home.Category

interface CategoryRepository {
    suspend fun addCategory(
        category: Category
    )

    suspend fun updateCategoryAfterUpload(
        localId: Long,
        serverId: Long,
        isUpload: Boolean,
        status: String
    )
}