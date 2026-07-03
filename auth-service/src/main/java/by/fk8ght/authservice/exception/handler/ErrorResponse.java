package by.fk8ght.authservice.exception.handler;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
public class ErrorResponse {
    private LocalDateTime time;
    private int status;
    private String message;
    private String path;
}