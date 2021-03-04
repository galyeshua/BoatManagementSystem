const contentEL = document.querySelector("#mainContent");


// CREATE CONTENT UTILS

function cleanMainContent(title){
    window.scroll(0,0);
    contentEL.innerHTML = "";
    clearAlert();

    if(title !== undefined){
        const header = document.createElement("h1");
        header.innerText = title;
        contentEL.appendChild(header);
    }
}


// CREATE CONTENT UTILS

function clearAlert(){
    displayAlertsEL.innerHTML = "";
    displayAlertsEL.setAttribute("style", "display: none;")
}

function cleanMainContent(title){
    window.scroll(0,0);
    contentEL.innerHTML = "";
    clearAlert();

    if(title !== undefined){
        const header = document.createElement("h1");
        header.innerText = title;
        contentEL.appendChild(header);
    }
}


function setSelectFieldValues(parentElement, field) {
    const inputEl = parentElement.querySelector("#" + field.id);
    const labelEl = parentElement.querySelector("label[for="+ field.id +"]");

    updateHiddenAttribute(field, [inputEl, labelEl]);

    if(field.disabled !== undefined)
        inputEl.disabled = field.disabled;
    if(field.required !== undefined)
        inputEl.required = field.required;
    if(field.options !== undefined){
        inputEl.innerHTML = "";
        field.options.forEach((optionObject)=>{
            const optionEl = document.createElement("option");
            optionEl.innerText = optionObject.value;
            optionEl.id = optionObject.id !== undefined ? field.id + "Option__id__" + optionObject.id : undefined;
            inputEl.appendChild(optionEl);
        });
    }

    if(field.hideLabel === true)
        labelEl.setAttribute("style", "display: none;");
    if(field.hideLabel === false)
        labelEl.setAttribute("style", "display: block;");

    if(field.selected !== undefined)
        inputEl.value = field.selected;

    if(field.label !== undefined)
        labelEl.innerText = field.required === true ? "* " + field.label : field.label;
}


function createSelectField(field){
    const divEl = document.createElement("div");
    divEl.className = "form-group";

    const labelEl = document.createElement("label");
    labelEl.setAttribute("for", field.id);
    divEl.appendChild(labelEl);

    const selectEl = document.createElement("select");
    selectEl.setAttribute("class", "form-control");
    selectEl.setAttribute("id", field.id);
    divEl.appendChild(selectEl);

    setSelectFieldValues(divEl, field);
    return divEl;
}


function createButton(field){
    const buttonEl = document.createElement("button");
    buttonEl.className = field.color !== undefined ? "mr-1 btn btn-" + field.color : "btn btn-primary";
    buttonEl.id = field.id;
    buttonEl.type = "submit";
    buttonEl.innerText = field.text;
    if(field.disabled === true)
        buttonEl.disabled = true;
    return buttonEl;
}

function createFormButtonField(field){
    const divEl = document.createElement("div");
    divEl.className = "form-group";
    divEl.appendChild(createButton(field));
    return divEl;
}

function setCheckBoxFieldValues(parentElement, field){
    const inputEl = parentElement.querySelector("#" + field.id);
    const labelEl = parentElement.querySelector("label[for="+ field.id +"]");

    updateHiddenAttribute(field, [inputEl, labelEl]);

    if(field.disabled !== undefined)
        inputEl.disabled = field.disabled;
    if(field.checked !== undefined)
        inputEl.checked = field.checked;
    if(field.name !== undefined)
        inputEl.name = field.name;

    if(field.label !== undefined)
        labelEl.innerText = field.label;
}

function createFormCheckBoxField(field){
    const divEl = document.createElement("div");
    divEl.className = (field.type !== "radio" ? "form-group " : "")+ "form-check" + (field.inline === true ? "-inline" : "");

    const inputEl = document.createElement("input");
    inputEl.setAttribute("type", field.type);
    inputEl.setAttribute("class", "form-check-input");
    inputEl.setAttribute("id", field.id);
    divEl.appendChild(inputEl);

    const labelEl = document.createElement("label");
    labelEl.setAttribute("for", field.id);
    labelEl.setAttribute("class","form-check-label");
    divEl.appendChild(labelEl);

    setCheckBoxFieldValues(divEl, field);

    return divEl;
}

function setTextFieldValues(parentElement, field){
    const inputEl = parentElement.querySelector("#" + field.id);
    const labelEl = parentElement.querySelector("label[for="+ field.id +"]");

    updateHiddenAttribute(field, [inputEl, labelEl]);

    if(field.type !== undefined)
        inputEl.type = field.type;

    if(field.value !== undefined)
        inputEl.value = field.value;
    if(field.required !== undefined)
        inputEl.required = field.required;
    if(field.disabled === true)
        inputEl.disabled = true;
    if(field.placeholder !== undefined)
        inputEl.placeholder = field.placeholder;

    if(field.hideLabel === true)
        labelEl.setAttribute("style", "display: none;");
    if(field.hideLabel === false)
        labelEl.setAttribute("style", "display: block;");

    if(field.label !== undefined)
        labelEl.innerText = field.required === true ? "* " + field.label : field.label;

}

