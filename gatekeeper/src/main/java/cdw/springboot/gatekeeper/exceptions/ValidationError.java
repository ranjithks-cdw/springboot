package cdw.springboot.gatekeeper.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ValidationError {
    private String field;
    private String message;
}
