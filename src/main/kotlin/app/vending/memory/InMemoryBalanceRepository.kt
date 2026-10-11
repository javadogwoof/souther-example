package app.vending.memory

import app.vending.balance.Balance
import app.vending.balance.HasCredit
import app.vending.balance.HasNoCredit
import app.vending.insertmoney.LoadBalance
import app.vending.insertmoney.SaveBalance
import org.springframework.stereotype.Component

@Component
class InMemoryBalanceStore {
    private var balance: Balance = HasNoCredit.INSTANCE

    val load = object : LoadBalance() {
        override fun apply(): Balance = balance
    }

    val save = object : SaveBalance() {
        override fun apply(credit: HasCredit): HasCredit = credit.also { balance = it }
    }
}
