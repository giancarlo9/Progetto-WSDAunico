
package giannonegiancarlo.Progetto.WSDA.payloads;

import java.time.LocalDateTime;

public class ErrorsPayload {
    private String message;
    private LocalDateTime timestamp;

    // Costruttore senza argomenti
    public ErrorsPayload() {}

    // Costruttore con argomenti
    public ErrorsPayload(String message, LocalDateTime timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }

    // Getter e Setter
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
