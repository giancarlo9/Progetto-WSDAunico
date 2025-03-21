package giannonegiancarlo.Progetto.WSDA.payloads;

import giannonegiancarlo.Progetto.WSDA.enums.Ruolo;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record NewUtenteDTO(
        @NotEmpty(message = "È obbligatorio avere un username")
        @Size(min = 8, max = 30, message = "L'username deve essere compreso tra gli 8 e i 30 caratteri")
        String username,
        @NotEmpty(message = "L'email è obbligatoria")
        @Email(message = "L'email inserita non è valida")
        String email,
        @NotEmpty(message = "La password è obbligatoria")
        String password,
        Ruolo ruolo
) {
}
