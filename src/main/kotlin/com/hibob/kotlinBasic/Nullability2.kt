package com.hibob.bootcamp


/**
* Modify the main function to calculate and print the sum of all non-null integers in the list numbers.
* Use safe calls and/or the Elvis operator where appropriate.
 **/
fun main4() {
    val numbers: List<Int?> = listOf(1, null, 3, null, 5)
    val sum = numbers.filterNotNull().sum()
    println(sum)
}
    // Task: Calculate the sum of all non-null numbers

