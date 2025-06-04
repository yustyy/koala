package com.exskylab.koala.core.utilities.exceptions;

import com.exskylab.koala.core.utilities.results.ErrorResult;
import com.exskylab.koala.core.utilities.results.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import javax.naming.AuthenticationException;
import javax.naming.InsufficientResourcesException;
import java.nio.file.AccessDeniedException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(KoalaException.class)
    public ResponseEntity<Result> handleKoalaException(KoalaException ex, HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResult(ex.getMessage(), HttpStatus.BAD_REQUEST, request.getRequestURI()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Result> handleUserNotFoundException(UserNotFoundException ex, HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResult(ex.getMessage(), HttpStatus.NOT_FOUND, request.getRequestURI()));
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Result> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex, HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResult(ex.getMessage(), HttpStatus.CONFLICT, request.getRequestURI()));
    }

    @ExceptionHandler(PhoneNumberAlreadyExistsException.class)
    public ResponseEntity<Result> handlePhoneNumberAlreadyExistsException(PhoneNumberAlreadyExistsException ex, HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResult(ex.getMessage(), HttpStatus.CONFLICT, request.getRequestURI()));
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Result> handleInvalidCredentialsException(InvalidCredentialsException ex, HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResult(ex.getMessage(), HttpStatus.UNAUTHORIZED, request.getRequestURI()));
    }

    @ExceptionHandler(EmailNotVerifiedException.class)
    public ResponseEntity<Result> handleEmailNotVerifiedException(EmailNotVerifiedException ex, HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResult(ex.getMessage(), HttpStatus.FORBIDDEN, request.getRequestURI()));
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<Result> handleInvalidTokenException(InvalidTokenException ex, HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResult(ex.getMessage(), HttpStatus.UNAUTHORIZED, request.getRequestURI()));
    }

    @ExceptionHandler(RequiredFieldException.class)
    public ResponseEntity<Result> handleRequiredFieldException(RequiredFieldException ex, HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResult(ex.getMessage(), HttpStatus.BAD_REQUEST, request.getRequestURI()));
    }

    @ExceptionHandler(VerificationTokenNotFoundException.class)
    public ResponseEntity<Result> handleVerificationTokenNotFoundException(VerificationTokenNotFoundException ex, HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResult(ex.getMessage(), HttpStatus.NOT_FOUND, request.getRequestURI()));
    }

    @ExceptionHandler(VerificationTokenAlreadyUsedException.class)
    public ResponseEntity<Result> handleVerificationTokenAlreadyUsedException(VerificationTokenAlreadyUsedException ex, HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResult(ex.getMessage(), HttpStatus.CONFLICT, request.getRequestURI()));
    }

    @ExceptionHandler(VerificationTokenExpiredException.class)
    public ResponseEntity<Result> handleVerificationTokenExpiredException(VerificationTokenExpiredException ex, HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResult(ex.getMessage(), HttpStatus.BAD_REQUEST, request.getRequestURI()));
    }

    @ExceptionHandler(VerificationAlreadyApprovedException.class)
    public ResponseEntity<Result> handleVerificationAlreadyApprovedException(VerificationAlreadyApprovedException ex, HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResult(ex.getMessage(), HttpStatus.CONFLICT, request.getRequestURI()));
    }

    @ExceptionHandler(VerificationAlreadyPendingException.class)
    public ResponseEntity<Result> handleVerificationAlreadyPendingException(VerificationAlreadyPendingException ex, HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResult(ex.getMessage(), HttpStatus.CONFLICT, request.getRequestURI()));
    }

    @ExceptionHandler(VerificationTypeNotFoundException.class)
    public ResponseEntity<Result> handleVerificationTypeNotFoundException(VerificationTypeNotFoundException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResult(ex.getMessage(), HttpStatus.NOT_FOUND, request.getRequestURI()));
    }

    @ExceptionHandler(VerificationTypeInvalidException.class)
    public ResponseEntity<Result> handleVerificationTypeInvalidException(VerificationTypeInvalidException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResult(ex.getMessage(), HttpStatus.BAD_REQUEST, request.getRequestURI()));
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Result> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {
     StringBuilder errorMessages = new StringBuilder();

     ex.getBindingResult().getAllErrors().forEach((error) -> {
         String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errorMessages.append(fieldName).append(": ").append(errorMessage).append("; ");
     });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResult("Validasyon hatası oluştu detaylı bilgi: "+errorMessages, HttpStatus.BAD_REQUEST, request.getRequestURI()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result> handleGlobalException(Exception ex, WebRequest request, HttpServletRequest httpServletRequest) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResult("Beklenmeyen bir hata oluştu: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, httpServletRequest.getRequestURI()));
    }

    //for spring security dawwwgg
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Result> handleAccessDeniedException(AccessDeniedException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResult("Bu işlemi yapmak için yetkiniz yok! " + ex.getMessage(), HttpStatus.FORBIDDEN, request.getRequestURI()));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Result> handleAuthenticationException(AuthenticationException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResult("Kimlik doğrulama başarısız: " + ex.getMessage(), HttpStatus.UNAUTHORIZED, request.getRequestURI()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Result> handleBadCredentialsException(BadCredentialsException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResult("Geçersiz kullanıcı adı ve şifre " + ex.getMessage(), HttpStatus.UNAUTHORIZED, request.getRequestURI()));
    }

    @ExceptionHandler(InsufficientResourcesException.class)
    public ResponseEntity<Result> handleInsufficientResourcesException(InsufficientResourcesException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResult("Bu işlem için kimlik doğrulaması yapmanız gerekmektedir! " + ex.getMessage(), HttpStatus.UNAUTHORIZED, request.getRequestURI()));
    }

}
