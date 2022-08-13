package ru.netology

import java.util.Scanner
import kotlin.collections.HashMap

private val sc = Scanner(System.`in`)

fun main() {
    val account = Account(7_540_000)
    val cardsType: HashMap<Int, String> = hashMapOf(Pair(1, "Visa"),
                                                    Pair(2, "Мир"),
                                                    Pair(3, "Mastercard"),
                                                    Pair(4, "Maestro"),
                                                    Pair(5, "VK Pay"))
    var sum: Int
    var type: String
    var more = ""

    do {
        println("\n$account")
        type = chooseType(cardsType)
        do {
            sum = enterSum()
        } while (!account.transfer(sum, type))
        if (account.zeroCheck())
            break
        do {
            try {
                print("\nWould you like to transfer more (y/n)? ")
                more = sc.next()
            } catch (_: Exception) {}
        } while (more != "y" && more != "n")
    } while (more == "y")
}

fun chooseType(cardsType: HashMap<Int, String>): String {
    var type: String
    println("""
        The terms of transfers:
          - the minimum transfer limit is 1000 kopecks;
          - using "Visa" or "Мир": commission -> 0.75%, min 3500 kopecks;
          - using "Mastercard" or "Maestro": no commission,
            if total transfers in calendar month is under
            7500000 kopecks, otherwise -> (0.6% + 2000 kopecks);
          - using "VK Pay": no commission.
    """.trimIndent())
    print("""
            Choose which way you would like to make a transfer:
            1. Visa
            2. Мир
            3. Mastercard
            4. Maestro
            5. VK Pay
            >> 
        """.trimIndent())
    do {
        try {
            type = cardsType.getValue(sc.next().toInt())
            break
        } catch (e: Exception) {
            print("Enter a correct value... >> ")
        }
    } while (true)
    return type
}

fun enterSum(): Int {
    var sum: Int
    print("Enter the sum in kopecks you would like to transfer:\n>> ")
    do {
        try {
            sum = sc.next().toInt()
            if (sum <= 0) throw Exception()
            break
        } catch (e: Exception) {
            print("Enter a correct value... >> ")
        }
    } while (true)
    return sum
}