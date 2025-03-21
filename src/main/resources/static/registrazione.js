document.addEventListener("DOMContentLoaded", () => {
  const form = document.getElementById("registrationForm");
  const errorMessage = document.getElementById("error-message");
  const successMessage = document.getElementById("success-message");

  form.addEventListener("submit", async (e) => {
    e.preventDefault();

    const username = document.getElementById("username").value.trim();
    const email = document.getElementById("email").value.trim();
    const password = document.getElementById("password").value.trim();
    const confirmPassword = document
      .getElementById("confirmPassword")
      .value.trim();
    const ruolo = document.getElementById("role").value.trim();

    // Reset messaggi
    errorMessage.textContent = "";
    successMessage.textContent = "";

    if (!username || !email || !password || !confirmPassword || !role) {
      showError("Tutti i campi sono obbligatori.");
      return;
    }

    if (password !== confirmPassword) {
      showError("Le password non corrispondono.");
      return;
    }

    try {
      const response = await fetch("http://localhost:3001/auth/register", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          username,
          email,
          password,
          ruolo,
        }),
      });

      if (response.ok) {
        showSuccess("UTENTE REGISTRATO CON SUCCESSO! Reindirizzamento...");
        setTimeout(() => {
          window.location.href = "index.html";
        }, 3000);
      } else {
        const errorData = await response.json();
        showError(errorData.message);
      }
    } catch (error) {
      console.error("Errore durante la registrazione:", error);
      showError("Errore di connessione. Riprova.");
    }
  });

  function showError(message) {
    errorMessage.textContent = message;
    errorMessage.classList.add("alert", "alert-danger");
  }

  function showSuccess(message) {
    successMessage.textContent = message;
    successMessage.classList.add("alert", "alert-success");
  }
});