function createLabelField(field){
    const divEl = document.createElement("div");
    const labelEl = document.createElement("label");

    updateHiddenAttribute(field, [labelEl]);

    if(field.id !== undefined)
        labelEl.id = field.id;

    if(field.label !== undefined)
        labelEl.innerText = field.required === true ? "* " + field.label : field.label;

    divEl.appendChild(labelEl);


    return divEl;
}

function createDivField(field){
    const divEl = document.createElement("div");
    divEl.className = "pb-2";

    if(field.hidden === true)
        divEl.setAttribute("style", "display: none;")
    if(field.hidden === false)
        divEl.setAttribute("style", "display: block;")

    if(field.id !== undefined)
        divEl.id = field.id;

    if(field.text !== undefined)
        divEl.innerText = field.text;

    if(field.html !== undefined)
        divEl.innerHTML = field.html;

    return divEl;
}

function createFormTextField(field){
    const divEl = document.createElement("div");
    divEl.className = "form-group";

    const labelEl = document.createElement("label");
    labelEl.setAttribute("for", field.id);
    divEl.appendChild(labelEl);

    const inputEl = document.createElement("input");
    inputEl.id = field.id;
    inputEl.className = "form-control";
    divEl.appendChild(inputEl);

    setTextFieldValues(divEl, field);

    return divEl;
}


function setFileFieldValues(parentElement, field){
    const inputEl = parentElement.querySelector("#" + field.id);
    const labelEl = parentElement.querySelector("label[for="+ field.id +"]");

    updateHiddenAttribute(field, [inputEl, labelEl]);

    if(field.accept !== undefined)
        inputEl.accept = field.accept;

    if(field.value !== undefined)
        inputEl.value = field.value;
    if(field.required !== undefined)
        inputEl.required = field.required;
    if(field.disabled === true)
        inputEl.disabled = true;
    if(field.name !== undefined)
        inputEl.name = field.name;

    if(field.hideLabel === true)
        labelEl.setAttribute("style", "display: none;");
    if(field.hideLabel === false)
        labelEl.setAttribute("style", "display: block;");
    if(field.label !== undefined)
        labelEl.innerText = field.required === true ? "* " + field.label : field.label;
}

function createFileField(field){
    const divEl = document.createElement("div");
    divEl.className = "form-group";

    const labelEl = document.createElement("label");
    labelEl.setAttribute("for", field.id);
    divEl.appendChild(labelEl);

    const inputEl = document.createElement("input");
    inputEl.id = field.id;
    inputEl.className = "form-control";
    inputEl.type = field.type;
    divEl.appendChild(inputEl);

    setFileFieldValues(divEl, field);

    return divEl;
}

function updateHiddenAttribute(field, elements){
    elements.forEach(e =>{
        if(field.hidden === true) {
            e.style.display = "none";
        }
        if(field.hidden === false) {
            e.style.display = "block";
        }
    });
}

function createFormField(fieldObject){
    switch (fieldObject.type.toLowerCase()){
        case "text":
            return createFormTextField(fieldObject);

        case "password":
            return createFormTextField(fieldObject);

        case "email":
            return createFormTextField(fieldObject);

        case "time":
            return createFormTextField(fieldObject);

        case "date":
            return createFormTextField(fieldObject);

        case "number":
            return createFormTextField(fieldObject);

        case "checkbox":
            return createFormCheckBoxField(fieldObject);

        case "select":
            return createSelectField(fieldObject);

        case "label":
            return createLabelField(fieldObject);

        case "div":
            return createDivField(fieldObject);

        case "file":
            return createFileField(fieldObject);

        case "radio":
            return createFormCheckBoxField(fieldObject);

        case "submit":
            return createFormButtonField(fieldObject);
    }
}

function createForm(formFieldsObject){
    const formEL = document.createElement("form");
    if(formFieldsObject.multipart === true)
        formEL.setAttribute("enctype", "multipart/form-data");
    if(formFieldsObject.method !== undefined)
        formEL.setAttribute("method", formFieldsObject.method);
    if(formFieldsObject.action !== undefined)
        formEL.setAttribute("action", formFieldsObject.action);

    if(formFieldsObject.inline === true)
        formEL.className = "form-inline";

    formFieldsObject.fields.forEach((field)=>{
        if(field !== null){
            const formFieldEL = createFormField(field);
            if(formFieldsObject.inline === true)
                formFieldEL.classList.add("pr-2");
            formEL.appendChild(formFieldEL);
        }
    });

    return formEL;
}

