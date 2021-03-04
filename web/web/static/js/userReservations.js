async function getReservationsForUser(urlParametersObject) {
    const result = await ajax({
        url: 'user/reservations',
        method: 'get',
        urlParameters: urlParametersObject
        });


    if(result.status === "ok"){
        return result.message;
    } else
        displayAlert(result.message, "danger");
}

async function editReservationForUser(reservation) {
    const result = await ajax({
        url: 'user/reservations',
        method: 'put',
        jsonObject: reservation
    });

    if(result.status === "ok") {
        await displayReservationHomePage();
        displayAlert("Reservation updated successfully", "success");
    } else
        displayAlert(result.message, "danger");
}

async function addReservation(reservation) {
    const result = await ajax({
        url: 'user/reservations',
        method: 'post',
        jsonObject: reservation
    });

    if(result.status === "ok") {
        await displayReservationHomePage();
        displayAlert("Reservation added successfully", "success");
    } else
        displayAlert(result.message, "danger");
}

function handleFormAddUserReservationSubmit(event){
    clearAlert();

    const reservation = getReservationFromForm();
    addReservation(reservation);

    window.scroll(0,0);
    event.preventDefault();
}

function handleFormEditReservationSubmitForUser(event, originalReservation){
    const reservation = getReservationFromForm(originalReservation);
    editReservationForUser(reservation);
    window.scroll(0,0);
    event.preventDefault();
}


function updateRowersSuggestionList(rowers){
    const suggestionPlaceEl = document.querySelector('#Form_reservationRowersSuggestions');

    const rowersHtml = [];
    rowers.forEach(r=>{
        rowersHtml.push('<button type="button" class="btn btn-sm btn-link" id="addRowerToList_' + r.serialNumber + '">' + r.name + '</button>');
    })

    if(rowers.length > 0){
        suggestionPlaceEl.innerHTML = "Available Rowers: " + rowersHtml.join(", ");
        suggestionPlaceEl.setAttribute("style", "display: block;")
    } else {
        suggestionPlaceEl.innerHTML = "";
        suggestionPlaceEl.setAttribute("style", "display: none;")
    }

    rowers.forEach(r=>{
        document.querySelector('#addRowerToList_' + r.serialNumber).addEventListener('click', function(event){
            addToRowersList(r);
        });
    });
}

function getRowersIdByDivElement(rowerElement){
    return parseInt(rowerElement.split("</span>")[0].split(">")[1]);
}

function getRowersIdsFromForm(rowersList){
    const idsList = [];
    rowersList.forEach(r => {
        idsList.push(getRowersIdByDivElement(r)); // get serialNumber
    });
    return idsList;
}

function addToRowersList(rowerToAdd){
    const rowersPlaceEl = document.querySelector('#Form_reservationRowers');
    const newItem = "<span style='display: none;'>" + rowerToAdd.serialNumber + "</span><a href='javascript:void(0)' class='badge badge-secondary'" +
        " id='#deleteRowerFromList_" + rowerToAdd.serialNumber + "'>" + rowerToAdd.name + " (x)</a>";

    let rowersHtml = [];
    if(rowersPlaceEl.innerHTML !== "")
        rowersHtml = rowersPlaceEl.innerHTML.split(', ');

    const rowersIds = getRowersIdsFromForm(rowersHtml);

    if(rowersIds.includes(rowerToAdd.serialNumber) !== true)
        rowersHtml.push(newItem);

    displayRowers(rowersHtml);

}

function deleteRowersFromList(rowerSerialNumber){
    const rowersPlaceEl = document.querySelector('#Form_reservationRowers');

    let oldRowersHtml = rowersPlaceEl.innerHTML.split(', ');
    let newRowersHtml = [];

    oldRowersHtml.forEach((divElement)=> {
        if(getRowersIdByDivElement(divElement) !== rowerSerialNumber)
            newRowersHtml.push(divElement);
    });

    displayRowers(newRowersHtml);
}

