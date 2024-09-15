package com.hibob.academy.service

import com.hibob.academy.dao.*
import org.springframework.stereotype.Component

@Component
class ExampleService(private val exampleDao: ExampleDao) {

    fun get(companyId: Long): Example? = exampleDao.readExample(companyId)
}