package br.com.autoflex.exception;

import java.time.Instant;
import java.util.List;

public record ErrorApi(Instant timestamp, int status, String error, String message, String path, List<FieldViolation> fieldErrors) {

    public record FieldViolation(String field, String message) {

    }

}