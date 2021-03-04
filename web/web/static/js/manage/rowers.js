async function getRowersForManager(urlParametersObject, permissions='user') {
    return getRowersForUser(urlParametersObject, 'manage');
}


async function editRower(rower) {
    const result = await ajax({
        url: 'manage/rowers',
        method: 'put',
        jsonObject: rower
    });

    if(result.status === "ok") {
        await displayRowersPage();
        displayAlert("Rower '" + rower.serialNumber +  "' updated successfully", "success");
    } else
        displayAlert(result.message, "danger");
}

async function deleteRower(rower) {
    const result = await ajax({
        url: 'manage/rowers',
        method: 'delete',
        jsonObject: rower
    });

    if(result.status === "ok") {
        await displayRowersPage();
        displayAlert("Rower '" + rower.serialNumber +  "' deleted successfully", "success");
    } else
        displayAlert(result.message, "danger");
}

async function addRower(rower) {
    const result = await ajax({
        url: 'manage/rowers',
        method: 'post',
        jsonObject: rower
    });

    if(result.status === "ok") {
        await displayRowersPage();
        displayAlert("Rower '" + rower.name +  "' added successfully", "success");
    } else
        displayAlert(result.message, "danger");
}

function handleFormAddRowerSubmit(event){
    const rower = getRowerFromForm();
    addRower(rower);
    window.scroll(0,0);
    event.preventDefault();
}

function handleFormEditRowerSubmit(event){
    const rower = getRowerFromForm();
    editRower(rower);
    window.scroll(0,0);
    event.preventDefault();
}

function getRowerFromForm(){
    const rowerNotes = document.querySelector("#Form_rowerNotes").value.trim();
    const rowerLevel = document.querySelector("#Form_rowerLevel").value.trim();
    const rowerBoatSerialNumber = document.querySelector("#Form_rowerBoatSerial").value.trim();
    const rowerPhoneNumber = document.querySelector("#Form_rowerPhoneNumber").value.trim();
    const rowerAge = document.querySelector("#Form_rowerAge").value.trim();
    const rowerPassword = document.querySelector("#Form_rowerPassword").value.trim();

    const rower = {
        serialNumber: document.querySelector("#Form_rowerSerialNumber").value,
        name: document.querySelector("#Form_rowerName").value,
        email: document.querySelector("#Form_rowerEmail").value,
        password: rowerPassword === "" ? null : rowerPassword,
        isManager: document.querySelector("#Form_rowerManager").checked,
        age: rowerAge === "" ? null : rowerAge,
        notes: rowerNotes === "" ? null : rowerNotes,
        level:  rowerLevel === "" ? null : rowerLevel,
        boatSerialNumber: rowerBoatSerialNumber === "" ? null : rowerBoatSerialNumber,
        phoneNumber: rowerPhoneNumber === "" ? null : rowerPhoneNumber
    }
    return rower;
}

function createRowerForm(){
    const rowerForm = createForm({fields: [
            {type: "number", label: "Serial Number", id: "Form_rowerSerialNumber", required: true},
            {type: "text", label: "Name", id: "Form_rowerName", required: true},
            {type: "password", label: "Password", id: "Form_rowerPassword", required: true},
            {type: "email", label: "Email", id: "Form_rowerEmail", placeholder: "example@bms.com", required: true},
            {type: "number", label: "Boat Serial Number (optional)", id: "Form_rowerBoatSerial"},
            {type: "number", label: "Age", id: "Form_rowerAge"},
            {type: "select", label: "Level", id: "Form_rowerLevel", options: [{value:"BEGINNER"}, {value:"INTERMEDIATE"}, {value:"ADVANCED"}]},
            {type: "text", label: "Notes", id: "Form_rowerNotes"},
            {type: "text", label: "Phone Number", id: "Form_rowerPhoneNumber"},
            {type: "checkbox", label: "Is Manager", id: "Form_rowerManager"},
            {type: "submit", text: "Submit", id: "Form_rowerSubmit", color: "info"}
        ]});
    return rowerForm;
}

