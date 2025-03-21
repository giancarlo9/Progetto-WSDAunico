document.addEventListener("DOMContentLoaded", () => {
  const form = document.getElementById("clienteForm");
  const responseMessage = document.getElementById("responseMessage");

  form.addEventListener("submit", async (e) => {
    e.preventDefault();

    const targa = document.getElementById("targa").value.trim().toUpperCase();
    const marca = document.getElementById("marca").value.trim();
    const modello = document.getElementById("modello").value.trim();
    const anno = document.getElementById("anno").value.trim();
    const clienteId = document.getElementById("idCliente").value.trim();
    const authToken = localStorage.getItem("authToken"); // Recupero il token

    if (!authToken) {
      responseMessage.innerHTML = `<div class="alert alert-danger">Accesso non autorizzato.</div>`;
      return;
    }

    const veicoloData = { targa, marca, modello, anno, clienteId };

    try {
      const response = await fetch("http://localhost:3001/veicoli", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${authToken}`,
        },
        body: JSON.stringify(veicoloData),
      });

      const result = await response.json();

      if (response.ok) {
        responseMessage.innerHTML = `<div class="alert alert-success">Veicolo registrato con successo!</div>`;
        setTimeout(() => {
          form.reset(); // Svuota il form
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
