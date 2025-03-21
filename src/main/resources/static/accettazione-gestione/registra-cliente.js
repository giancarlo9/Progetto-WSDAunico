document.addEventListener("DOMContentLoaded", () => {
  const form = document.getElementById("clienteForm");
  const responseMessage = document.getElementById("responseMessage");

  form.addEventListener("submit", async (e) => {
    e.preventDefault();

    const nome = document.getElementById("nome").value.trim();
    const cognome = document.getElementById("cognome").value.trim();
    const indirizzo = document.getElementById("indirizzo").value.trim();
    const telefono = document.getElementById("telefono").value.trim();
    const email = document.getElementById("email").value.trim();
    const authToken = localStorage.getItem("authToken"); // Recupero il token

    if (!authToken) {
      responseMessage.innerHTML = `<div class="alert alert-danger">Accesso non autorizzato.</div>`;
      return;
    }

    const clienteData = { nome, cognome, indirizzo, telefono, email };

    try {
      const response = await fetch("http://localhost:3001/clienti", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${authToken}`,
        },
        body: JSON.stringify(clienteData),
      });

      const result = await response.json();

      if (response.ok) {
        responseMessage.innerHTML = `<div class="alert alert-success">Cliente registrato con successo!</div>`;
        setTimeout(() => {
          form.reset(); // Svuoto il form
          responseMessage.innerHTML = "";
        }, 3000); 
      } else {
        responseMessage.innerHTML = `<div class="alert alert-danger">${
          result.message || "Errore nella registrazione"
        }</div>`;
      }
    } catch (error) {
      console.error("Errore nella richiesta:", error);
      responseMessage.innerHTML = `<div class="alert alert-danger">Errore di connessione al server.</div>`;
    }
  });
});