function updateRowerEditFormValues(formEL, rower){
    setTextFieldValues(formEL,{id:"Form_rowerSerialNumber", value:rower.serialNumber, disabled:true});
    setTextFieldValues(formEL,{id:"Form_rowerPassword", value:"", required:false, label: "New Password"});
    setTextFieldValues(formEL,{id:"Form_rowerName", value:rower.name});
    setTextFieldValues(formEL,{id:"Form_rowerEmail", value:rower.email});
    setTextFieldValues(formEL,{id:"Form_rowerBoatSerial", value:rower.boatSerialNumber});
    setTextFieldValues(formEL,{id:"Form_rowerAge", value:rower.age});
    setTextFieldValues(formEL,{id:"Form_rowerNotes", value:rower.notes});
    setTextFieldValues(formEL,{id:"Form_rowerPhoneNumber", value:rower.phoneNumber});
    setCheckBoxFieldValues(formEL, {id: "Form_rowerManager", checked: rower.isManager});
    setSelectFieldValues(formEL, {id: "Form_rowerLevel", selected: rower.level});
}

function formatRowersObject(rowersFromServer){
    let rowers = [];
    rowersFromServer.forEach(r => {
        rowers.push({
            "Serial Number": r.serialNumber,
            "Name": r.name,
            "Email": r.email,
            "Level": r.level,
            "Info": (r.age !== undefined ? "Age: " + r.age + "<br>" : "") +
                (r.notes !== undefined ? "Notes: " + r.notes + "<br>" : "") +
                (r.phoneNumber !== undefined ? "Phone Number: " + r.phoneNumber + "<br>" : "") +
                (r.hasPrivateBoat === true ? "Has Private Boat: " + r.hasPrivateBoat + " (" + r.boatSerialNumber + ") <br>" : ""),
            "Join Date": dateObjectToString(r.joinDate),
            "Expire Dare": dateObjectToString(r.expireDate),
            "Is Manager": r.isManager === true ? "Yes" : "",
            "Actions": '<button type="button" class="btn btn-sm btn-primary" id="editRower' + r.serialNumber + '">Edit</button> ' +
                       '<button type="button" class="btn btn-sm btn-danger" id="deleteRower' + r.serialNumber + '">Delete</button>'
        })
    });
    return rowers;
}

function displayAddNewRowerPage(){
    cleanMainContent("Add Rower");
    const addRowerFormEL = createRowerForm();
    contentEL.appendChild(addRowerFormEL);
    addRowerFormEL.addEventListener("submit", handleFormAddRowerSubmit);
}

function displayEditRowerPage(event, rower){
    cleanMainContent("Edit Rower " + rower.serialNumber);
    const editRowerFormEL = createRowerForm();
    updateRowerEditFormValues(editRowerFormEL, rower);
    contentEL.appendChild(editRowerFormEL);
    editRowerFormEL.addEventListener("submit", handleFormEditRowerSubmit);
}

async function displayRowersPage(){
    cleanMainContent("Rowers");

    const createRowerBtnEL = createButton({id: "displayCreateRower", color: "info", text: "Add Rower"});
    contentEL.appendChild(createRowerBtnEL);
    createRowerBtnEL.addEventListener('click', displayAddNewRowerPage);

    const importRowerBtnEL = createButton({id: "importRower", color: "primary", text: "Import"});
    contentEL.appendChild(importRowerBtnEL);
    importRowerBtnEL.addEventListener('click', function(event){
        displayImportXmlPage({
            type: "rowers"
        })
    });

    const exportRowerBtnEL = createButton({id: "exportRower", color: "primary", text: "Export"});
    contentEL.appendChild(exportRowerBtnEL);
    exportRowerBtnEL.addEventListener('click', function(event){
        exportXML("rowers")
    });

    const rowers = await getRowersForManager({});
    const formattedRowers = formatRowersObject(rowers);

    const table = createTable(formattedRowers);
    contentEL.appendChild(table);

    // add listeners for delete and edit
    rowers.forEach((rower)=>{
        document.querySelector("#editRower" + rower.serialNumber).addEventListener('click', function(event){
            displayEditRowerPage(event, rower);
        });
        document.querySelector("#deleteRower" + rower.serialNumber).addEventListener('click', function(event){
            deleteRower(rower);
        });
    });
}