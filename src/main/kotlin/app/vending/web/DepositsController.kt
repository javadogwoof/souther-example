package app.vending.web

import app.vending.insertmoney.InsertMoney
import app.vending.money.Money
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/deposits")
class DepositsController(private val insertMoney: InsertMoney) {

    @PostMapping
    fun post(@RequestBody body: Any): ResponseEntity<Any> =
        Money.decoder().decode(body)
            .fold(
                {
                    val balance = insertMoney.apply(it)

                    ResponseEntity(mapOf("credit" to balance.value), HttpStatus.CREATED)
                },
                {
                    ResponseEntity.badRequest().build()
                }
            )
}
