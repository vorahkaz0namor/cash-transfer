package ru.netology

import kotlin.math.roundToInt

private const val COMMISSION_VISA_MIR = 0.0075
private const val COMMISSION_VISA_FIXED = 3_500
private const val COMMISSION_MAESTRO = 0.006
private const val COMMISSION_MAESTRO_FIXED = 2_000
private const val MIN_TRANSFER = 1_000
private const val MONTH_TRANSFER_LIMIT = 7_500_000

class Account(_amount: Int) {
    private var amount: Int = _amount
    private var transfersThisMonth: Int = 0

    private fun decreaseAmount(sum: Int): Boolean {
        amount -= sum
        return true
    }

    fun zeroCheck(): Boolean {
        println("""
                
                ${  when {
                        (amount == 0) -> 
                            ("Sorry, but your Account has no more money to transfer.")
                        (amount < MIN_TRANSFER) -> 
                            "Sorry, but your Account is smaller than minimum transfer limit."
                        else -> return false
                    }
                }
                You must put some kopecks on it.
                Good bye!
            """.trimIndent())
        return true
    }

    private fun commission(sum: Int, type: String): HashMap<String, Int> {
        val commissionAndMaxPossibleSum = HashMap<String, Int>()
        when (type) {
            "Visa", "Мир" -> {
                val commissionByPercent = sum * COMMISSION_VISA_MIR
                if (commissionByPercent > COMMISSION_VISA_FIXED)
                    commissionAndMaxPossibleSum.put("commission", commissionByPercent.roundToInt())
                else
                    commissionAndMaxPossibleSum.put("commission", COMMISSION_VISA_FIXED)
                if ((amount * COMMISSION_VISA_MIR / (1 + COMMISSION_VISA_MIR)) > COMMISSION_VISA_FIXED)
                    commissionAndMaxPossibleSum.put("maxPossibleSum", (amount / (1 + COMMISSION_VISA_MIR)).roundToInt())
                else
                    commissionAndMaxPossibleSum.put("maxPossibleSum", amount - COMMISSION_VISA_FIXED)

            }
            "Mastercard", "Maestro" -> {
                if ((sum + transfersThisMonth) > MONTH_TRANSFER_LIMIT)
                    commissionAndMaxPossibleSum.put("commission", (sum * COMMISSION_MAESTRO + COMMISSION_MAESTRO_FIXED).roundToInt())
                else commissionAndMaxPossibleSum.put("commission", 0)
                if ((((amount - COMMISSION_MAESTRO_FIXED) / (1 + COMMISSION_MAESTRO)) + transfersThisMonth) > MONTH_TRANSFER_LIMIT)
                    commissionAndMaxPossibleSum.put("maxPossibleSum", ((amount - COMMISSION_MAESTRO_FIXED) / (1 + COMMISSION_MAESTRO)).roundToInt())
                else commissionAndMaxPossibleSum.put("maxPossibleSum", amount)
            }
            else -> {
                commissionAndMaxPossibleSum.put("commission", 0)
                commissionAndMaxPossibleSum.put("maxPossibleSum", amount)
            }
        }
        return commissionAndMaxPossibleSum
    }

    fun transfer(sum: Int, type: String): Boolean {
        val countCommission = commission(sum, type)
        return when {
            (sum < MIN_TRANSFER) -> {
                println("The sum $sum kopecks is too small.\n" +
                        "It must be not smaller than $MIN_TRANSFER kopecks.")
                false
            }
            (amount < sum + countCommission.getValue("commission")) -> {
                    println("The sum $sum kopecks is invalid.\n" +
                        "It must be not greater than ${countCommission.getValue("maxPossibleSum")} kopecks.")
                false
            }
            else -> {
                this.decreaseAmount(sum + countCommission.getValue("commission"))
                this.transfersThisMonth += sum
                println("""The transfer of sum $sum kopecks from your Account
to your Friend was made successfully.
The transfer commission was ${countCommission.getValue("commission")} kopecks.
$this""".trimIndent())
                true
            }
        }
    }

    override fun toString(): String {
        return """
            Your Account is $amount kopecks.
            Total sum of transfers in this month is $transfersThisMonth kopecks.
            """.trimIndent()
    }
}