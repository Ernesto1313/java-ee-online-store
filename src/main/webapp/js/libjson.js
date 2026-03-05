function EnviarCarrito(url, carrito) {
    const datos = carrito.map(p => ({
        codigo: p.codigo,  // 👈 importante: cambiar "id" por "codigo"
        descripcion: p.descripcion,
        imagen: p.imagen,
        cantidad: p.cantidad,
        precio: p.precio
    }));
    console.log("Carrito enviado:", datos);

    fetch(url, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(datos)
    })
    .then(response => {
        if (response.ok) {
            window.location.href = "resguardo.jsp";
        } else {
            alert("Error al procesar el pedido.");
        }
    })
    .catch(error => {
        console.error("Error:", error);
        alert("Error de conexión.");
    });
}
