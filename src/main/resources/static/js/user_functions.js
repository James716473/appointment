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
    const role = document.querySelector('input[name=role]:checked').value;

    
    const data = JSON.stringify({
        email: email,
        pass: pass
    });

    if(role === "Doctor"){
        try{
            const response = await fetch(`${url}api/doctors/login`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: data
            });
            if(response.status === 200){
                alert("Doctor logged in!");
                const doctor_id = await response.json();
                window.location.href = "/doctor-info";
            } else {
                alert("Doctor not found!");
                
            }
            
        } catch (error) {
            alert("error has occured");
        }
    } else {
        try{
            const response = await fetch(`${url}api/users/login`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: data
            });
            if(response.status === 200){
                alert("user logged in!");
                const user_id = await response.json();
                window.location.href = "/user-info";
                
            } else {
                alert("user not found!");
                
            }
            
        } catch (error) {
            alert("error has occured");
        }
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

function toggle_page_section(event, page_section, class_name) {
    event.preventDefault();
    
    if (class_name !== undefined) {
        const inputs = document.querySelectorAll(class_name);
        let checked = false;
    if (page_section === 'select-service') {
        filter_doctors_by_hospital(); // hides doctors now, not yet visible
        renderSpecialtiesForHospital();
    }
    if (page_section === 'select-doctor') {
        filter_doctors_by_hospital(); // ensure doctors match latest hospital
        filter_doctors_by_specialty(); // optional: hide by chosen specialty
    }
        if(inputs[0].type === "radio"){
            inputs.forEach(input => {
                if (input.checked) {
                    checked = true;
                }
            });   
        } else {
            checked = true;
            for (const input of inputs) {
                if (input.value.trim() === "") {
                    checked = false;
                    break;
                }
            }
        }

        if (!checked) {
            alert("Please input value to proceed.");
            return;
        }
        
    }
    

    const sections = document.querySelectorAll(".page-section");
    sections.forEach(section => {
        if (section.id === page_section) {
            section.style.display = "block";
        } else {
            section.style.display = "none";
        }
    });

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

async function book_appointment(event){
    event.preventDefault();
    const user_id = document.getElementById("user_id").value;
    const doctor_id = document.querySelector('input[name=doctor]:checked').value;
    const appointment_date = document.getElementById("date").value;
    const appointment_time = document.getElementById("time").value;
    const appointment_type = document.querySelector('input[name=specialty]:checked').value;
    const description = document.getElementById("reason").value;

    
    let billing_id = null;


    const date_issued = new Date().toISOString().slice(0, 10);

    try{
        const response = await fetch(`${url}api/billings/`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                date_issued: date_issued
            })
        });
        if(response.ok){
            billing_id = await response.json();
            alert("Billing created successfully!");
        }
        else {
            alert("Failed to create billing.");
        }
    } catch (error) {
        alert("An error occurred: " + error.message);
    }
    
    console.log(user_id);
    console.log(doctor_id);
    console.log(appointment_date);
    console.log(appointment_time);
    console.log(appointment_type);
    console.log(description);
    console.log(billing_id);
    
    appointment_data = JSON.stringify({
        user_id: user_id,
        doctor_id: doctor_id,
        appointment_date: appointment_date,
        appointment_time: appointment_time,
        appointment_type: appointment_type,
        description: description,
        billing_id: billing_id
    });

    try{
        const response = await fetch(`${url}api/appointments/`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: appointment_data
        });
        if(response.ok){
            alert("Appointment booked successfully!");
        } else {
            alert("Failed to book appointment.");
        }
    } catch (error) {
        alert("An error occurred: " + error.message);
    }
}

function filter_doctors_by_hospital() {
    const selectedHospital = document.querySelector('input[name=affiliate]:checked').value;
    document.querySelectorAll('.doctor-radio').forEach(radio => {
        const belongs = radio.dataset.hospital === selectedHospital;
        radio.parentElement.style.display = belongs ? '' : 'none';   // hide whole <div>
        if (!belongs) radio.checked = false;                         // uncheck if hidden
    });
}

function filter_doctors_by_specialty() {
    const spec = document.querySelector('input[name=specialty]:checked')?.value;
    if (!spec || spec === 'General') return;          // show all for General
  
    document.querySelectorAll('.doctor-radio').forEach(radio => {
        const match = radio.dataset.specialty === spec &&
                      radio.style.display !== 'none'; // still in hospital
        radio.parentElement.style.display = match ? '' : 'none';
        if (!match) radio.checked = false;
    });
  }

function renderSpecialtiesForHospital() {
    const hosp = document.querySelector('input[name=affiliate]:checked').value;
  
    // collect distinct specialties from doctors that belong to this hospital
    const specialties = new Set();
    document.querySelectorAll('.doctor-radio').forEach(r => {
      if (r.dataset.hospital === hosp) specialties.add(r.dataset.specialty);
    });
  
    // add a “General” option first
    const container = document.getElementById('specialty-container');
    container.innerHTML =
      `<div>
         <input class="service-radio" type="radio"
                name="specialty" id="general" value="General">
         <label for="general">General</label>
       </div>`;
  
    // append one radio per distinct specialty
    specialties.forEach(spec => {
      const safeId = `spec-${spec.replace(/\s+/g,'_')}`;
      container.insertAdjacentHTML('beforeend', `
        <div>
          <input class="service-radio" type="radio"
                 name="specialty" id="${safeId}" value="${spec}">
          <label for="${safeId}">${spec}</label>
        </div>`);
    });
}