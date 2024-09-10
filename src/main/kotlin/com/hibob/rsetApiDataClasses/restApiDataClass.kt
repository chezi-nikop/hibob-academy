package com.hibob.rsetApiDataClasses

import java.time.LocalDateTime

data class Owner(val id: Long, val name: String, val companyId: Long, val employeeId: Long)

data class Pet(val id: Long, val name: String, val type: String, val companyId: Long, val dateOfArrival: LocalDateTime)