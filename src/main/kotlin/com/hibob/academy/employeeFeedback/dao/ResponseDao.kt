package com.hibob.academy.employeeFeedback.dao

import org.jooq.DSLContext
import org.jooq.RecordMapper
import org.springframework.stereotype.Component
import org.jooq.Record

@Component
class ResponseDao(private val sql: DSLContext) {
    private val responseTable = ResponseTable.instance

    private val responseMapper = RecordMapper<Record, ResponseDataOut>
    { record ->
        ResponseDataOut(
            record[responseTable.id],
            record[responseTable.responderId],
            record[responseTable.feedbackId],
            record[responseTable.content],
            record[responseTable.companyId],
            record[responseTable.date],
        )
    }

    fun addResponse(response: ResponseDataIn) : Long {
        val responseId = sql.insertInto(responseTable)
            .set(responseTable.responderId, response.responderId)
            .set(responseTable.feedbackId, response.feedbackId)
            .set(responseTable.content, response.content)
            .set(responseTable.companyId, response.companyId)
            .returning(responseTable.id)
            .fetchOne()!!
        return responseId.get(responseTable.id)
    }

    fun getResponse(feedbackId: Long, companyId: Long) : List<ResponseDataOut> {
        val responses = sql.select()
            .from(responseTable)
            .where(responseTable.feedbackId.eq(feedbackId))
            .and(responseTable.companyId.eq(companyId))
            .fetch(responseMapper)
        return responses
    }

    fun deleteTable(companyId: Long) {
        sql.deleteFrom(responseTable)
            .where(responseTable.companyId.eq(companyId))
            .execute()
    }
}