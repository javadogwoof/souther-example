package app.vending.web

import app.vending.product.Product
import app.vending.purchase.InsufficientCredit
import app.vending.purchase.Purchase
import app.vending.purchase.Purchased
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/purchases")
class PurchasesController(private val purchase: Purchase) {

    @PostMapping
    fun post(@RequestBody body: Any): ResponseEntity<Any> =
        Product.decoder().decode(body)
            .fold(
                {
                    when (val result = purchase.apply(it)) {
                        is Purchased -> ResponseEntity(
                            mapOf(
                                "product" to Product.encoder().encode(result.product),
                                "change" to result.change.value,
                            ),
                            HttpStatus.CREATED,
                        )
                        is InsufficientCredit -> ResponseEntity.status(HttpStatus.CONFLICT)
                            .body(mapOf("error" to "InsufficientCredit"))
                    }
                },
                {
                    ResponseEntity.badRequest().build()
                }
            )
}
