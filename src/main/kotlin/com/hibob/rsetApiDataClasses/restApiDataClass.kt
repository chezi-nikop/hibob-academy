package com.hibob.rsetApiDataClasses

import java.time.LocalDateTime

data class Owner (val id: Int, val name: String, val companyId: Int, val employeeId: Int)

data class Pets(val id: Int, val name: String, val type: String, val companyId: Int, val dateOfArrival: LocalDateTime)