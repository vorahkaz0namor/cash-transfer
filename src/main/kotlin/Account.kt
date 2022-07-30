package ru.netology

private const val COMMISSION_PERCENTAGE = 0.0075
private const val COMMISSION_FIXED = 3_500
private const val MIN_TRANSFER = 1_000

class Account(_amount: Int) {
    private var amount: Int = _amount

    private fun decreaseAmount(sum: Int): Boolean {
        amount -= sum
        return true
    }

    private fun commission(sum: Int): Int {
        val commissionByPercent = sum * COMMISSION_PERCENTAGE
        return if (commissionByPercent > COMMISSION_FIXED)
                   commissionByPercent.toInt()
               else
                   COMMISSION_FIXED
    }

    fun transfer(sum: Int): Boolean {
        when {
            amount == 0 -> {
                println("There is no money on Account.")
            }
            amount < sum + this.commission(sum) -> {
                println("The sum $sum kopecks is invalid.\n" +
                        "It must be not greater than ${Math.round(amount / 1.0075).toInt()} kopecks.")
            }
            sum < MIN_TRANSFER -> {
                println("The sum $sum kopecks is too small.\n" +
                        "It must be not smaller than $MIN_TRANSFER kopecks.")
            }
            sum == Int.MAX_VALUE -> {}
            else -> {
                this.decreaseAmount(sum + this.commission(sum))
                println("""
                    The transfer of sum $sum kopecks from your Account
                    to your Friend was made successfully.
                    The transfer commission was ${this.commission(sum)} kopecks.
                    $this
                """.trimIndent())
                return true
            }
        }
        return false
    }

    override fun toString(): String {
        return "Your Account is $amount kopecks."
    }
}