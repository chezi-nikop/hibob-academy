package com.hibob.academy.employeeFeedback.validation

import jakarta.ws.rs.container.ContainerRequestContext
import com.hibob.academy.employeeFeedback.dao.EmployeeDataForCookie
import com.hibob.academy.employeeFeedback.dao.*
import com.hibob.academy.employeeFeedback.filter.AuthenticationFilterEmployee.Companion.ACTIVE_EMPLOYEE
import org.springframework.stereotype.Component

@Component
class PermissionValidator {
    object Permission {

        fun getInfoFromCookie(requestContext: ContainerRequestContext): EmployeeDataForCookie {
            val cookieInfo = requestContext.getProperty(ACTIVE_EMPLOYEE) as EmployeeDataForCookie
            return cookieInfo
        }

        fun checkPermission(request: ContainerRequestContext): Boolean {
            val activeEmployee = getInfoFromCookie(request)

            return activeEmployee.role == RoleType.HR || activeEmployee.role == RoleType.ADMIN
        }

        fun checkFeedbackWriter(response: List<ResponseDataOut>, request: ContainerRequestContext): Boolean {
            val info = getInfoFromCookie(request)

            return response.firstOrNull()?.responderId == info.id
        }
    }
}