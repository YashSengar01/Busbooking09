document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("registerForm");
    const messageDiv = document.getElementById("message");

    form.addEventListener("submit", async (e) => {
        e.preventDefault();

        const formData = {
            name: form.name.value,
            email: form.email.value,
            username: form.username.value,
            password: form.password.value
        };

        try {
            const response = await fetch("/api/auth/register", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(formData)
            });

            const result = await response.json();

            if (response.ok) {
                // Success
                messageDiv.className = "alert alert-success";
                messageDiv.textContent = result.message || "Registration successful! Redirecting...";
                messageDiv.classList.remove("d-none");

                setTimeout(() => {
                    window.location.href = "/login";
                }, 1500);
            } else {
                // Error
                messageDiv.className = "alert alert-danger";
                messageDiv.textContent = result.error || "Registration failed.";
                messageDiv.classList.remove("d-none");
            }
        } catch (error) {
            messageDiv.className = "alert alert-danger";
            messageDiv.textContent = "User already exists.";
            messageDiv.classList.remove("d-none");
        }
    });
});
