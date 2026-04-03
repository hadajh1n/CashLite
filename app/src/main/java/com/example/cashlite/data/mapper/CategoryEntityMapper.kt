package com.example.cashlite.data.mapper

import com.example.cashlite.R
import com.example.cashlite.data.local.CategoryKeys
import com.example.cashlite.data.room.category.CategoryEntity
import com.example.cashlite.data.room.category.CategoryType

class CategoryEntityMapper {

    private fun expenseCategoryToEntity(): List<CategoryEntity> {
        return listOf(
            CategoryEntity(
                type = CategoryType.EXPENSE,
                categoryName = CategoryKeys.EXPENSE_SUPERMARKET,
                imageId = R.drawable.icon_expense_basket,
                color = R.color.icon_expense_basket,
                isSystem = true,
            ),
            CategoryEntity(
                type = CategoryType.EXPENSE,
                categoryName = CategoryKeys.EXPENSE_FOOD,
                imageId = R.drawable.icon_expense_food,
                color = R.color.icon_expense_food,
                isSystem = true,
            ),
            CategoryEntity(
                type = CategoryType.EXPENSE,
                categoryName = CategoryKeys.EXPENSE_CLOTHES,
                imageId = R.drawable.icon_expense_clothes,
                color = R.color.icon_expense_clothes,
                isSystem = true,
            ),
            CategoryEntity(
                type = CategoryType.EXPENSE,
                categoryName = CategoryKeys.EXPENSE_CAR,
                imageId = R.drawable.icon_expense_car,
                color = R.color.icon_expense_car,
                isSystem = true,
            ),
            CategoryEntity(
                type = CategoryType.EXPENSE,
                categoryName = CategoryKeys.EXPENSE_BUS,
                imageId = R.drawable.icon_expense_bus,
                color = R.color.icon_expense_bus,
                isSystem = true,
            ),
            CategoryEntity(
                type = CategoryType.EXPENSE,
                categoryName = CategoryKeys.EXPENSE_BICYCLE,
                imageId = R.drawable.icon_expense_bicycle,
                color = R.color.icon_expense_bicycle,
                isSystem = true,
            ),
            CategoryEntity(
                type = CategoryType.EXPENSE,
                categoryName = CategoryKeys.EXPENSE_HOUSING,
                imageId = R.drawable.icon_expense_housing,
                color = R.color.icon_expense_housing,
                isSystem = true,
            ),
            CategoryEntity(
                type = CategoryType.EXPENSE,
                categoryName = CategoryKeys.EXPENSE_EDUCATION,
                imageId = R.drawable.icon_expense_education,
                color = R.color.icon_expense_education,
                isSystem = true,
            ),
            CategoryEntity(
                type = CategoryType.EXPENSE,
                categoryName = CategoryKeys.EXPENSE_FLAG,
                imageId = R.drawable.icon_expense_flag,
                color = R.color.icon_expense_flag,
                isSystem = true,
            ),
            CategoryEntity(
                type = CategoryType.EXPENSE,
                categoryName = CategoryKeys.EXPENSE_ELECTRONICS,
                imageId = R.drawable.icon_expense_laptop,
                color = R.color.icon_expense_laptop,
                isSystem = true,
            ),
            CategoryEntity(
                type = CategoryType.EXPENSE,
                categoryName = CategoryKeys.EXPENSE_PHONE,
                imageId = R.drawable.icon_expense_phone,
                color = R.color.icon_expense_phone,
                isSystem = true,
            ),
            CategoryEntity(
                type = CategoryType.EXPENSE,
                categoryName = CategoryKeys.EXPENSE_PHARMACY,
                imageId = R.drawable.icon_expense_pharmacy,
                color = R.color.icon_expense_pharmacy,
                isSystem = true,
            ),
            CategoryEntity(
                type = CategoryType.EXPENSE,
                categoryName = CategoryKeys.EXPENSE_BABY,
                imageId = R.drawable.icon_expense_baby,
                color = R.color.icon_expense_baby,
                isSystem = true,
            ),
            CategoryEntity(
                type = CategoryType.EXPENSE,
                categoryName = CategoryKeys.EXPENSE_CAT,
                imageId = R.drawable.icon_expense_cat,
                color = R.color.icon_expense_cat,
                isSystem = true,
            ),
        )
    }

    private fun incomeCategoryToEntity(): List<CategoryEntity> {
        return listOf(
            CategoryEntity(
                type = CategoryType.INCOME,
                categoryName = CategoryKeys.INCOME_WALLET,
                imageId = R.drawable.icon_income_wallet,
                color = R.color.icon_income_wallet,
                isSystem = true,
            ),
            CategoryEntity(
                type = CategoryType.INCOME,
                categoryName = CategoryKeys.INCOME_GRAPH,
                imageId = R.drawable.icon_income_graph,
                color = R.color.icon_income_graph,
                isSystem = true,
            ),
            CategoryEntity(
                type = CategoryType.INCOME,
                categoryName = CategoryKeys.INCOME_AWARD,
                imageId = R.drawable.icon_income_award,
                color = R.color.icon_income_award,
                isSystem = true,
            ),
        )
    }

    fun getAllSystemCategories(): List<CategoryEntity> =
        expenseCategoryToEntity() + incomeCategoryToEntity()
}