package app.vending

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class VendingApplication

fun main(args: Array<String>) {
    runApplication<VendingApplication>(*args)
}
