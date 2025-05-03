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
                window.location.href = "/";
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
                window.location.href = "/";
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


    console.log(email); 
    console.log(pass);
    
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
                window.location.href = "/doctor";
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
                window.location.href = "/user";
                
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

function toggle_page_section(event, page_section, name) {
    event.preventDefault();
    


    if (name !== undefined) {
        const inputs = document.querySelectorAll(`[name=${name}]`);
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
            document.getElementById(`${section.id}-circle`).className = "circle active";
        } else {
            section.style.display = "none";
            document.getElementById(`${section.id}-circle`).className = "circle";
        }
    });

}

function toggle_appointment(event, section_id, appointment_id, id) {
    event.preventDefault();
    const sections = document.querySelectorAll(".page-section");
    if(appointment_id !== undefined && section_id === "reschedule") {
        document.getElementById("resched-appointment-id").value = appointment_id;
        document.getElementById("resched-id").value = id;
    } else if (appointment_id !== undefined && section_id === "cancel") {
        document.getElementById("cancel-appointment-id").value = appointment_id;
        document.getElementById("cancel-id").value = id;
    }
    sections.forEach(section => {
        if (section.id === section_id) {
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
            window.location.href = "/user";
        } else {
            alert("Failed to book appointment.");
        }
    } catch (error) {
        alert("An error occurred: " + error.message);
    }
}

function filter_doctors_by_hospital() {
    const selectedHospital = document.querySelector('input[name=affiliate]:checked').value;
    document.querySelectorAll('input[name="doctor"]').forEach(radio => {
        const belongs = radio.dataset.hospital === selectedHospital;
        radio.parentElement.style.display = belongs ? '' : 'none';   // hide whole <div>
        if (!belongs) radio.checked = false;                         // uncheck if hidden
    });
}

function filter_doctors_by_specialty() {
    const spec = document.querySelector('input[name=specialty]:checked')?.value;
    if (!spec || spec === 'General') return;          // show all for General
  
    document.querySelectorAll('input[name="doctor"]').forEach(radio => {
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
    document.querySelectorAll('input[name="doctor"]').forEach(r => {
      if (r.dataset.hospital === hosp) specialties.add(r.dataset.specialty);
    });
  
    // add a “General” option first
    const container = document.getElementById('specialty-container');
    container.innerHTML =
      `
         <input class="radio" type="radio"
                name="specialty" id="general" value="General">
         <label for="general" class="radio-label">General</label>
       `;
  
    // append one radio per distinct specialty
    specialties.forEach(spec => {
      const safeId = `spec-${spec.replace(/\s+/g,'_')}`;
      container.insertAdjacentHTML('beforeend', `
        
          <input class="radio" type="radio"
                 name="specialty" id="${safeId}" value="${spec}">
          <label for="${safeId}" class="radio-label">${spec}</label>
        `);
    });
}

async function send_message(event, id){
    event.preventDefault();

    const button = event.target; // 'this' can also be used here if you use it within the onclick handler
    const role = button.dataset.role; // Get the data-role value
    
    const sender_id = id;
    const receiver_id = document.querySelector("input[name=recipient]:checked").value;
    const message_type = role === "user" ? "u-d" : "d-u";
    const message = document.getElementById("message").value;

    const message_data = JSON.stringify({
        sender_id: sender_id,
        receiver_id: receiver_id,
        message_type: message_type,
        message: message
    });

    try{
        response = await fetch(`${url}api/messages/create`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: message_data
        });
        if(response.ok){
            alert("Message created successfully!");
            
            document.getElementById("message").value = "";
            window.location.reload(); // reload the page to see the new message

        }
        else {
            alert("Failed to create message.");
        }
    } catch (error) {
        alert("An error occurred: " + error.message);
    }
    

    
}

function filterMessages(id) {
    const selectedRecipientId = document.querySelector('input[name=recipient]:checked').value;
    const allMessages = document.querySelectorAll("#messages-list li"); // Assuming messages are in a list
    const role = document.getElementById("recipient-div").dataset.role; // Get the data-role value
    allMessages.forEach(message => {
        const senderId = message.getAttribute("data-sender-id");
        const receiverId = message.getAttribute("data-receiver-id");
        const messageType = message.getAttribute("data-message-type");

        if(role === "user"){
            if ((receiverId === selectedRecipientId && String(id) === senderId && messageType === "u-d") || 
                (senderId === selectedRecipientId && String(id) === receiverId && messageType === "d-u")) {
                message.style.display = "list-item"; // Show only matching messages
            } else {
                message.style.display = "none"; // Hide others
            }
        } else {
            if ((receiverId === selectedRecipientId && String(id) === senderId && messageType === "d-u") || 
                (senderId === selectedRecipientId && String(id) === receiverId && messageType === "u-d")) {
                message.style.display = "list-item"; // Show only matching messages
            } else {
                message.style.display = "none"; // Hide others
            }
        }
        
    });
}

async function request_cancel_message(event, index){
    event.preventDefault();
    const reason = document.getElementById(`cancel-reason-${index}`).value;
    const appointment_id = document.getElementById(`cancel-appointment-id-${index}`).value;
    
    response = await fetch(`${url}api/appointments/find`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            appointment_id: appointment_id,
        })
    });

    const appointment = await response.json();
    console.log(appointment);
    const message_data = JSON.stringify({
        sender_id: appointment.user_id,
        receiver_id: appointment.doctor_id,
        message_type: "u-d",
        message: `I would like to cancel my appointment in ${appointment.appointment_date} at ${appointment.appointment_time}. Reason: ${reason}`

    });

    
    try{
        response = await fetch(`${url}api/messages/create`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: message_data
        });
        if(response.ok){
            alert("Message created successfully!");
            
            document.getElementById(`cancel-reason-${index}`).value = ""; // clear the input field
            window.location.reload(); // reload the page to see the new message
            
        }
        else {
            alert("Failed to create message.");
        }
    } catch (error) {
        alert("An error occurred: " + error.message);
    }

        
}

