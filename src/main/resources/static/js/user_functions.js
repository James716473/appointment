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
    const pass = document.getElementById("pass").value;
    const user_data = JSON.stringify({
        email: email,
        pass: pass
    });
    try{
        const response = await fetch(`${url}api/users/login`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: user_data
        });
        if(response.status === 200){
            alert("user logged in!");
            window.location.href = "/book-appointment";
        } else {
            alert("user not found!");
            
        }
        
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
            },
            credentials: "same-origin"
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
    const button = document.getElementById("edit__button");
    const info = document.getElementById("info");
    const edit_info = document.getElementById("edit_info");

    if (!button || !info || !edit_info) return; // checks if the 3 variable exists

    if(info.style.display === "none"){
        info.style.display = "block";
        edit_info.style.display = "none";
    } else {
        info.style.display = "none";
        edit_info.style.display = "block";
    }
}

async function edit_user(event, user_id) {
    event.preventDefault();
    
    const first_name = document.getElementById("edit_first_name").value;
    const middle_name = document.getElementById("edit_middle_name").value;
    const last_name = document.getElementById("edit_last_name").value;
    const email = document.getElementById("edit_email").value;
    const pass = document.getElementById("edit_pass").value;
    const sex = document.querySelector('input[name=edit_sex]:checked').value;
    const birth_date = document.getElementById("edit_birth_date").value;
    const contact_number = document.getElementById("edit_contact_number").value;
    const medical_history = document.getElementById("edit_medical_history").value;
    const allergies = document.getElementById("edit_allergies").value;
    const family_medical_history = document.getElementById("edit_family_medical_history").value;

    const user_data = JSON.stringify({
        user_id: user_id,
        first_name: first_name,
        middle_name: middle_name,
        last_name: last_name,
        email: email,
        pass: pass,
        sex: sex,
        birth_date: birth_date,
        contact_number: contact_number,
        medical_history: medical_history,
        allergies: allergies,
        family_medical_history: family_medical_history
    });

    try{
        const response = await fetch(`${url}api/users/`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: user_data
        });
        if(response.ok){
            alert("User information updated successfully!");
            location.reload();
        } else {
            alert("Failed to update user information.");
        }
    } catch (error) {
        alert("An error occurred: " + error.message);
    }
}

async function edit_doctor(event, doctor_id) {
    event.preventDefault();
    
    const first_name = document.getElementById("edit_first_name").value;
    const middle_name = document.getElementById("edit_middle_name").value;
    const last_name = document.getElementById("edit_last_name").value;
    const email = document.getElementById("edit_email").value;
    const pass = document.getElementById("edit_pass").value;
    const sex = document.querySelector('input[name=edit_sex]:checked').value;
    const birth_date = document.getElementById("edit_birth_date").value;
    const contact_number = document.getElementById("edit_contact_number").value;
    const specialty = document.getElementById("edit_specialty").value;
    const affiliation = document.getElementById("edit_affiliation").value;
    const schedule_from = document.getElementById("edit_schedule_from").value;
    const schedule_to = document.getElementById("edit_schedule_to").value;

    doctor_data = JSON.stringify({
        doctor_id: doctor_id,
        first_name: first_name,
        middle_name: middle_name,
        last_name: last_name,
        email: email,
        pass: pass,
        sex: sex,
        birth_date: birth_date,
        contact_number: contact_number,
        specialty: specialty,
        affiliation: affiliation,
        schedule_from: schedule_from,
        schedule_to: schedule_to
    });

    try{
        const response = await fetch(`${url}api/doctors/`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: doctor_data
        });
        if(response.ok){
            alert("User information updated successfully!");
            location.reload();
        } else {
            alert("Failed to update user information.");
        }
    } catch (error) {
        alert("An error occurred: " + error.message);
    }
}

async function create_affiliate(event){
    event.preventDefault();
    const name = document.getElementById("name").value;
    const address = document.getElementById("address").value;
    const description = document.getElementById("description").value;

    affiliate_data = JSON.stringify({
        name: name,
        address: address,
        description: description
    });

    try{
        const response = await fetch(`${url}api/affiliates/`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: affiliate_data
        });
        if(response.ok){
            alert("Affiliate created successfully!");
        } else {
            alert("Failed to create affiliate.");
        }
    } catch (error) {
        alert("An error occurred: " + error.message);
    }
    
    window.location.href = "/admin/affiliates";

}

async function delete_affiliate(event, affiliate_id) {
    event.preventDefault();

    if (!confirm(`Are you sure you want to delete affiliate with ID ${affiliate_id}?`)) {
        return;
    }

    const affiliate_data = JSON.stringify({ affiliate_id: affiliate_id });

    try {
        const response = await fetch(`${url}api/affiliates/`, {
            method: "DELETE",
            headers: {
                "Content-Type": "application/json"
            },
            body: affiliate_data
        });

        if (response.ok) {
            alert("Affiliate deleted successfully!");
            location.reload();
        } else {
            alert("Failed to delete affiliate.");
        }
    } catch (error) {
        alert("An error occurred: " + error.message);
    }
}