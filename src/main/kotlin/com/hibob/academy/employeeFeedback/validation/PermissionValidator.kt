package com.hibob.academy.employeeFeedback.validation

import jakarta.ws.rs.container.ContainerRequestContext
import jakarta.ws.rs.core.Context
import com.hibob.academy.employeeFeedback.dao.EmployeeDataForCookie
import com.hibob.academy.employeeFeedback.dao.*
import com.hibob.academy.employeeFeedback.filter.AuthenticationFilterEmployee.Companion.ACTIVE_EMPLOYEE
import org.springframework.stereotype.Component

@Component
class PermissionValidator {
    companion object {
        val permissionValidator = PermissionValidator()
    }

    fun getInfoFromCookie(requestContext: ContainerRequestContext): EmployeeDataForCookie {
        val cookieInfo = requestContext.getProperty(ACTIVE_EMPLOYEE) as EmployeeDataForCookie
        return cookieInfo
    }

    fun checkPermission(@Context request: ContainerRequestContext): Boolean {
        val activeEmployee = getInfoFromCookie(request)

        return activeEmployee.role == RoleType.HR || activeEmployee.role == RoleType.ADMIN
    }
}