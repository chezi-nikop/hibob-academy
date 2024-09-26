package com.hibob.academy.employeeFeedback.dao

import com.hibob.academy.utils.JooqTable

class FeedbackTable(tableName: String = "feedback") : JooqTable(tableName) {
    val id = createBigIntField("id")
    val employeeId = createBigIntField("employee_id")
    val content = createVarcharField("content")
    val status = createVarcharField("status")
    val companyId = createBigIntField("company_id")
    val date = createLocalDateField("date")

    companion object {
        val instance = FeedbackTable()
    }
}

class EmployeeTable(tableName: String = "employees") : JooqTable(tableName) {
    val id = createBigIntField("id")
    val firstName = createVarcharField("first_name")
    val lastName = createVarcharField("last_name")
    val role = createVarcharField("role")
    val companyId = createBigIntField("company_id")
    val department = createVarcharField("department")

    companion object {
        val instance = EmployeeTable()
    }
}

class CompanyTable(tableName: String = "company") : JooqTable(tableName) {
    val id = createBigIntField("id")
    val name = createVarcharField("name")

    companion object {
        val instance = CompanyTable()
    }
}