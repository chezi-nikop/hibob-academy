package com.hibob.academy.employeeFeedback.dao

import jakarta.ws.rs.NotFoundException
import org.jooq.Condition
import org.jooq.RecordMapper
import org.jooq.DSLContext
import org.jooq.Record
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class FeedbackDao(private val sql: DSLContext) {
    private val feedbackTable = FeedbackTable.instance
    private val employeeTable = EmployeeTable.instance

    private val feedbackMapper = RecordMapper<Record, FeedbackDataOut>
    { record ->
        FeedbackDataOut(
            record[feedbackTable.id],
            record[feedbackTable.employeeId],
            record[feedbackTable.content],
            FeedbackStatus.stringToEnum(record[feedbackTable.status]),
            record[feedbackTable.companyId],
            record[feedbackTable.date]
        )
    }

    fun addFeedback(feedback: FeedbackDataIn): Long {
        val id = sql.insertInto(feedbackTable)
            .set(feedbackTable.employeeId, feedback.employeeId)
            .set(feedbackTable.content, feedback.content)
            .set(feedbackTable.status, FeedbackStatus.enumToString(FeedbackStatus.UNREVIEWED))
            .set(feedbackTable.companyId, feedback.companyId)
            .returning(feedbackTable.id)
            .fetchOne()
        return id?.get(feedbackTable.id) ?: throw NotFoundException("Failed to insert feedback")
    }

    fun getFeedbackById(id: Long, companyId: Long): FeedbackDataOut {
        return sql.select()
            .from(feedbackTable)
            .where(feedbackTable.id.eq(id))
            .and(feedbackTable.companyId.eq(companyId))
            .fetchOne(feedbackMapper) ?: throw NotFoundException("Failed to fetch feedback")
    }

    fun getFeedbackStatus(feedbackId: Long, employeeId: Long, companyId: Long): String {
        return sql.select(feedbackTable.status)
            .from(feedbackTable)
            .where(feedbackTable.id.eq(feedbackId))
            .and(feedbackTable.companyId.eq(companyId))
            .and(feedbackTable.employeeId.eq(employeeId))
            .fetchOne(feedbackTable.status)
            ?: throw NotFoundException("feedbackId does not exist in the system")
    }

    fun updateFeedbackStatus(updateFeedback: UpdateStatus, companyId: Long): Int {
        return sql.update(feedbackTable)
            .set(feedbackTable.status, updateFeedback.status.name)
            .where(feedbackTable.id.eq(updateFeedback.feedbackId))
            .and(feedbackTable.companyId.eq(companyId))
            .execute()
    }

    fun getFeedbackByFilter(filter: FeedbackFilter, companyId: Long): List<FeedbackDataOut> {
        return sql.select()
            .from(feedbackTable)
            .leftJoin(employeeTable).on(feedbackTable.employeeId.eq(employeeTable.id))
            .where(buildConditions(filter, companyId))
            .fetch(feedbackMapper)
    }

    private fun buildConditions(filter: FeedbackFilter, companyId: Long): Condition {
        return listOfNotNull(
            feedbackTable.companyId.eq(companyId),
            filter.department?.let { employeeTable.department.eq(it) },
            buildDateCondition(filter.startDate, filter.endDate),  // סינון לפי טווח תאריכים
            filter.isAnonymous?.let { anonymousCondition(it) }
        ).reduce(Condition::and)
    }

    private fun buildDateCondition(startDate: LocalDate?, endDate: LocalDate?): Condition? {
        return when {
            startDate != null && endDate != null -> feedbackTable.date.between(startDate, endDate)
            startDate != null -> feedbackTable.date.greaterOrEqual(startDate)
            endDate != null -> feedbackTable.date.lessOrEqual(endDate)
            else -> null
        }
    }

    private fun anonymousCondition(isAnonymous: Boolean): Condition {
        return if (isAnonymous) feedbackTable.employeeId.isNull
        else feedbackTable.employeeId.isNotNull
    }

    fun deleteTable(companyId: Long) {
        sql.deleteFrom(feedbackTable)
            .where(feedbackTable.companyId.eq(companyId))
            .execute()
    }
}