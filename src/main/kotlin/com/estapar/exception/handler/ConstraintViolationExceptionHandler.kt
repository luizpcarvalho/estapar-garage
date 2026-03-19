package com.estapar.exception.handler

import io.micronaut.context.annotation.Primary
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Produces
import io.micronaut.http.server.exceptions.ExceptionHandler
import jakarta.inject.Singleton
import jakarta.validation.ConstraintViolationException

@Produces
@Primary
@Singleton
class ConstraintViolationExceptionHandler : ExceptionHandler<ConstraintViolationException, HttpResponse<Any>> {
    override fun handle(request: HttpRequest<*>, exception: ConstraintViolationException): HttpResponse<Any> {
        val errors = exception.constraintViolations.map { violation ->
            mapOf(
                "field" to violation.propertyPath.toString(),
                "message" to violation.message
            )
        }
        return HttpResponse.badRequest(mapOf("errors" to errors))
    }
}