async function getReservationsForManager(urlParametersObject) {
    const result = await ajax({
        url: 'manage/reservations',
        method: 'get',
        urlParameters: urlParametersObject
    });

    if(result.status === "ok"){
        return result.message;
    } else
        displayAlert(result.message, "danger");
}

async function getBoatsForReservation(urlParametersObject) {
    const result = await ajax({
        url: 'manage/allocation/boats',
        method: 'get',
        urlParameters: urlParametersObject
    });

    if(result.status === "ok"){
        return result.message;
    } else {
        await displayManageReservationPage();
        displayAlert(result.message, "danger");
    }
}

async function editReservationForManager(reservation) {
    const result = await ajax({
        url: 'manage/reservations',
        method: 'put',
        jsonObject: reservation
    });

    if(result.status === "ok") {
        await displayManageReservationPage();
        displayAlert("Reservation updated successfully", "success");
    } else
        displayAlert(result.message, "danger");
}

async function unApproveReservationForManager(reservation) {
    const result = await ajax({
        url: 'manage/allocation/unapprove',
        method: 'post',
        jsonObject: reservation
    });

    if(result.status === "ok") {
        await displayManageReservationPage();
        displayAlert("Reservation unapproved successfully", "success");
    } else
        displayAlert(result.message, "danger");
}

async function approveReservationForManager(costumeReservation) {
    const result = await ajax({
        url: 'manage/allocation/approve',
        method: 'post',
        jsonObject: costumeReservation
    });

    if(result.status === "ok") {
        await displayManageReservationPage();
        displayAlert("Reservation approved successfully", "success");
    } else
        displayAlert(result.message, "danger");
}

async function deleteReservationForManager(reservation) {
    await deleteReservationForUser(reservation, 'manage');
}

async function splitReservationForManager(reservation) {
    const result = await ajax({
        url: 'manage/reservations/split',
        method: 'put',
        jsonObject: reservation
    });

    if(result.status === "ok") {
        await displayManageReservationPage();
        displayAlert("Reservation was split successfully", "success");
    } else
        displayAlert(result.message, "danger");
}


function getCheckedParticipantsFromForm(){
    const rowersCheckedOptions = document.querySelectorAll("input[name=splitParticipants]:checked");
    const rowersIdsList = [];
    rowersCheckedOptions.forEach(checkbox => {
        rowersIdsList.push(checkbox.id.split("__ID__")[1]);
    });
    return rowersIdsList;
}

function handleFormEditReservationSubmitForManager(event, originalReservation){
    const reservation = getReservationFromForm(originalReservation);
    delete reservation.isForCurrentUser;
    editReservationForManager(reservation);
    window.scroll(0,0);
    event.preventDefault();
}

function createFilterReservationForm(inline){
    const daysOptions = [];
    daysOptions.push({value: "All Week"})
    for(let i=0; i<7; i++)
        daysOptions.push({value: dateStringForTodayPlusRefDays(i, "dd/MM/yyyy")});

    let dateFilter = document.querySelector("#Form_reservationDateFilter");
    dateFilter !== null ? dateFilter = dateFilter.value : null;
    let statusFilter = document.querySelector("#Form_reservationStatusFilter");
    statusFilter !== null ? statusFilter = statusFilter.value : null;

    const filterReservationForm = createForm({inline: true,
        fields: [
            {type: "select", id: "Form_reservationDateFilter", options: daysOptions,
            selected: dateFilter !== null ? dateFilter : undefined},

            {type: "select", id: "Form_reservationStatusFilter",
                options: [{value:"All Reservations"}, {value:"Approved Reservations"},
                    {value:"Unapproved Reservations"}],
                selected: statusFilter !== null ? statusFilter : undefined},
        ]});
    return filterReservationForm;
}

function formatBoatForAllocation(boat){
    return "Boat serial number: " + boat.serialNumber + ", Name: " + boat.name + ", Code: " + boat.boatCode;
}

