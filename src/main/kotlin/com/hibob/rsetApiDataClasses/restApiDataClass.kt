package com.hibob.rsetApiDataClasses

import java.time.LocalDate

data class Pet(val id: Long, val name: String, val type: String, val companyId: Long, val dateOfArrival: LocalDate)

data class Owner(val id: Long, val name: String?, val firstName: String?, val lastName: String?, val companyId: Long, val employeeId: String)
