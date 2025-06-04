package com.exskylab.koala.core.utilities.exceptions;

import com.exskylab.koala.core.utilities.results.ErrorDataResult;
import com.exskylab.koala.core.utilities.results.ErrorResult;
import com.exskylab.koala.core.utilities.results.Result;
import com.exskylab.koala.entities.VerificationType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.View;

import javax.naming.AuthenticationException;
import javax.naming.InsufficientResourcesException;
import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(KoalaException.class)
    public ResponseEntity<Result> handleKoalaException(KoalaException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResult(ex.getMessage(), HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Result> handleUserNotFoundException(UserNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResult(ex.getMessage(), HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Result> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResult(ex.getMessage(), HttpStatus.CONFLICT));
    }

    @ExceptionHandler(PhoneNumberAlreadyExistsException.class)
    public ResponseEntity<Result> handlePhoneNumberAlreadyExistsException(PhoneNumberAlreadyExistsException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResult(ex.getMessage(), HttpStatus.CONFLICT));
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Result> handleInvalidCredentialsException(InvalidCredentialsException ex){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResult(ex.getMessage(), HttpStatus.UNAUTHORIZED));
    }

    @ExceptionHandler(EmailNotVerifiedException.class)
    public ResponseEntity<Result> handleEmailNotVerifiedException(EmailNotVerifiedException ex){
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResult(ex.getMessage(), HttpStatus.FORBIDDEN));
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<Result> handleInvalidTokenException(InvalidTokenException ex){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResult(ex.getMessage(), HttpStatus.UNAUTHORIZED));
    }

    @ExceptionHandler(RequiredFieldException.class)
    public ResponseEntity<Result> handleRequiredFieldException(RequiredFieldException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResult(ex.getMessage(), HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(VerificationTokenNotFoundException.class)
    public ResponseEntity<Result> handleVerificationTokenNotFoundException(VerificationTokenNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResult(ex.getMessage(), HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler(VerificationTokenAlreadyUsedException.class)
    public ResponseEntity<Result> handleVerificationTokenAlreadyUsedException(VerificationTokenAlreadyUsedException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResult(ex.getMessage(), HttpStatus.CONFLICT));
    }

    @ExceptionHandler(VerificationTokenExpiredException.class)
    public ResponseEntity<Result> handleVerificationTokenExpiredException(VerificationTokenExpiredException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResult(ex.getMessage(), HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(VerificationAlreadyApprovedException.class)
    public ResponseEntity<Result> handleVerificationAlreadyApprovedException(VerificationAlreadyApprovedException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResult(ex.getMessage(), HttpStatus.CONFLICT));
    }

    @ExceptionHandler(VerificationAlreadyPendingException.class)
    public ResponseEntity<Result> handleVerificationAlreadyPendingException(VerificationAlreadyPendingException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResult(ex.getMessage(), HttpStatus.CONFLICT));
    }

    @ExceptionHandler(VerificationTypeNotFoundException.class)
    public ResponseEntity<Result> handleVerificationTypeNotFoundException(VerificationTypeNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResult(ex.getMessage(), HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler(VerificationTypeInvalidException.class)
    public ResponseEntity<Result> handleVerificationTypeInvalidException(VerificationTypeInvalidException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResult(ex.getMessage(), HttpStatus.BAD_REQUEST));
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Result> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorDataResult<>(errors,"Validasyon hatası detaylı bilgi için dataya bakınız!", HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result> handleGlobalException(Exception ex, WebRequest request){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResult("Beklenmeyen bir hata oluştu: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
    }

    //for spring security dawwwgg
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Result> handleAccessDeniedException(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResult("Bu işlemi yapmak için yetkiniz yok! " + ex.getMessage(), HttpStatus.FORBIDDEN));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Result> handleAuthenticationException(AuthenticationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResult("Kimlik doğrulama başarısız: " + ex.getMessage(), HttpStatus.UNAUTHORIZED));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Result> handleBadCredentialsException(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResult("Geçersiz kullanıcı adı ve şifre " + ex.getMessage(), HttpStatus.UNAUTHORIZED));
    }

    @ExceptionHandler(InsufficientResourcesException.class)
    public ResponseEntity<Result> handleInsufficientResourcesException(InsufficientResourcesException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResult("Bu işlem için kimlik doğrulaması yapmanız gerekmektedir! " + ex.getMessage(), HttpStatus.UNAUTHORIZED));
    }

}
