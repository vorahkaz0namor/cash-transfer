package ru.netology

import org.junit.Test

import org.junit.Assert.*
import java.time.temporal.TemporalAmount
import kotlin.math.roundToInt

class AccountTest {

    private val COMMISSION_VISA_MIR = 0.0075
    private val COMMISSION_VISA_FIXED = 3_500
    private val COMMISSION_MAESTRO = 0.006
    private val COMMISSION_MAESTRO_FIXED = 2_000

    @Test
    fun zeroCheck() {
        val account = listOf(Account(0), Account(600), Account(1_500))
        val checkExpected = listOf(true, true, false)
        val result = ArrayList<Boolean>()
        for (acc in account)
            result.add(acc.zeroCheck())

        assertEquals(checkExpected, result)
    }

    @Test
    fun commission() {

        val accountsVisa = listOf(600_000, 460_000)
        val sumsVisa = listOf(467_000, 450_000)
        val accountsMaestro = listOf(8_000_000, 7_500_000)
        val sumsMaestro = listOf(7_600_000, 7_400_000)
        val type = listOf("Visa", "Mastercard")

        // with Visa
        assertEquals(expectedCommission(accountsVisa, sumsVisa, type[0]),
                     realCommission(accountsVisa, sumsVisa, type[0]))

        // with Mastercard
        assertEquals(expectedCommission(accountsMaestro, sumsMaestro, type[1]),
                     realCommission(accountsMaestro, sumsMaestro, type[1]))

        // with VK Pay
        assertEquals(expectedCommission(accountsVisa, sumsVisa),
                     realCommission(accountsVisa, sumsVisa))
    }

    @Test
    fun transfer() {
        val amount = 7_000
        val sum = listOf(700, 4_000, 3_000)
        val type = "Visa"
        val expectedTransfer = listOf(false, false, true)
        val result = ArrayList<Boolean>()
        for (s in sum)
            result.add(Account(amount).transfer(s, type))

        assertEquals(expectedTransfer, result)
    }

    private fun expectedCommission(accounts: List<Int>, sums: List<Int>, type: String = "VK Pay"):
            ArrayList<HashMap<String, Int>> {
        val expectedCommission = ArrayList<HashMap<String, Int>>()
        when (type) {
            "Visa" -> {
                // C01 = true; C02 = true
                expectedCommission.add(hashMapOf(
                    Pair("commission", (sums[0] * COMMISSION_VISA_MIR).roundToInt()),
                    Pair("maxPossibleSum", (accounts[0] / (1 + COMMISSION_VISA_MIR)).roundToInt())))
                // C01 = false; C02 = true
                expectedCommission.add(hashMapOf(
                    Pair("commission", COMMISSION_VISA_FIXED),
                    Pair("maxPossibleSum", (accounts[0] / (1 + COMMISSION_VISA_MIR)).roundToInt())))
                // C01 = false; C02 = false
                expectedCommission.add(hashMapOf(
                    Pair("commission", COMMISSION_VISA_FIXED),
                    Pair("maxPossibleSum", (accounts[1] - COMMISSION_VISA_FIXED))))
            }
            "Mastercard" -> {
                // C03 = true; C04 = true
                expectedCommission.add(hashMapOf(
                    Pair("commission", (sums[0] * COMMISSION_MAESTRO + COMMISSION_MAESTRO_FIXED).roundToInt()),
                    Pair("maxPossibleSum", ((accounts[0] - COMMISSION_MAESTRO_FIXED) / (1 + COMMISSION_MAESTRO)).roundToInt())))
                // C03 = false; C04 = true
                expectedCommission.add(hashMapOf(
                    Pair("commission", 0),
                    Pair("maxPossibleSum", ((accounts[0] - COMMISSION_MAESTRO_FIXED) / (1 + COMMISSION_MAESTRO)).roundToInt())))
                // C03 = false; C04 = false
                expectedCommission.add(hashMapOf(
                    Pair("commission", 0),
                    Pair("maxPossibleSum", (accounts[1]))))
            }
            else -> {
                expectedCommission.add(hashMapOf(
                    Pair("commission", 0),
                    Pair("maxPossibleSum", (accounts[0]))))
            }
        }
        return expectedCommission
    }

    private fun realCommission(accounts: List<Int>, sums: List<Int>, type: String = "VK Pay"):
            ArrayList<HashMap<String, Int>> {
        val realCommission = ArrayList<HashMap<String, Int>>()
        if (type != "VK Pay") {
            for (acc in accounts)
                for (s in sums)
                    if (s < acc)
                        realCommission.add(Account(acc).commission(s, type))
        }
        else
            realCommission.add(Account(accounts[0]).commission(sums[0]))

        return realCommission
    }
}