function displayRowers(rowers){
    updateRowersSuggestionList([]);
    document.querySelector('#Form_searchReservationRowers').value = "";
    const rowersPlaceEl = document.querySelector('#Form_reservationRowers');

    if(rowers.length > 0){
        rowersPlaceEl.innerHTML = rowers.join(", ");
        rowersPlaceEl.setAttribute("style", "display: block;")
    } else {
        rowersPlaceEl.innerHTML = "";
        rowersPlaceEl.setAttribute("style", "display: none;")
    }

    rowers.forEach(r=>{
        document.getElementById('#deleteRowerFromList_' + getRowersIdByDivElement(r)).addEventListener('click', function(event){
            deleteRowersFromList(getRowersIdByDivElement(r));
        });
    });
}

async function displayRowersByNameForReservation(){
    const rowers = await getRowersForUser({
        name: document.querySelector('#Form_searchReservationRowers').value
    });
    updateRowersSuggestionList(rowers);
}

function getBoatTypeFromReservationForm(){
    let boatType = [];
    if(document.querySelector("#Form_reservationOneRower").checked)
        boatType.push("ONE");
    if(document.querySelector("#Form_reservationTwoRower").checked)
        boatType.push("TWO");
    if(document.querySelector("#Form_reservationFourRower").checked)
        boatType.push("FOUR");
    if(document.querySelector("#Form_reservationEightRower").checked)
        boatType.push("EIGHT");
    return boatType;
}

function getActivityFromReservationForm(originalReservation){
    const activity = {};
    const activitySelectEl = document.querySelector("#Form_reservationActivity");
    const wantsToEditRadioEl = document.querySelector("#Form_wantsToEditActivity");

    if(originalReservation !== undefined)
        if(wantsToEditRadioEl.checked === false && wantsToEditRadioEl.hidden !== true)
            return originalReservation.activity;

    if(activitySelectEl !== null){ // select field
        activitySelectEl.querySelectorAll("option").forEach(option => {
            if(option.value === activitySelectEl.value)
                activity.id = option.id.split("__id__")[1];
        });

    } else { // starTime and finishTime
        activity.startTime = timeStringToObject(document.querySelector("#Form_activityStartTime").value);
        activity.finishTime = timeStringToObject(document.querySelector("#Form_activityFinishTime").value);
    }
    return activity;
}

function getReservationFromForm(originalReservation){
    const rowersPlaceEl = document.querySelector('#Form_reservationRowers');
    const boatType = getBoatTypeFromReservationForm();
    const reservationId = document.querySelector("#Form_reservationID").value.trim();
    const activity = getActivityFromReservationForm(originalReservation);

    let rowersHtml = [];
    if(rowersPlaceEl.innerHTML !== "")
        rowersHtml = rowersPlaceEl.innerHTML.split(', ');
    const rowersIds = getRowersIdsFromForm(rowersHtml);

    const reservation = {
        id: reservationId !== "" ? parseInt(reservationId) : null,
        activity: activity,
        activityDate: dateStringToObject(document.querySelector("#Form_reservationDate").value),
        boatType: boatType,
        participants: rowersIds,
        isForCurrentUser: document.querySelector("#Form_reservationForYou").checked
    }

    return reservation;
}

async function createNewReservationForm(){
    const activities = await getActivitiesForUser();
    const activitiesOptions = [];
    activities.forEach(a=>{
        activitiesOptions.push({value: getActivityFormattedString(a), id: a.id})
    });

    const reservationForm = createForm({fields: [
            {type: "number", label: "ID", id: "Form_reservationID", disabled: true, hidden: true},
            {type: "date", label: "Date", id: "Form_reservationDate", required: true, min: "2018-01-01"},

            {type: "radio", id: "Form_wantsToKeepActivity", name: "wantToEditActivity",
                checked: true, hidden: true},
            {type: "radio", label: "Change Activity", id: "Form_wantsToEditActivity", name: "wantToEditActivity",
                hidden: true},

            activities.length === 0 ? {type: "time", label: "Start Time", id: "Form_activityStartTime",
                required: true} : null,
            activities.length === 0 ? {type: "time", label: "Finish Time", id: "Form_activityFinishTime",
                required: true} : null,
            activities.length !== 0 ? {type: "select", label: "Activity", id: "Form_reservationActivity",
                options: activitiesOptions, required: true} : null,
            {type: "label", label: "Preferred number of rowers", required: true},
            {type: "checkbox", label: "One", id: "Form_reservationOneRower", inline: true},
            {type: "checkbox", label: "Two", id: "Form_reservationTwoRower", inline: true},
            {type: "checkbox", label: "Four", id: "Form_reservationFourRower", inline: true},
            {type: "checkbox", label: "Eight", id: "Form_reservationEightRower", inline: true},

            {type: "text", label: "Reservation Owner", id: "Form_reservationOrderMemberName", disabled: true, hidden: true},

            {type: "checkbox", label: "The reservation is for you", id: "Form_reservationForYou", checked: true},
            {type: "label", label: "Rowers List"},
            {type: "div", id: "Form_reservationRowers"},
            {type: "text", id: "Form_searchReservationRowers", placeholder: "Search rower", hideLabel: true},
            {type: "label", label: "", id: "Form_reservationRowersSuggestions", hidden: true},
            {type: "submit", text: "Submit", id: "Form_rowerSubmit", color: "info"}
        ]});
    reservationForm.querySelector("#Form_searchReservationRowers").addEventListener('keyup', displayRowersByNameForReservation);
    return reservationForm;
}