async function createAllocateBoatForReservationForm(boats){
    const availableBoatsOptions = [];
    boats.allAvailableBoatsForTime.forEach(b => {
        availableBoatsOptions.push({value: formatBoatForAllocation(b)})
    })
    const suitableBoat = boats.suitableBoatsForReservation.length > 0 ? boats.suitableBoatsForReservation[0] : null;
    const suitableBoatsString = [];
    boats.suitableBoatsForReservation.forEach(b =>{
        if(suitableBoatsString.length < 3)
            suitableBoatsString.push("<li>" + formatBoatForAllocation(b)) + "</li>";
    })

    const allocateBoatForReservationForm = createForm({fields: [
            {type: "number", label: "ID", id: "Form_reservationID", disabled: true, hidden: true},
            {type: "div", id: "Form_suitableBoatsMsg",
            html: suitableBoat !== null? "Suggested boat for this reservation:<ul class='mb-0'>" + suitableBoatsString.join("") + "</ul>"
                : "There are not suitable boats for this reservation by the number of rowers, size of boat and participants level."},
            {type: "select", label: "Choose boat to allocate", id: "Form_availableBoatsForReservation",
                options: availableBoatsOptions, selected: suitableBoat != null? formatBoatForAllocation(suitableBoat) : undefined},
            {type: "label", id: "form_requestedReservationBoats", hidden: true},
            {type: "label", id: "form_numOfRowersInReservation", hidden: true},
            {type: "submit", text: "Submit", id: "Form_allocateSubmit", color: "info"}
        ]});
    return allocateBoatForReservationForm;
}

async function createSplitReservationForm(reservation){
    const allRowersMap = await getRowersNameSerialNumberMap();
    const fields = [];

    fields.push({type: "number", label: "ID", id: "Form_reservationID", value: reservation.id ,disabled: true, hidden: true});
    fields.push({type: "label", label:"Choose participants for new reservation:"});

    reservation.participants.forEach(rowerSerialNumber => {fields.push(
            {type: "checkbox", label: allRowersMap[rowerSerialNumber], id: "Form_splitRowerId__ID__" + rowerSerialNumber, name: "splitParticipants"}
            )});
    fields.push({type: "submit", text: "Submit", id: "Form_splitSubmit", color: "info"});

    return createForm({fields: fields});
}

function updateAllocateBoatForReservationFormForManager(formEL, reservation) {
    document.querySelector("#Form_suitableBoatsMsg").className = "alert alert-primary";
    setTextFieldValues(formEL, {id: "Form_reservationID", value: reservation.id});

    const requestedReservationBoatsEl = document.querySelector("#form_requestedReservationBoats");
    requestedReservationBoatsEl.style.display = "block";
    requestedReservationBoatsEl.innerHTML = "Requested Boats: " + reservation.boatType.join(", ");

    const numOfRowersInReservationEl = document.querySelector("#form_numOfRowersInReservation");
    numOfRowersInReservationEl.style.display = "block";
    numOfRowersInReservationEl.innerHTML = "Num Of Rowers: " + reservation.participants.length;
}

async function updateReservationExtraFormValuesForManager(formEL, reservation) {
    const allRowersMap = await getRowersNameSerialNumberMap();

    updateRowerListLabelsByIdsList(reservation.participants, allRowersMap);
    setCheckBoxFieldValues(formEL,{id:"Form_reservationForYou", hidden: true});
    setTextFieldValues(formEL,{id:"Form_reservationOrderMemberName", hidden: false});
}

async function displayEditReservationForManagerPage(event, reservation){
    cleanMainContent("Edit Reservation (Id: " + reservation.id + ")");
    const addReservationFormEL = await createNewReservationForm();
    contentEL.appendChild(addReservationFormEL);
    await updateReservationEditFormValues(addReservationFormEL, reservation);
    await updateReservationExtraFormValuesForManager(addReservationFormEL, reservation);
    document.querySelectorAll('input[type=radio][name="wantToEditActivity"]').forEach(radio => {
        radio.addEventListener('change', displayActivityFields);
    });
    addReservationFormEL.addEventListener("submit", function(event){
        handleFormEditReservationSubmitForManager(event, reservation);
    });
}

async function handleFormAllocateBoatSubmit(event){
    clearAlert();
    const selectedBoatString = document.querySelector("#Form_availableBoatsForReservation").value;

    const boatId = parseInt(selectedBoatString.split(",")[0].split(":")[1]);
    const reservationId = parseInt(document.querySelector("#Form_reservationID").value);

    approveReservationForManager({id: reservationId, allocatedBoatID: boatId}).then();

    window.scroll(0,0);
    event.preventDefault();
}

async function handleFormSplitReservationSubmit(event){
    const newRowersIdList = getCheckedParticipantsFromForm();
    const reservationId = document.querySelector("#Form_reservationID").value;

    splitReservationForManager({id: reservationId, participants: newRowersIdList}).then();

    window.scroll(0,0);
    event.preventDefault();
}

