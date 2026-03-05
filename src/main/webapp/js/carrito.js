class ProductoCarrito {
    constructor(codigo, descripcion, imagen, cantidad, precio, existencias) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.cantidad = cantidad;
        this.precio = precio;
        this.existencias = existencias;
    }
}

const CLAVE_CARRITO = "carrito";
let carrito = [];

function cargarCarrito() {
    const datos = localStorage.getItem(CLAVE_CARRITO);
    carrito = datos ? JSON.parse(datos) : [];
}

function guardarCarrito() {
    localStorage.setItem(CLAVE_CARRITO, JSON.stringify(carrito));
}

function anadirCarrito(codigo, descripcion, imagen, precio, existencias) {
    cargarCarrito();

    let producto = carrito.find(p => p.codigo === codigo);
    if (producto) {
        if (producto.cantidad < producto.existencias) {
            producto.cantidad += 1;
        } else {
            alert("No hay más existencias disponibles.");
            return;
        }
    } else {
        carrito.push(new ProductoCarrito(codigo, descripcion, imagen, 1, precio, existencias));
    }

    guardarCarrito();
    alert(`${descripcion} añadido al carrito.`);
}

function eliminarProducto(codigo) {
    cargarCarrito();
    carrito = carrito.filter(p => p.codigo !== codigo);
    guardarCarrito();
    alert("Producto eliminado del carrito");
    location.reload();
}

function cambiarCantidad(codigo, nuevaCantidad) {
    cargarCarrito();
    const producto = carrito.find(p => p.codigo === codigo);
    if (producto) {
        const cantidadNum = parseInt(nuevaCantidad);

        if (isNaN(cantidadNum)) {
            alert("Cantidad inválida.");
            return;
        }

        if (cantidadNum <= 0) {
            eliminarProducto(codigo);
        } else if (cantidadNum <= producto.existencias) {
            producto.cantidad = cantidadNum;
            guardarCarrito();
            location.reload();
        } else {
            alert("No puedes pedir más de las existencias disponibles");
        }
    }
}


function mostrarResumenCompra() {
    cargarCarrito();
    const contenedor = document.getElementById("productosCarrito");
    const total = document.getElementById("totalAmount");

    contenedor.innerHTML = "";
    let suma = 0;

    carrito.forEach(p => {
        const div = document.createElement("div");
        div.classList.add("alert", "alert-info");
        div.innerHTML = `
            <img src="pics/${p.imagen.split('/').pop()}" width="125" height="125"><br>
            <strong>${p.descripcion}</strong><br>
            Precio: ${p.precio} €<br>
            Cantidad: ${p.cantidad}
            <hr>
        `;
        contenedor.appendChild(div);
        suma += p.precio * p.cantidad;
    });

    total.textContent = suma.toFixed(2) + "€";
}

function eliminarCarrito() {
    carrito = [];
    guardarCarrito();
}

function mostrarCarrito() {
    cargarCarrito();
    const contenedor = document.getElementById("productosCarrito");
    const total = document.getElementById("totalAmount");

    contenedor.innerHTML = "";
    let suma = 0;

    carrito.forEach(p => {
        const div = document.createElement("div");
        div.classList.add("alert", "alert-info");
        div.innerHTML = `
            <img src="pics/${p.imagen.split('/').pop()}" width="125" height="125"><br>
            <strong>${p.descripcion}</strong><br>
            Precio: ${p.precio} €<br>
            Cantidad: <input type="number" value="${p.cantidad}" min="1" max="${p.existencias}" onchange="cambiarCantidad(${p.codigo}, this.value)">
            <button onclick="eliminarProducto(${p.codigo})" class="btn btn-danger btn-sm ms-2">Eliminar</button>
            <hr>
        `;
        contenedor.appendChild(div);
        suma += p.precio * p.cantidad;
    });

    total.textContent = suma.toFixed(2) + "€";
}
