async function getBoats() {
    const result = await ajax({
        url: 'manage/boats',
        method: 'get'
    });

    if(result.status === "ok"){
        return result.message;
    } else
        displayAlert(result.message, "danger");
}

async function editBoat(boat) {
    const result = await ajax({
        url: 'manage/boats',
        method: 'put',
        jsonObject: boat
    });

    if(result.status === "ok") {
        await displayBoatsPage();
        displayAlert("Boat '" + boat.serialNumber +  "' updated successfully", "success");
    } else
        displayAlert(result.message, "danger");
}

async function deleteBoat(boat) {
    const result = await ajax({
        url: 'manage/boats',
        method: 'delete',
        jsonObject: boat
    });

    if(result.status === "ok") {
        await displayBoatsPage();
        displayAlert("Boat '" + boat.serialNumber +  "' deleted successfully", "success");
    } else
        displayAlert(result.message, "danger");
}

async function addBoat(boat) {
    const result = await ajax({
        url: 'manage/boats',
        method: 'post',
        jsonObject: boat
    });

    if(result.status === "ok") {
        await displayBoatsPage();
        displayAlert("Boat '" + boat.name +  "' added successfully", "success");
    } else
        displayAlert(result.message, "danger");
}

function handleFormAddBoatSubmit(event){
    const boat = getBoatFromForm();
    addBoat(boat);
    window.scroll(0,0);
    event.preventDefault();
}

function handleFormEditBoatSubmit(event){
    const boat = getBoatFromForm();
    editBoat(boat);
    window.scroll(0,0);
    event.preventDefault();
}

function getBoatFromForm(){
    const boat = {
        serialNumber: document.querySelector("#Form_boatSerialNumber").value,
        name: document.querySelector("#Form_boatName").value,
        boatType: document.querySelector("#Form_boatType").value,
        isWide: document.querySelector("#Form_boatWide").checked,
        isMarine: document.querySelector("#Form_boatMarine").checked,
        isPrivate: document.querySelector("#Form_boatPrivate").checked,
        isDisabled: document.querySelector("#Form_boatDisabled").checked,
        hasCoxswain: document.querySelector("#Form_boatCoxswain").checked,
        numOfRowers: document.querySelector("#Form_boatRowers").value,
        numOfPaddles: document.querySelector("#Form_boatPaddles").value
    }
    return boat;
}

function createBoatForm(){
    const boatForm = createForm({fields: [
            {type: "number", label: "Serial Number", id: "Form_boatSerialNumber", required: true},
            {type: "text", label: "Name", id: "Form_boatName", required: true},
            {type: "select", label: "Type", id: "Form_boatType", required: true,
                options: [{value:"SINGLE"}, {value:"PAIR"}, {value:"COXED_PAIR"}, {value:"DOUBLE"},
                    {value:"COXED_DOUBLE"}, {value:"FOUR"}, {value:"COXED_FOUR"}, {value:"QUAD"}, {value:"COXED_QUAD"},
                    {value:"EIGHT"}, {value:"OCTUPLE"}]},
            {type: "select", label: "Num Of Rowers", id: "Form_boatRowers", options: [{value:"ONE"}, {value:"TWO"},
                    {value:"FOUR"}, {value:"EIGHT"}],
                disabled: true, hidden: true},
            {type: "checkbox", label: "Has Coxswain", id: "Form_boatCoxswain", hidden: true, disabled: true},
            {type: "select", label: "Paddles", id: "Form_boatPaddles", options: [{value:"SINGLE"}, {value:"DOUBLE"}],
                hidden: true, disabled: true},
            {type: "checkbox", label: "Is Wide", id: "Form_boatWide"},
            {type: "checkbox", label: "Is Marine", id: "Form_boatMarine"},
            {type: "checkbox", label: "Is Private", id: "Form_boatPrivate"},
            {type: "checkbox", label: "Is Disabled", id: "Form_boatDisabled"},
            {type: "submit", text: "Submit", id: "Form_rowerSubmit", color: "info"}
        ]});
    return boatForm;
}