async function displayAllocateBoatReservationPage(reservation){
    const BoatsForReservation = await getBoatsForReservation({id: reservation.id});
    if(BoatsForReservation.allAvailableBoatsForTime.length === 0){
        await displayManageReservationPage();
        displayAlert("There are no available boats for this reservation time", "warning");
    } else {
        cleanMainContent("Allocate Boat for Reservation (Id: " + reservation.id + ")");
        const AllocateBoatForReservationForm = await createAllocateBoatForReservationForm(BoatsForReservation);
        contentEL.appendChild(AllocateBoatForReservationForm);
        updateAllocateBoatForReservationFormForManager(AllocateBoatForReservationForm, reservation);
        AllocateBoatForReservationForm.addEventListener("submit", handleFormAllocateBoatSubmit);
    }
}

async function displaySplitReservationPage(reservation){
    cleanMainContent("Split Reservation Participants");
    const splitReservationForm = await createSplitReservationForm(reservation);
    contentEL.appendChild(splitReservationForm);
    splitReservationForm.addEventListener("submit", handleFormSplitReservationSubmit);
}

function addReservationManageEventListeners(reservations){
    // add listeners for delete, approve, unapprove and edit
    reservations.forEach((reservation)=>{
        if(reservation.isApproved === false){
            document.querySelector("#editReservation" + reservation.id).addEventListener('click', async function(event){
                await displayEditReservationForManagerPage(event, reservation);
            });
            document.querySelector("#deleteReservation" + reservation.id).addEventListener('click', async function(event){
                await deleteReservationForManager(reservation);
            });
            document.querySelector("#allocateBoatForReservation" + reservation.id).addEventListener('click', async function(event){
                await displayAllocateBoatReservationPage(reservation);
            });
            if(reservation.participants.length > 1)
                document.querySelector("#splitReservation" + reservation.id).addEventListener('click', async function(event){
                    await displaySplitReservationPage(reservation);
                });
        } else {
            document.querySelector("#unapproveReservation" + reservation.id).addEventListener('click', async function(event){
                await unApproveReservationForManager(reservation);
            });
        }
    });
}

async function displayReservationTableByFilter(){
    const tableContentEl = document.querySelector("#reservationTableContent");
    let dateFilter = document.querySelector("#Form_reservationDateFilter").value;
    let statusFilter = document.querySelector("#Form_reservationStatusFilter").value;
    let isForWeek = false;

    if(dateFilter.toLowerCase() === "all week"){
        dateFilter = dateStringForTodayPlusRefDays();
        isForWeek = true;
    }
    if(statusFilter.toLowerCase().startsWith("approved"))
        statusFilter = "approved";
    else if(statusFilter.toLowerCase().startsWith("unapproved"))
        statusFilter = "unapproved";
    else
        statusFilter = "all";

    const reservations = await getReservationsForManager({
        date: dateFilter,
        is_for_week: isForWeek,
        status: statusFilter
    });

    let formattedReservations = await formatReservationsObject(reservations);
    for(let i=0; i<reservations.length; i++){
        if(reservations[i].isApproved === true)
            formattedReservations[i].Actions = '<button type="button" class="btn btn-sm btn-warning" id="unapproveReservation' + reservations[i].id + '">Unapprove</button> ';
        else{
            if(reservations[i].participants.length > 1)
                formattedReservations[i].Actions += '<button type="button" class="btn btn-sm btn-info" id="splitReservation' + reservations[i].id + '">Split</button> ';
            formattedReservations[i].Actions += '<button type="button" class="btn btn-sm btn-success" id="allocateBoatForReservation' + reservations[i].id + '">Approve</button> ';
        }
    }
    const table = createTable(formattedReservations);
    tableContentEl.innerHTML = table.outerHTML;

    addReservationManageEventListeners(reservations);
}


async function displayManageReservationPage(){
    const filterForm = createFilterReservationForm(true);
    cleanMainContent("Reservations");
    contentEL.appendChild(filterForm);

    const tableContentEl = document.createElement("div");
    tableContentEl.id = "reservationTableContent";
    contentEL.appendChild(tableContentEl);
    filterForm.addEventListener('change', displayReservationTableByFilter);

    await displayReservationTableByFilter();
}