function createTable(elements){
    if(elements.length <= 0){
        const noElementsEl = document.createElement('p');
        noElementsEl.innerText = "No Info To Display";
        return noElementsEl;
    }

    const table = document.createElement('table');
    table.classList.add('table');
    table.classList.add('table-hover');
    const thead = table.createTHead();
    const tbody = table.createTBody();
    let tr = document.createElement('tr');

    const columns = Object.keys(elements[0]);

    columns.forEach((column) => {
        const th = document.createElement('th');
        th.innerText = column;
        tr.append(th);
    });
    thead.append(tr);

    elements.forEach((element)=>{
        tr = document.createElement('tr');
        columns.forEach((column) => {
            const td = document.createElement('td');
            td.innerHTML = element[column];
            tr.append(td);
        });
        tbody.append(tr);
    });

    return table;
}

// AJAX
async function ajax(infoObject){
    const urlParameters = infoObject.urlParameters !== undefined ? "?" + keyValueStringFromMapObject(infoObject.urlParameters) : ""
    const response = await fetch(infoObject.url + urlParameters, {
        method: infoObject.method !== undefined ? infoObject.method : 'get',
        headers: infoObject.deleteHeaders !== true ? (new Headers({
            'Content-Type': infoObject.contentType === undefined ? 'application/json' : infoObject.contentType
        })) : undefined,
        body: infoObject.jsonObject !== undefined ? JSON.stringify(infoObject.jsonObject) :
            (infoObject.formObject !== undefined ? infoObject.formObject : undefined)
    });
    const result = await response.json();
    return result;
}

function keyValueStringFromMapObject(mapObject){
    let result = "";
    Object.keys(mapObject).forEach(k => {
        result += k + "=" + mapObject[k] + "&";
    });
    return result.slice(0, result.length - 1);
}

// Cookies
function getCookie(name){
    const cookies = decodeURIComponent(document.cookie).split(";");
    let res = null;
    cookies.forEach(cookie => {
        if(cookie.startsWith(name + "=")){
            res = cookie.split(name + "=")[1];
            return res;
        }
    });
    return res;
}

function getCurrentUserSerialNumber(){
    return parseInt(getCookie("userSerialNumber"));
}

// FORMATTERS
function dateObjectToString(dateObject){
    return dateObject.year + "-" + String(dateObject.month).padStart(2, '0') + "-" + String(dateObject.day).padStart(2, '0');
}

function dateStringToObject(dateString){
    const d = dateString.split("-");
    return {year: d[0], month: d[1], day: d[2]}
}


function timeStringToObject(timeString){
    const t = timeString.split(":");
    return {hour: t[0], minute: t[1]}
}

function timeObjectToString(timeObject){
    return String(timeObject.hour).padStart(2, '0') + ":" + String(timeObject.minute).padStart(2, '0');
}

function dateStringForTodayPlusRefDays(numOfDayToAdd=0, format="dd/MM/yyyy"){
    let res = format;
    const d = new Date(new Date().setDate(new Date().getDate() + numOfDayToAdd));
    res = res.replace("dd", String(d.getDate()).padStart(2, '0'));
    res = res.replace("MM", String(d.getMonth()+1).padStart(2, '0'));
    res = res.replace("yyyy", String(d.getFullYear()));
    return res;
}



// Export XML

async function exportXML(type) {
    const result = await ajax({
        url: 'manage/xml',
        method: 'get',
        urlParameters: {type: type}
    });

    if(result.status === "ok"){
        return downloadFile(type + ".xml", result.message);
    } else
        displayAlert(result.message, "danger");
}

function downloadFile(filename, text) {
    let element = document.createElement('a');
    element.setAttribute('href', 'data:text/plain;charset=utf-8,' + encodeURIComponent(text));
    element.setAttribute('download', filename);

    element.style.display = 'none';
    document.body.appendChild(element);
    element.click();
    document.body.removeChild(element);
}

// Import XML

async function displayNextPageByType(type){
    switch (type){
        case "activities":
            await displayActivitiesPage();
            break;
        case "boats":
            await displayBoatsPage();
            break;
        case "rowers":
            await displayRowersPage();
            break;
    }
}

async function importXML(infoObject) {
    const result = await ajax({
        url: 'manage/xml',
        method: 'post',
        urlParameters: {type: infoObject.type,override: document.querySelector("#Form_wantsToOverrideImportData").checked},
        formObject: infoObject.formData,
        deleteHeaders: true,
    });

    if(result.status === "ok"){
        await displayNextPageByType(infoObject.type);
        displayAlert("file uploaded successfully", "success");
    }else if(result.status === "warning"){
        await displayNextPageByType(infoObject.type);
        const warningString = [];
        result.message.forEach(msg => {
            warningString.push(msg);
        })
        displayAlert(warningString.join("<br>"), "warning");

    } else
        displayAlert(result.message, "danger");
}

