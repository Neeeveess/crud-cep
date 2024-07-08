package br.com.crudcep.exceptions

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExceptionControllerAdvice {

    @ExceptionHandler
    fun handleCepNotFoundException(ex: CepNotFoundException): ResponseEntity<ErrorMessageModel> {
        val errorMessage = ErrorMessageModel(
            HttpStatus.NOT_FOUND.value(),
            ex.message,
            System.currentTimeMillis()
        )
        return ResponseEntity(errorMessage, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler
    fun handleCepExists(ex: CepExistsException): ResponseEntity<ErrorMessageModel> {
        val errorMessage = ErrorMessageModel(
            HttpStatus.CONFLICT.value(),
            ex.message,
            System.currentTimeMillis()
        )
        return ResponseEntity(errorMessage, HttpStatus.CONFLICT)
    }
}