function getActivityFormattedString(activity){
    return timeObjectToString(activity.startTime) + "-" + timeObjectToString(activity.finishTime) +
        (activity.name !== undefined ? ", " + activity.name : "") +
        (activity.boatType !== undefined ? ", " + activity.boatType : "");
}

function updateRowerListLabelsByIdsList(idsList, rowersMap){
    idsList.forEach(participantId => {
        addToRowersList({serialNumber:participantId, name: rowersMap[participantId]});
    });
}

async function updateReservationEditFormValues(formEL, reservation){
    const currentUserSerialNumber = getCurrentUserSerialNumber();
    const allRowersMap = await getRowersNameSerialNumberMap();
    const displayedActivity = getActivityFormattedString(reservation.activity);

    const activities = await getActivitiesForUser();
    const activitiesOptions = [];
    activities.forEach(a=>{
        activitiesOptions.push({value: getActivityFormattedString(a), id: a.id})
    });

    const participantsWithoutCurrentUser = [...reservation.participants];
    if(participantsWithoutCurrentUser.includes(parseInt(currentUserSerialNumber)))
        participantsWithoutCurrentUser.pop(parseInt(currentUserSerialNumber));
    updateRowerListLabelsByIdsList(participantsWithoutCurrentUser, allRowersMap);

    const isForCurrentUser = reservation.participants.includes(currentUserSerialNumber);
    setCheckBoxFieldValues(formEL,{id:"Form_reservationForYou", checked:isForCurrentUser});

    setTextFieldValues(formEL,{id:"Form_reservationID", value:reservation.id});
    setTextFieldValues(formEL,{id:"Form_reservationDate", value:dateObjectToString(reservation.activityDate)});
    setCheckBoxFieldValues(formEL,{id:"Form_wantsToKeepActivity", hidden: false,
        label: "Current Activity: " + displayedActivity});
    setCheckBoxFieldValues(formEL,{id:"Form_wantsToEditActivity", hidden: false});
    setCheckBoxFieldValues(formEL,{id:"Form_reservationOneRower", checked:reservation.boatType.includes("ONE")});
    setCheckBoxFieldValues(formEL,{id:"Form_reservationTwoRower", checked:reservation.boatType.includes("TWO")});
    setCheckBoxFieldValues(formEL,{id:"Form_reservationFourRower", checked:reservation.boatType.includes("FOUR")});
    setCheckBoxFieldValues(formEL,{id:"Form_reservationEightRower", checked:reservation.boatType.includes("EIGHT")});
    setTextFieldValues(formEL,{id:"Form_reservationOrderMemberName", value:allRowersMap[reservation.orderedMemberID]});

    if(activities.length === 0){ // no activities, display hour
        setTextFieldValues(formEL,{id:"Form_activityStartTime", disabled:true, required: false});
        setTextFieldValues(formEL,{id:"Form_activityFinishTime", disabled:true, required: false});
    } else { // display select
        setSelectFieldValues(formEL, {id: "Form_reservationActivity", disabled:true, required: false, hideLabel: true});
    }

}

function displayActivityFields(event){
    const elementIds = ["Form_activityStartTime", "Form_activityFinishTime", "Form_reservationActivity"];
    let required = true;
    let disabled = false;

    if(this.id === "Form_wantsToKeepActivity"){
        required = false;
        disabled = true;
    }

    elementIds.forEach(elementID =>{
        const EL = document.querySelector("#" + elementID);
        if(EL !== null){
            EL.required = required;
            EL.disabled = disabled;
        }
    });
}

