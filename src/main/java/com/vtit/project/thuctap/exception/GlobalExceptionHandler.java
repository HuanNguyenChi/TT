package com.vtit.project.thuctap.exception;

import com.vtit.project.thuctap.constant.enums.ResponseCode;
import com.vtit.project.thuctap.constant.enums.ResponseObject;
import com.vtit.project.thuctap.dto.response.ApiResponse;
import java.util.Map;
import java.util.Objects;

import jakarta.validation.ConstraintViolation;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private static final String MIN_ATTRIBUTE = "min";

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse> handlingRuntimeException(RuntimeException exception) {
        log.error("Exception: ", exception);
        ApiResponse apiResponse = new ApiResponse();

        apiResponse.setCode(ResponseCode.UNCATEGORIZED_EXCEPTION.getCode());
//        apiResponse.setMessage(ResponseCode.UNCATEGORIZED_EXCEPTION.getMessage());
        apiResponse.setMessage(ResponseCode.UNCATEGORIZED_EXCEPTION);


        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = ThucTapException.class)
    ResponseEntity<ApiResponse> handlingThucTapException(ThucTapException exception) {
        ResponseCode responseCode = exception.getResponseCode();
        ResponseObject responseObject = exception.getResponseObject();
        ApiResponse apiResponse = new ApiResponse();

        apiResponse.setCode(responseCode.getCode());
//        apiResponse.setMessage(responseCode.getMessage());
        apiResponse.setMessage(responseCode, responseObject);

        return ResponseEntity.status(responseCode.getStatusCode()).body(apiResponse);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ApiResponse> handlingAccessDeniedException(AccessDeniedException exception) {
        ResponseCode responseCode = ResponseCode.UNAUTHORIZED;

        return ResponseEntity.status(responseCode.getStatusCode())
                .body(ApiResponse.builder()
                        .code(responseCode.getCode())
                        .message(responseCode.getMessage())
                        .build());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handlingValidation(MethodArgumentNotValidException exception) {
        String enumKey = exception.getFieldError().getDefaultMessage();

        ResponseCode responseCode = ResponseCode.INVALID_KEY;
        Map<String, Object> attributes = null;
        try {
            responseCode = ResponseCode.valueOf(enumKey);

            var constraintViolation =
                    exception.getBindingResult().getAllErrors().get(0).unwrap(ConstraintViolation.class);

            attributes = constraintViolation.getConstraintDescriptor().getAttributes();

            log.info(attributes.toString());

        } catch (IllegalArgumentException e) {

        }

        ApiResponse apiResponse = new ApiResponse();

        apiResponse.setCode(responseCode.getCode());
//        apiResponse.setMessage(
//                Objects.nonNull(attributes)
//                        ? mapAttribute(responseCode.getMessage(), attributes)
//                        : responseCode.getMessage());
        apiResponse.setMessage(
                Objects.nonNull(attributes)
                        ? ResponseCode.valueOf(mapAttribute(responseCode.getMessage(), attributes))
                        : responseCode);
        return ResponseEntity.badRequest().body(apiResponse);
    }

    private String mapAttribute(String message, Map<String, Object> attributes) {
        String minValue = String.valueOf(attributes.get(MIN_ATTRIBUTE));

        return message.replace("{" + MIN_ATTRIBUTE + "}", minValue);
    }
}
