package hm.edu.foodweek.controller.exceptions

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ErrorAdvice {

    @ExceptionHandler(BadRequestException::class)
    fun handleBadRequest(exception: BadRequestException): ResponseEntity<String?> {
        return ResponseEntity(exception.message, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(NotFoundException::class)
    fun handleNotFound(exception: NotFoundException): ResponseEntity<String?> {
        return ResponseEntity(exception.message, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(UnauthorizedException::class)
    fun handleUnauthorized(exception: UnauthorizedException): ResponseEntity<String?> {
        return ResponseEntity("You are not authorized to perform this operation! You may need to add a 'user' header with your user ID!", HttpStatus.UNAUTHORIZED)
    }

    @ExceptionHandler(RuntimeException::class)
    fun handleInternalError(exception: RuntimeException): ResponseEntity<String?> {
        return ResponseEntity(exception.localizedMessage, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}