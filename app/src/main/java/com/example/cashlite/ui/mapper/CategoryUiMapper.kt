package com.example.cashlite.ui.mapper

import com.example.cashlite.data.dataclass.history.CategoryUI
import com.example.cashlite.data.room.category.CategoryEntity

class CategoryUiMapper {

    fun fromEntityToUI(entity: CategoryEntity): CategoryUI =
        CategoryUI(
            idCategory = entity.idCategory,
            imageId = entity.imageId,
            categoryName = entity.categoryName,
        )
}