package com.hibob.academy.employeeFeedback.validation

import jakarta.ws.rs.container.ContainerRequestContext
import jakarta.ws.rs.core.Context
import com.hibob.academy.employeeFeedback.dao.EmployeeDataForCookie
import com.hibob.academy.employeeFeedback.dao.*
import com.hibob.academy.employeeFeedback.filter.AuthenticationFilterEmployee.Companion.ACTIVE_EMPLOYEE

class PermissionValidator {

    fun getInfoFromCookie(requestContext: ContainerRequestContext): EmployeeDataForCookie {
        val cookieInfo = requestContext.getProperty(ACTIVE_EMPLOYEE) as EmployeeDataForCookie
        return cookieInfo
    }

    fun getLoginCompanyId(@Context request: ContainerRequestContext): Long {
        val activeEmployee = getActiveEmployee(request)
        return activeEmployee.companyId
    }

    fun getLoginRole(@Context request: ContainerRequestContext): RoleType {
        val activeEmployee = getActiveEmployee(request)
        return activeEmployee.role
    }


}