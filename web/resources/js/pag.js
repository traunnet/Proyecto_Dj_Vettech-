document.addEventListener("DOMContentLoaded", function () {
    const filas = document.querySelectorAll(".fila-usuario");
    const filasPorPagina = 15;
    const totalPaginas = Math.ceil(filas.length / filasPorPagina);
    const paginacion = document.getElementById("paginacionUsuarios");

    function mostrarPagina(pagina) {
        filas.forEach((fila, index) => {
            fila.style.display =
                index >= (pagina - 1) * filasPorPagina && index < pagina * filasPorPagina
                    ? ""
                    : "none";
        });
    }

    function crearPaginacion() {
        paginacion.innerHTML = "";

        for (let i = 1; i <= totalPaginas; i++) {
            const li = document.createElement("li");
            li.className = "page-item";
            const a = document.createElement("a");
            a.className = "page-link";
            a.textContent = i;
            a.href = "#";

            a.addEventListener("click", function (e) {
                e.preventDefault();
                mostrarPagina(i);
                document.querySelectorAll("#paginacionUsuarios .page-item").forEach(el => el.classList.remove("active"));
                li.classList.add("active");
            });

            li.appendChild(a);
            paginacion.appendChild(li);
        }

        paginacion.firstChild.classList.add("active");
    }

    mostrarPagina(1);
    crearPaginacion();
});
