package com.hibob.bootcamp


/**
 * Modify the main function to print each user's email safely.
 * Use the Elvis operator to provide the "Email not provided" default string if the email is null.
 * Ensure your solution handles both user1 and user2 correctly.
 */
data class User(val name: String?, val email: String?)

fun main3() {
    val user1: User = User("Alice", null)
    val user2: User = User(null, "alice@example.com")

    println(user1?.email ?: "Email not provided")
    println(user2?.email ?: "Email not provided")

    // Task: Print user email or "Email not provided" if null
}
