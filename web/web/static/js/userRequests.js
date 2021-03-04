async function getActivitiesForUser() {
    const result = await ajax({
        url: 'user/activities',
        method: 'get'
    });

    if(result.status === "ok"){
        return result.message;
    } else
        displayAlert(result.message, "danger");
}

async function getRowersForUser(urlParametersObject, permissions='user') {
    const result = await ajax({
        url: permissions + '/rowers',
        method: 'get',
        urlParameters: urlParametersObject
    });

    if(result.status === "ok"){
        return result.message;
    } else
        displayAlert(result.message, "danger");
}

async function deleteReservationForUser(reservation, permissions='user') {
    const result = await ajax({
        url: permissions + '/reservations',
        method: 'delete',
        jsonObject: reservation
    });

    if(result.status === "ok") {
        permissions === 'manage' ? await displayManageReservationPage() : await displayReservationHomePage();
        displayAlert("Reservation deleted successfully", "success");
    } else
        displayAlert(result.message, "danger");
}

async function getCurrentUserInfo(urlParametersObject) {
    return getRowersForUser({
        currentUserInfo: "true"
    });
}

async function getRowersNameSerialNumberMap(){
    const rowers = await getRowersForUser();
    const rowersMap = {};

    rowers.forEach(r => {
        rowersMap[r.serialNumber] = r.name;
    });

    return rowersMap;
}

async function displayEditProfilePage(event){
    cleanMainContent("Edit Profile");
    const editRowerFormEL = await createEditProfileForm();
    contentEL.appendChild(editRowerFormEL);
    editRowerFormEL.addEventListener("submit", handleFormEditProfileSubmit);
}

async function createEditProfileForm(){
    const userInfo = await getCurrentUserInfo();

    const rowerForm = createForm({fields: [
            {type: "text", label: "Name", id: "Form_rowerName", value: userInfo.name, required: true},
            {type: "password", label: "Password", id: "Form_rowerPassword"},
            {type: "email", label: "Email", id: "Form_rowerEmail", placeholder: "example@bms.com", value:userInfo.email, required: true},
            {type: "text", label: "Phone Number", id: "Form_rowerPhoneNumber", value: userInfo.phoneNumber},
            {type: "submit", text: "Submit", id: "Form_rowerSubmit", color: "info"}
        ]});
    return rowerForm;
}

async function handleFormEditProfileSubmit(event){
    const rower = getRowerFromEditProfileForm();
    console.log(rower);
    editProfile(rower).then();
    window.scroll(0,0);
    event.preventDefault();
}

function getRowerFromEditProfileForm(){
    const rowerPhoneNumber = document.querySelector("#Form_rowerPhoneNumber").value.trim();
    const rowerPassword = document.querySelector("#Form_rowerPassword").value.trim();

    const rower = {
        name: document.querySelector("#Form_rowerName").value,
        email: document.querySelector("#Form_rowerEmail").value,
        password: rowerPassword === "" ? null : rowerPassword,
        phoneNumber: rowerPhoneNumber === "" ? null : rowerPhoneNumber
    }
    return rower;
}

async function editProfile(rower) {
    const result = await ajax({
        url: 'user/rowers',
        method: 'put',
        jsonObject: rower
    });

    if(result.status === "ok") {
        await displayReservationHomePage();
        buildMenu().then();
        displayAlert("Profile Date updated successfully", "success");
    } else
        displayAlert(result.message, "danger");
}

let notificationsInterval = setInterval(getNotificationsForUser, 5000);

async function getNotificationsForUser(permissions='user'){
    const result = await ajax({
        url: permissions + '/notifications',
        method: 'get'
    });

    if(result.status === "ok") {
        if(permissions === "manage")
            return result.message.globalNotifications;
        else{
            displayAllUserNotifications(result.message.userNotifications);
            displayGeneralNotifications(result.message.globalNotifications);
        }
    } else if(result.status === "expired"){
        cleanMainContent("");
        clearInterval(notificationsInterval);
        displayAlert(result.message, "warning");
        setTimeout(function(){window.location.assign('login.html')}, 4000);
    } else
        displayAlert(result.message, "danger");
}


function displayGeneralNotifications(notifications){
    const displayNotificationsEL = document.querySelector("#globalNotifications");

    if(notifications.length > 0){
        const notificationsMsg = [];
        notifications.forEach(n => {
            notificationsMsg.push("<li>" + n.message + "</li>");
        });
        displayNotificationsEL.innerHTML = "<strong>System Messages: </strong><ul class='mb-1'>" + notificationsMsg.join("") + "</ul>";
        displayNotificationsEL.style.display = "block";
    } else {
        displayNotificationsEL.style.display = "none";
        displayNotificationsEL.innerHTML = "";
    }
}

function displayAllUserNotifications(notifications){
    const displayNotificationsEL = document.querySelector("#displayNotifications");
    notifications.forEach(n => {
        displayOneUserNotification(displayNotificationsEL, n);
    });
}

function displayOneUserNotification(displayPlaceEl, notificationInfo){
    const tmpListItemEl = document.createElement("li");
    tmpListItemEl.className = "alert alert-primary";
    tmpListItemEl.innerText = notificationInfo.message;
    tmpListItemEl.id = "notification_" + notificationInfo.id;
    displayPlaceEl.appendChild(tmpListItemEl);
    setTimeout(function (){
        deleteNotificationFromDisplay(displayPlaceEl, notificationInfo);
    }, 30000);
}

function deleteNotificationFromDisplay(displayPlaceEl, notificationInfo){
    const notificationsEL = document.querySelector("#notification_" + notificationInfo.id);
    if(notificationsEL !== null)
        displayPlaceEl.removeChild(notificationsEL);
}

// GENERAL
function logout(){
    window.location.assign('logout');
}