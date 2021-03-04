//import "./common.js";

const emailFieldEL = document.querySelector("#emailField");
const passFieldEL = document.querySelector("#passField");
const loginFormEL = document.querySelector("#loginForm");


function handleServerResponse(responseJson){
    if(responseJson.status === "ok") {
        window.location.assign('index.html');
        //window.location.href = 'index.html';
    } else if(responseJson.status === "error") {
        displayAlert(responseJson.message, "danger");
    }
}

async function submitUser(email, password){
    const userObject = { email: email, password: password }

    console.log("is submit - " + email + " , " + password)

    const response = await fetch('login', {
        method: 'post',
        body: JSON.stringify(userObject),
        headers: new Headers({
            'Content-Type': 'application/json'
        })
    });
    const result = await response.json();
    if(response.status === 200){
        handleServerResponse(result);
    }
}

function handleLoginSubmit(event){
    const email = (emailFieldEL.value).trim();
    const password = (passFieldEL.value).trim();

    if(email.length > 0 && password.length > 0){
        submitUser(email, password);
    } else {
        displayAlert("empty email or password", "danger");
    }

    event.preventDefault();
}


loginFormEL.addEventListener("submit", handleLoginSubmit);




