import arrow.data.*

sealed class ProductValidationError(val errorMsg: String) {
    object InvalidSellingUnit : ProductValidationError("Missing sellingUnit")
    object InvalidCapacityQuantity : ProductValidationError("Missing sellingCapacity")
}

class ProductValidator {

    fun validateProduct(productData: ProductData): Validated<NonEmptyList<ProductValidationError>, ProductData> {

        val result = Validated
                .applicative(NonEmptyList.semigroup<ProductValidationError>())
                .map(
                        productData.validateSellingUnit(),
                        productData.validateCapacityQuantity()
                )
                { it.a }


        return result.fix()
    }

    private fun ProductData.validateSellingUnit(): Validated<NonEmptyList<ProductValidationError>, ProductData> =
            when {
                this.unitSale.isNullOrBlank() -> Validated.Invalid(NonEmptyList(ProductValidationError.InvalidSellingUnit))
                else -> Validated.Valid(this)
            }


    private fun ProductData.validateCapacityQuantity(): Validated<NonEmptyList<ProductValidationError>, ProductData> =
            when {
                this.capacityQuantity.isNullOrBlank() -> Validated.Invalid(NonEmptyList(ProductValidationError.InvalidCapacityQuantity))
                else -> Validated.Valid(this)
            }

}