function updateBoatEditFormValues(formEL, boat){
    setTextFieldValues(formEL,{id:"Form_boatSerialNumber", value:boat.serialNumber, disabled:true});
    setTextFieldValues(formEL,{id:"Form_boatName", value:boat.name});
    setSelectFieldValues(formEL, {id: "Form_boatType", selected: boat.boatType, disabled: true, hidden: true});
    setCheckBoxFieldValues(formEL, {id: "Form_boatWide", checked: boat.isWide, disabled:true});
    setCheckBoxFieldValues(formEL, {id: "Form_boatMarine", checked: boat.isMarine});
    setCheckBoxFieldValues(formEL, {id: "Form_boatPrivate", checked: boat.isPrivate});
    setCheckBoxFieldValues(formEL, {id: "Form_boatDisabled", checked: boat.isDisabled});
    setSelectFieldValues(formEL, {id: "Form_boatRowers", selected: boat.numOfRowers, disabled: true, hidden: false});
    setSelectFieldValues(formEL, {id: "Form_boatPaddles", selected: boat.numOfPaddles, disabled: false, hidden: false});
    setCheckBoxFieldValues(formEL, {id: "Form_boatCoxswain", checked: boat.hasCoxswain, disabled: false, hidden: false});
}

function formatBoatsObject(boatsFromServer){
    let boats = [];
    boatsFromServer.forEach(b => {
        boats.push({
            "Serial Number": b.serialNumber,
            "Name": b.name,
            "Type": b.boatType,
            "Code": b.boatCode,
            "Is Private": b.isPrivate === true ? "Yes" : "",
            "Is Disabled": b.isDisabled === true ? "Yes" : "",
            "Actions": '<button type="button" class="btn btn-sm btn-primary" id="editBoat' + b.serialNumber + '">Edit</button> ' +
                '<button type="button" class="btn btn-sm btn-danger" id="deleteBoat' + b.serialNumber + '">Delete</button>'
        })
    });
    return boats;
}

function displayAddNewBoatPage(){
    cleanMainContent("Add Boat");
    const addBoatFormEL = createBoatForm();
    contentEL.appendChild(addBoatFormEL);
    addBoatFormEL.addEventListener("submit", handleFormAddBoatSubmit);
}

function displayEditBoatPage(event, boat){
    cleanMainContent("Edit Boat " + boat.serialNumber);
    const addBoatFormEL = createBoatForm();
    updateBoatEditFormValues(addBoatFormEL, boat);
    contentEL.appendChild(addBoatFormEL);
    addBoatFormEL.addEventListener("submit", handleFormEditBoatSubmit);
}


async function displayBoatsPage(){
    cleanMainContent("Boats");

    const createBoatBtnEL = createButton({id: "displayCreateBoat", color: "info", text: "Add Boat"});
    contentEL.appendChild(createBoatBtnEL);
    createBoatBtnEL.addEventListener('click', displayAddNewBoatPage);

    const importBoatBtnEL = createButton({id: "importBoat", color: "primary", text: "Import"});
    contentEL.appendChild(importBoatBtnEL);
    importBoatBtnEL.addEventListener('click', function(event){
        displayImportXmlPage({
            type: "boats"
        })
    });

    const exportBoatBtnEL = createButton({id: "exportBoat", color: "primary", text: "Export"});
    contentEL.appendChild(exportBoatBtnEL);
    exportBoatBtnEL.addEventListener('click', function(event){
        exportXML("boats")
    });

    const boats = await getBoats();
    const formattedBoats = formatBoatsObject(boats);

    const table = createTable(formattedBoats);
    contentEL.appendChild(table);

    // add listeners for delete and edit
    boats.forEach((boat)=>{
        document.querySelector("#editBoat" + boat.serialNumber).addEventListener('click', function(event){
            displayEditBoatPage(event, boat)
        });
        document.querySelector("#deleteBoat" + boat.serialNumber).addEventListener('click', function(event){
            deleteBoat(boat)
        });
    });
}