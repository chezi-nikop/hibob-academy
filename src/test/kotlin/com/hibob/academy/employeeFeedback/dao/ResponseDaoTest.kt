package com.hibob.academy.employeeFeedback.dao

import com.hibob.academy.utils.BobDbTest
import org.jooq.DSLContext
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import kotlin.random.Random

@BobDbTest
class ResponseDaoTest @Autowired constructor(private val sql: DSLContext) {
    private val responseDao = ResponseDao(sql)

    private val responderId = Random.nextLong(1, Long.MAX_VALUE)
    private val feedbackId = Random.nextLong(1, Long.MAX_VALUE)
    private val content = "good response"
    private val companyId = Random.nextLong(1, Long.MAX_VALUE)
    private val responseIn = ResponseDataIn(responderId, feedbackId, content, companyId )

    @Test
    fun `Submit new response`() {
        val responseId = responseDao.addResponse(responseIn)
        assertNotNull(responseId)
        assertNotNull(responseDao.getResponse(responseId, companyId))
    }

    @BeforeEach
    @AfterEach
    fun clearTable() {
        responseDao.deleteTable(companyId)
    }
}