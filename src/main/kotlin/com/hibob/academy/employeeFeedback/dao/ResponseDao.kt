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
            record[responseTable.responseId],
            record[responseTable.feedbackId],
            record[responseTable.content],
            record[responseTable.date],
        )
    }

    fun addResponse(response: ResponseDataIn) : Long {
        val responseId = sql.insertInto(responseTable)
            .set(responseTable.responseId, response.responseId)
            .set(responseTable.feedbackId, response.feedbackId)
            .set(responseTable.content, response.content)
            .returning(responseTable.id)
            .fetchOne()!!
        return responseId.get(responseTable.id)
    }

    fun getResponse(feedbackId: Long) : List<ResponseDataOut> {
        val responses = sql.select()
            .from(responseTable)
            .where(responseTable.feedbackId.eq(feedbackId))
            .fetch(responseMapper)
        return responses
    }

    fun deleteTable(feedbackId: Long) {
        sql.deleteFrom(responseTable)
            .where(responseTable.feedbackId.eq(feedbackId))
            .execute()
    }
}