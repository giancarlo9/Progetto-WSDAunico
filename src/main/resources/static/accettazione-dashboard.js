document.addEventListener("DOMContentLoaded", () => {
  const welcomeMessage = document.getElementById("welcome-message");
  const logoutButton = document.getElementById("logoutButton");

  // Recupero il ruolo dal localStorage
  const userRole = localStorage.getItem("userRole");

  if (userRole) {
    welcomeMessage.textContent = `Benvenuto nella dashboard ${userRole}!`;
  } else {
    welcomeMessage.textContent = "Benvenuto!";
  }

  // Funzione di logout
  logoutButton.addEventListener("click", () => {
    localStorage.removeItem("authToken");
    localStorage.removeItem("userRole");
    window.location.href = "index.html"; // Redirect alla homepage o alla pagina di login
  });
});
