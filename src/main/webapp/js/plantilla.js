document.addEventListener("DOMContentLoaded", () => {
  fetch("verificarSesion.jsp")
      .then(response => response.text())
      .then(data => {
          const isLoggedIn = data.trim() === "true";
          const userLink = isLoggedIn ? "usuario.html" : "loginUsuario.jsp";

          document.getElementById("cabecera").innerHTML = `
            <div class="fixed-header">
              <nav class="navbar navbar-dark">
                  <div class="container text-center">
                      <a class="navbar-brand d-flex align-items-center" href="index.jsp">
                        <img src="pics/botas.png" alt="Icono EVERNEST" width="30" height="30" class="me-2">
                        EVERNEST
                      </a>
                  </div>
              </nav>
              <div class="container-fluid bg-dark">
                  <div class="container">
                      <ul class="navbar-nav flex-row justify-content-center">
                          <li class="nav-item"><a class="nav-link" href="index.jsp">Inicio</a></li>
                          <li class="nav-item"><a class="nav-link" href="empresa.html">Empresa</a></li>
                          <li class="nav-item"><a class="nav-link" href="productos.jsp">Productos</a></li>
                          <li class="nav-item"><a class="nav-link" href="carrito.jsp">Carrito</a></li>
                          <li class="nav-item"><a class="nav-link" href="${userLink}">Usuario</a></li>
                          <li class="nav-item"><a class="nav-link" href="contacto.html">Contacto</a></li>
                      </ul>
                  </div>
              </div>
            </div>
          `;

          document.getElementById("pie").innerHTML = `
            <footer class="footer">
              <p>&copy; 2025 EVERNEST - Av. de Tirso de Molina, 16, Campanar, 46015 València, Valencia</p>
              <p>Instagram: @evernest | Teléfono: +34 123 456 789 | Correo: info@evernest.com</p>
            </footer>
          `;
      });
});