async function request_reschedule_message(event, index){
    event.preventDefault();
    const reason = document.getElementById(`resched-reason-${index}`).value;
    const appointment_id = document.getElementById(`resched-appointment-id-${index}`).value;
    const date = document.getElementById(`new-date-${index}`).value;
    const time = document.getElementById(`new-time-${index}`).value;

    response = await fetch(`${url}api/appointments/find`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            appointment_id: appointment_id,
        })
    });

    const appointment = await response.json();
    const message_data = JSON.stringify({
        sender_id: appointment.user_id,
        receiver_id: appointment.doctor_id,
        message_type: "u-d",
        message: `I would like to reschedule my appointment from ${appointment.appointment_date} at ${appointment.appointment_time} to ${date} at ${time}. Reason: ${reason}`

    });

    try{
        response = await fetch(`${url}api/messages/create`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: message_data
        });
        if(response.ok){
            alert("Message created successfully!");
            
            document.getElementById(`resched-reason-${index}`).value = ""; // clear the input field
            document.getElementById(`new-date-${index}`).value = ""; // clear the input field
            document.getElementById(`new-time-${index}`).value = ""; // clear the input field 
            // reload the page to see the new message
            
        }
        else {
            alert("Failed to create message.");
        }
    } catch (error) {
        alert("An error occurred: " + error.message);
    }


}

async function cancel_message(event, index){
    event.preventDefault();
    const reason = document.getElementById(`cancel-reason-${index}`).value;
    const appointment_id = document.getElementById(`cancel-appointment-id-${index}`).value;
    
    response = await fetch(`${url}api/appointments/find`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            appointment_id: appointment_id,
        })
    });

    const appointment = await response.json();
    console.log(appointment);
    const message_data = JSON.stringify({
        sender_id: appointment.doctor_id,
        receiver_id: appointment.user_id,
        message_type: "d-u",
        message: `I canceled our appointment in ${appointment.appointment_date} at ${appointment.appointment_time}. Reason: ${reason}`

    });

    
    try{
        response = await fetch(`${url}api/messages/create`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: message_data
        });
        if(response.ok){
            alert("Message created successfully!");
            
            document.getElementById(`cancel-reason-${index}`).value = ""; // clear the input field
            window.location.reload(); // reload the page to see the new message
            
        }
        else {
            alert("Failed to create message.");
        }
    } catch (error) {
        alert("An error occurred: " + error.message);
    }

    try{
        response = await fetch(`${url}api/appointments/`, {
            method: "DELETE",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                appointment_id: appointment.appointment_id,
            })
        });
        if(response.ok){
            alert("Appointment deleted successfully!");
            window.location.reload(); // reload the page to see the new message
            
        }
        else {
            alert("Failed to delete appointment.");
        }
    } catch (error) {
        alert("An error occurred: " + error.message);
    }
}

async function reschedule_message(event, index){
    event.preventDefault();
    const reason = document.getElementById(`resched-reason-${index}`).value;
    const appointment_id = document.getElementById(`resched-appointment-id-${index}`).value;
    const date = document.getElementById(`new-date-${index}`).value;
    const time = document.getElementById(`new-time-${index}`).value;

    response = await fetch(`${url}api/appointments/find`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            appointment_id: appointment_id,
        })
    });

    const appointment = await response.json();
    const message_data = JSON.stringify({
        sender_id: appointment.doctor_id,
        receiver_id: appointment.user_id,
        message_type: "d-u",
        message: `I rescheduled our appointment from ${appointment.appointment_date} at ${appointment.appointment_time} to ${date} at ${time}. Reason: ${reason}`

    });

    try{
        response = await fetch(`${url}api/messages/create`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: message_data
        });
        if(response.ok){
            alert("Message created successfully!");
            
            document.getElementById(`resched-reason-${index}`).value = ""; // clear the input field
            document.getElementById(`new-date-${index}`).value = ""; // clear the input field
            document.getElementById(`new-time-${index}`).value = ""; // clear the input field 

            
        }
        else {
            alert("Failed to create message.");
        }
    } catch (error) {
        alert("An error occurred: " + error.message);
    }

    try{
        response = await fetch(`${url}api/appointments/`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                appointment_id: appointment.appointment_id,
                appointment_date: date,
                appointment_time: time
            })
        });
        if(response.ok){
            alert("Appointment updated successfully!");
            window.location.reload(); // reload the page to see the new message
            
        }
        else {
            alert("Failed to update appointment.");
        }
    } catch (error) {
        alert("An error occurred: " + error.message);
    }
}

async function pay_appointment(event, billing_id){
    event.preventDefault();
    
    try{
        response = await fetch(`${url}api/billings/`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                billing_id: billing_id,
                status: "Paid"
            })
        });
        if(response.ok){
            alert("Payment successful!");
            window.location.reload(); // reload the page to see the new message
            
        }
        else {
            alert("Failed to pay appointment.");
        }
    } catch (error) {
        alert("An error occurred: " + error.message);
    }
}

async function undo_payment(event, billing_id){
    event.preventDefault();
    
    try{
        response = await fetch(`${url}api/billings/undo`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                billing_id: billing_id,
            })
        });
        if(response.ok){
            alert("Payment undone!");
            window.location.reload(); // reload the page to see the new message
            
        }
        else {
            alert("Failed to undo payment.");
        }
    } catch (error) {
        alert("An error occurred: " + error.message);
    }
}