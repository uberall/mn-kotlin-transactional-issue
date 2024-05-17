package com.example

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.runtime.Micronaut.run
import jakarta.inject.Singleton
import jakarta.transaction.Transactional

object Application {
	@JvmStatic
	fun main(args: Array<String>) {
		run(Application.javaClass)
	}
}


@Controller("/")
class MyController(
	private val myService: MyService
) {
	@Get("/exception")
	suspend fun throwException() = myService.throwException()
}

@Singleton
open class MyService {

	@Transactional
	open suspend fun throwException(): String? {
		throw MyException("Hello, World!")
	}
}

class MyException(override val message: String?) : Throwable(message)
