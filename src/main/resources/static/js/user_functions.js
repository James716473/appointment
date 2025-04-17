async function create_user(event){
    event.preventDefault();

    const last_name = document.getElementById("last_name").value;
    const first_name = document.getElementById("first_name").value;
    const middle_name = document.getElementById("middle_name").value;
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;
    const sex = document.querySelector('input[name=sex]:checked').value;
    const birth_date = document.getElementById("birth_date").value;
    const role = document.querySelector('input[name=role]:checked').value;

    const user_data = JSON.stringify({
        last_name: last_name,
        first_name: first_name,
        middle_name: middle_name,
        email: email,
        password: password,
        sex: sex,
        birth_date: birth_date,
        role: role
    });
    try{
        const response = await fetch("https://foal-engaged-regularly.ngrok-free.app/api/users/", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: user_data
        });
        if(response.status === 201){
            alert("user created!");
        }
    }
    catch (error){
        alert("Error has occured " + error.message);
    }

}

async function verify_user(event){
    event.preventDefault();
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;
    const user_data = JSON.stringify({
        email: email,
        password: password
    });
    try{
        const response = await fetch("https://foal-engaged-regularly.ngrok-free.app/api/users/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: user_data
        });
        
        user_info();
    } catch (error) {
        alert("error has occured");
    }
}

async function user_info(){
    try {
        const response = await fetch("https://foal-engaged-regularly.ngrok-free.app/api/users/info", {
            method: "GET",
            headers: {
                "Content-Type": "application/json"
            }
        });
        const data = await response.json();
        alert(JSON.stringify(data));

    } catch (error) {
        alert("error has occured");
    }
}

async function delete_user(event, id) {
    event.preventDefault();

    if (!confirm(`Are you sure you want to delete user with ID ${id}?`)) {
        return;
    }

    const user_data = JSON.stringify({ id: id });

    try {
        const response = await fetch("https://foal-engaged-regularly.ngrok-free.app/api/users/delete", {
            method: "DELETE",
            headers: {
                "Content-Type": "application/json"
            },
            body: user_data
        });

        if (response.ok) {
            alert("User deleted successfully!");
            location.reload();
        } else {
            alert("Failed to delete user.");
        }
    } catch (error) {
        alert("An error occurred: " + error.message);
    }
}



