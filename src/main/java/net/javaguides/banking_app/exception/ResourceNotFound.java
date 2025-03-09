package net.javaguides.banking_app.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class ResourceNotFound extends Exception {
    private String message;

    public ResourceNotFound(String message) {
        super(message);
    }
}
