url = "https://foal-engaged-regularly.ngrok-free.app/"

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
        pass: password,
        sex: sex,
        birth_date: birth_date,
        role: role
    });
    try{
        if(role === "User"){
            const response = await fetch(`${url}api/users/`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: user_data
            });
            if(response.status === 201){
                alert("user created!");
            }
        } else if(role === "Doctor"){
            const response = await fetch(`${url}api/doctors/`, {
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
        
        
    }
    catch (error){
        alert("Error has occured " + error.message);
    }

}

//check doctor and user repository
async function verify_user(event){
    event.preventDefault();
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;
    const user_data = JSON.stringify({
        email: email,
        password: password
    });
    try{
        const response = await fetch(`${url}api/users/login`, {
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
        const response = await fetch(`${url}api/users/info`, {
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

async function delete_user(event, user_id) {
    event.preventDefault();

    if (!confirm(`Are you sure you want to delete user with ID ${user_id}?`)) {
        return;
    }

    const user_data = JSON.stringify({ user_id: user_id });

    try {
        const response = await fetch(`${url}api/users/delete`, {
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

async function delete_doctor(event, doctor_id) {
    event.preventDefault();

    if (!confirm(`Are you sure you want to delete doctor with ID ${doctor_id}?`)) {
        return;
    }

    const doctor_data = JSON.stringify({ doctor_id: doctor_id });

    try {
        const response = await fetch(`${url}api/doctors/delete`, {
            method: "DELETE",
            headers: {
                "Content-Type": "application/json"
            },
            body: doctor_data
        });

        if (response.ok) {
            alert("Doctor deleted successfully!");
            location.reload();
        } else {
            alert("Failed to delete doctor.");
        }
    } catch (error) {
        alert("An error occurred: " + error.message);
    }
}

function edit_toggle() {
    const button = document.getElementById("edit_button");
    const user_info = document.getElementById("user_info");
    const edit_info = document.getElementById("edit_info");

    if (!button || !user_info || !edit_info) return; // checks if the 3 variable exists

    if(user_info.style.display === "none"){
        user_info.style.display = "block";
        edit_info.style.display = "none";
    } else {
        user_info.style.display = "none";
        edit_info.style.display = "block";
    }
}
