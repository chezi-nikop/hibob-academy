package com.hibob.bootcamp


/**
 * Write an extension function nullSafeToUpper() for String? that converts the string
 * to uppercase if it is not null, or returns "NO TEXT PROVIDED" if it is null.
 * Apply this function in the main function to handle the variable text.
 *
 **/
fun main5() {
    val text: String? = "Learn Kotlin"
    toUppercase(text)
    // Task: Create and use an extension function to print text in uppercase if it's not null, or "NO TEXT PROVIDED" if it is null.
}

fun toUppercase(text: String?) {
    println(text?.toUpperCase())
}

