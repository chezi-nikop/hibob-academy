package com.hibob.academy.employeeFeedback.dao

import jakarta.ws.rs.NotFoundException
import org.jooq.RecordMapper
import org.jooq.DSLContext
import org.jooq.Record
import org.springframework.stereotype.Component

@Component
class FeedbackDao(private val sql: DSLContext) {
    private val feedbackTable = FeedbackTable.instance

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

    fun getFeedbackStatus(id: Long, employeeId: Long, companyId: Long): String {
        return sql.select(feedbackTable.status)
            .from(feedbackTable)
            .where(feedbackTable.id.eq(id))
            .and(feedbackTable.companyId.eq(companyId))
            .and(feedbackTable.employeeId.eq(employeeId))
            .fetchOne(feedbackTable.status)
            ?: throw NotFoundException("companyId or feedbackId or employeeId does not exist in the system")
    }

    fun deleteTable(companyId: Long) {
        sql.deleteFrom(feedbackTable)
            .where(feedbackTable.companyId.eq(companyId))
            .execute()
    }
}