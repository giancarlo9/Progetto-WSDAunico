document.addEventListener("DOMContentLoaded", () => {
  const form = document.getElementById("loginForm");
  const emailInput = document.getElementById("email");
  const passwordInput = document.getElementById("password");
  const errorMessage = document.getElementById("error-message");

  form.addEventListener("submit", async (e) => {
    e.preventDefault();

    const email = emailInput.value.trim();
    const password = passwordInput.value.trim();

    if (!email || !password) {
      showError("Compila tutti i campi!");
      return;
    }

    try {
      const response = await fetch("http://localhost:3001/auth/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email, password }),
      });

      const data = await response.json();

      if (response.ok) {
       
        localStorage.setItem("authToken", data.accessToken); // Salvo il token
        localStorage.setItem("userRole", data.role); // Salvo il ruolo
        
       
        redirectUserByRole(); // Redirect in base al ruolo
      } else {
        showError(data.message || "Credenziali non valide.");
      }
    } catch (error) {
      console.error("Errore durante il login:", error);
      showError("Errore di connessione. Riprova.");
    }
  });

  // Funzione per mostrare il messaggio di errore
  function showError(message) {
    errorMessage.textContent = message;
    errorMessage.classList.add("alert", "alert-danger");
  }

  // Funzione per reindirizzare l'utente in base al ruolo
  function redirectUserByRole() {
    const role = localStorage.getItem("userRole");

    if (role === "MECCANICO") {
      window.location.href = "/meccanico-dashboard.html";
    } else if (role === "MAGAZZINIERE") {
      window.location.href = "/magazziniere-dashboard.html";
    } else if (role === "ACCETTAZIONE") {
      window.location.href = "/accettazione-dashboard.html";
    } else if (role === "CASSIERE") {
      window.location.href = "/cassiere-dashboard.html";
    } else {
      window.location.href = "/user-dashboard.html";
    }
  }
});

document.addEventListener("DOMContentLoaded", function () {
  const form = document.getElementById("stateForm");
  const responseContainer = document.createElement("div"); // Elemento per la risposta
  responseContainer.classList.add("mt-3", "alert", "d-none"); // Nasconde inizialmente il messaggio
  form.after(responseContainer); // Aggiunge il contenitore dopo il form

  form.addEventListener("submit", async function (event) {
    event.preventDefault(); // Evita il refresh della pagina

    //conversione input in MAIUSCOLO
    const codiceServizio = document
      .getElementById("codiceServizio")
      .value.trim()
      .toUpperCase();
    const targa = document.getElementById("targa").value.trim().toUpperCase();

    // Verifica che i campi non siano vuoti
    if (!codiceServizio || !targa) {
      showMessage("Compila tutti i campi!", "alert-danger");
      return;
    }

    // Creazione payload
    const payload = {
      codiceServizio: codiceServizio,
      targa: targa,
    };

    try {
      const response = await fetch(
        "http://localhost:3001/public/intervento/stato",
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify(payload),
        }
      );

      const result = await response.json(); // Conversione della risposta in JSON

      if (response.ok) {
        showMessage(
          `Stato della tua riparazione: ${JSON.stringify(result, null, 2)}`,
          "alert-success"
        );
      } else {
        showMessage(
          `Errore: ${result.message || "Qualcosa è andato storto"}`,
          "alert-danger"
        );
      }
    } catch (error) {
      showMessage("Errore di connessione al server", "alert-danger");
      console.error("Errore nella richiesta:", error);
    }
  });

  // Funzione per mostrare i messaggi di risposta
  function showMessage(message, alertClass) {
    responseContainer.textContent = message;
    responseContainer.className = `mt-3 alert ${alertClass}`;
    responseContainer.classList.remove("d-none");
  }
});
