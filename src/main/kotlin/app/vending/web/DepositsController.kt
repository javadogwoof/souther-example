package app.vending.web

import app.vending.Balance
import app.vending.Deposit
import app.vending.Money
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/deposits")
class DepositsController {

    @PostMapping
    fun post(@RequestBody body: Map<String, Any>): ResponseEntity<Any> =
        Money.decoder().decode(body).fold(
            {
                val balance = Deposit.of().apply(it)
                val response = Balance.encoder().encode(balance)

                ResponseEntity(response, HttpStatus.CREATED)
            },
            {
                ResponseEntity.badRequest().build()
            }
        )
}