async function formatReservationsObject(ReservationsFromServer, displayActions=true){
    const rowersMap = await getRowersNameSerialNumberMap();

    let reservations = [];
    ReservationsFromServer.forEach(r => {

        let participantsNames = [];
        r.participants.forEach(p=> {
            participantsNames.push(rowersMap[p])
        })

        reservations.push({
            "Reservetion date": dateObjectToString(r.activityDate) +
                "<br>" + timeObjectToString(r.activity.startTime) + " - " + timeObjectToString(r.activity.finishTime),
            "Info": "Requested Boat Type: " + r.boatType.join(", ") +
                "<br>Participants: " + participantsNames.join(", "),
            "Ordered By": rowersMap[r.orderedMemberID] + "<br>on " + dateObjectToString(r.orderDate.date) + "-" + timeObjectToString(r.orderDate.time),
            "Status": r.isApproved === true ? "Approved (Boat " + r.allocatedBoatID + ")" : "Not Approved",

            "Actions": displayActions===true? (r.isApproved === true ? "-" : '<button type="button" class="btn btn-sm btn-primary" id="editReservation' + r.id + '">Edit</button> ' +
                        '<button type="button" class="btn btn-sm btn-danger" id="deleteReservation' + r.id + '">Delete</button> ') : "-"
        })
    });
    return reservations;
}


async function displayAddNewReservationPage(){
    cleanMainContent("Create New Reservation");
    const addReservationFormEL = await createNewReservationForm();
    contentEL.appendChild(addReservationFormEL);
    addReservationFormEL.addEventListener("submit", handleFormAddUserReservationSubmit);
}

async function displayEditReservationPage(event, reservation){
    cleanMainContent("Edit Reservation " + reservation.id);
    const addReservationFormEL = await createNewReservationForm();
    contentEL.appendChild(addReservationFormEL);
    await updateReservationEditFormValues(addReservationFormEL, reservation);
    document.querySelectorAll('input[type=radio][name="wantToEditActivity"]').forEach(radio => {
        radio.addEventListener('change', displayActivityFields);
    });
    addReservationFormEL.addEventListener("submit", function(event){
        handleFormEditReservationSubmitForUser(event, reservation);
    });
}


async function displayReservationHistoryForUserPage(){
    cleanMainContent("My Reservations History");
    const showFutureReservationBtnEL = createButton({id: "showReservationFuture", color: "primary", text: "Show Future Reservations"});
    contentEL.appendChild(showFutureReservationBtnEL);
    showFutureReservationBtnEL.addEventListener('click', displayReservationHomePage);

    const reservations = await getReservationsForUser({for_time: "history"});
    const formattedReservations = await formatReservationsObject(reservations, false);

    const table = createTable(formattedReservations);
    contentEL.appendChild(table);
}

async function displayReservationHomePage(){
    cleanMainContent("My Reservations");

    const createReservationBtnEL = createButton({id: "displayCreateReservation", color: "info", text: "New Reservation"});
    contentEL.appendChild(createReservationBtnEL);
    createReservationBtnEL.addEventListener('click', displayAddNewReservationPage);

    const showReservationHistoryBtnEL = createButton({id: "showReservationHistory", color: "primary", text: "Show Reservation History"});
    contentEL.appendChild(showReservationHistoryBtnEL);
    showReservationHistoryBtnEL.addEventListener('click', displayReservationHistoryForUserPage);


    const reservations = await getReservationsForUser({
        for_time: "future",
        only_unapproved: "false"
    });
    const formattedReservations = await formatReservationsObject(reservations);

    const table = createTable(formattedReservations);
    contentEL.appendChild(table);

    // add listeners for delete and edit
    reservations.forEach((reservation)=>{
        if(reservation.isApproved === false){
            document.querySelector("#editReservation" + reservation.id).addEventListener('click', function(event){
                displayEditReservationPage(event, reservation);
            });
            document.querySelector("#deleteReservation" + reservation.id).addEventListener('click', function(event){
                deleteReservationForUser(reservation);
            });
        }
    });
}