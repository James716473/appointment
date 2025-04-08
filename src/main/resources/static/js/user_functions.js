async function create_user(event){
    event.preventDefault();

    const last_name = document.getElementById("last_name").value;
    const first_name = document.getElementById("first_name").value;
    const middle_name = document.getElementById("middle_name").value;
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;
    const sex = document.querySelector('input[name=sex]:checked').value;
    const birth_date = document.getElementById("birth_date").value;
    const role = document.querySelector('select[name=role]').value;

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
        const response = await fetch("http://localhost:8080/api/users/", {
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
        alert("Error has occured");
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
        const response = await fetch("http://localhost:8080/api/users/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: user_data
        });
        alert(response.status);
    } catch (error) {
        alert("error has occured");
    }
}