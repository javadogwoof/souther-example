package app.vending.web

import app.vending.insertmoney.InsertMoney
import app.vending.memory.InMemoryBalanceRepository
import app.vending.purchase.Purchase
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class VendingConfig {

    @Bean
    fun insertMoney(repository: InMemoryBalanceRepository): InsertMoney =
        InsertMoney.bind(repository.load, repository.save)

    @Bean
    fun purchase(repository: InMemoryBalanceRepository): Purchase =
        Purchase.bind(repository.load, repository.clear)
}
