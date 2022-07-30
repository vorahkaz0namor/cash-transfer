package ru.netology

import java.util.Scanner

fun main() {
    val account = Account(1_000_000)
    val sc = Scanner(System.`in`)
    var sum: Int

    do {
        println(account)
        print("Enter the sum in kopecks you would like to transfer:\n>> ")
        try {
            val sumScan = sc.next()
            sum = sumScan.toInt()
            if (sum <= 0) throw Exception()
        } catch (e: Exception) {
            println("Enter a correct value...")
            sum = Int.MAX_VALUE
        }
    } while (!account.transfer(sum))
}