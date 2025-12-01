    document.addEventListener("DOMContentLoaded", () => {

        const inputs = document.querySelectorAll(".form-control");

        inputs.forEach(input => {
            input.addEventListener("blur", () => {

                if (input.classList.contains("ui-state-error")) {
                    input.classList.remove("is-valid");
                    input.classList.add("is-invalid");
                } else {
                    if (input.value.trim() !== "") {
                        input.classList.remove("is-invalid");
                        input.classList.add("is-valid");
                    }
                }

            });
        });
    });