function displayImportXmlPage(infoObject){
    cleanMainContent("Import " + infoObject.type);
    const addImportXMLFormEL = createImportXmlForm();
    contentEL.appendChild(addImportXMLFormEL);

    document.querySelectorAll('input[type=radio][name="addOrReplaceData"]').forEach(radio => {
        radio.addEventListener('change', displayFileWarning);
    });

    addImportXMLFormEL.addEventListener("submit", function(event){
        uploadFile(event, infoObject.type);
    });
}

function uploadFile(event, type){
    event.preventDefault();
    window.scroll(0,0);

    const file = document.querySelector("#Form_xmlFile");
    const formData = new FormData();
    formData.append("uploadFile", file.files[0]);
    importXML({
        type: type,
        formData: formData
    });
}

function createImportXmlForm(){
    const importXmlForm = createForm({
        fields: [
            {type: "radio",label: "Add to current data", id: "Form_wantsToAddImportData", name: "addOrReplaceData", checked: true},
            {type: "radio",label: "Override current data", id: "Form_wantsToOverrideImportData", name: "addOrReplaceData"},
            {type: "label", label: "This operation will delete all reservations", id: "Form_warningIdOverride", hidden: true},
            {type: "file", label: "Xml File", id: "Form_xmlFile", name: "uploadFile", accept: "text/xml", required: true},
            {type: "submit", text: "Submit", id: "Form_importFileSubmit", color: "info"}
        ]});
    return importXmlForm;
}

function displayFileWarning(event){
    const labelFieldEl = document.querySelector("#Form_warningIdOverride");
    labelFieldEl.style.color = "red";

    if(document.querySelector("#Form_wantsToOverrideImportData").checked)
        labelFieldEl.style.display = "block";
    else
        labelFieldEl.style.display = "none";
}



// MENU
async function buildMenu(ismanager = false){
    const usernamePlaceEl = document.querySelector("#menu_usernamePlace");
    const leftButtonsPlaceEl = document.querySelector("#menu_leftButtonsPlace");
    const rightButtonsPlaceEl = document.querySelector("#menu_rightButtonsPlace");
    const userInfo = await getCurrentUserInfo();

    usernamePlaceEl.innerHTML = userInfo.name;

    let buttons = [];
    buttons.push("<button type=\"button\" class=\"btn btn-link pl-0\" id=\"homePageBtn\">Home</button>");
    if(userInfo.isManager){
        buttons.push("<button type=\"button\" class=\"btn btn-link\" id=\"boatsPageBtn\">Boats</button>");
        buttons.push("<button type=\"button\" class=\"btn btn-link\" id=\"rowersPageBtn\">Rowers</button>");
        buttons.push("<button type=\"button\" class=\"btn btn-link\" id=\"activitiesPageBtn\">Activities</button>");
        buttons.push("<button type=\"button\" class=\"btn btn-link\" id=\"manageReservationsPageBtn\">Manage Reservations</button>");
    }
    leftButtonsPlaceEl.innerHTML = await buttons.join("");

    buttons = [];
    buttons.push("<button type=\"button\" class=\"btn btn-link pl-1 pr-1\" id=\"editProfileBtn\">Edit Profile</button>");
    if(userInfo.isManager){
        buttons.push("<button type=\"button\" class=\"btn btn-link pl-1 pr-1\" id=\"generalManageBtn\">Manage</button>");
    }
    buttons.push("<button type=\"button\" class=\"btn btn-link pl-1 pr-1\" id=\"logoutUser\">Logout</button>");
    rightButtonsPlaceEl.innerHTML = await buttons.join(" | ");

    addMenuListeners(userInfo.isManager);
}

function addMenuListeners(isManager){
    document.querySelector("#homePageBtn").addEventListener('click', displayReservationHomePage);
    document.querySelector("#editProfileBtn").addEventListener('click', displayEditProfilePage);
    document.querySelector("#logoutUser").addEventListener('click', logout);

    if(isManager){
        document.querySelector("#rowersPageBtn").addEventListener('click', displayRowersPage);
        document.querySelector("#boatsPageBtn").addEventListener('click', displayBoatsPage);
        document.querySelector("#activitiesPageBtn").addEventListener('click', displayActivitiesPage);
        document.querySelector("#manageReservationsPageBtn").addEventListener('click', displayManageReservationPage);
        document.querySelector("#generalManageBtn").addEventListener('click', displayGeneralManagePage);
    }
}
