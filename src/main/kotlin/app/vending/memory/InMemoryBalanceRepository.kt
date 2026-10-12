package app.vending.memory

import app.vending.balance.Balance
import app.vending.balance.ClearBalance
import app.vending.balance.HasCredit
import app.vending.balance.HasNoCredit
import app.vending.balance.LoadBalance
import app.vending.balance.SaveBalance
import org.springframework.stereotype.Component

@Component
class InMemoryBalanceRepository {
    private var balance: Balance = HasNoCredit.INSTANCE

    private fun store(next: Balance) {
        balance = next
    }

    val load = object : LoadBalance() {
        override fun apply(): Balance = balance
    }

    val save = object : SaveBalance() {
        override fun apply(credit: HasCredit): HasCredit = credit.also(::store)
    }

    val clear = object : ClearBalance() {
        override fun apply(): HasNoCredit = HasNoCredit.INSTANCE.also(::store)
    }
}
