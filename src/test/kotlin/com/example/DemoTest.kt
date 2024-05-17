package com.example

import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.runtime.EmbeddedApplication
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import jakarta.inject.Inject
import org.junit.jupiter.api.fail

@MicronautTest(transactional = false)
class DemoTest(
    @Client("/") val client: HttpClient,
) {

    @Inject
    lateinit var application: EmbeddedApplication<*>

    @Test
    fun hikariIssue() {
        Assertions.assertTrue(application.isRunning)
        val request = HttpRequest.GET<Any>("/exception")
        val callCount = 11
        for (i in 1..callCount) {
            try {
                client.toBlocking().exchange(request, String::class.java)
            } catch (e: Throwable) {
                if (e !is HttpClientResponseException) {
                    fail("Unexpected Exception: $e")
                }
            }
        }
    }
}
