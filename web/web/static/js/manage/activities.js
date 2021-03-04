async function getActivitiesForManager() {
    return getActivitiesForUser();
}

async function editActivity(activity) {
    console.log(activity);
    const result = await ajax({
        url: 'manage/activities',
        method: 'put',
        jsonObject: activity
    });

    if(result.status === "ok") {
        await displayActivitiesPage();
        displayAlert("Activity '" + activity.name +  "' updated successfully", "success");
    } else
        displayAlert(result.message, "danger");
}

async function deleteActivity(activity) {
    const result = await ajax({
        url: 'manage/activities',
        method: 'delete',
        jsonObject: activity
    });

    if(result.status === "ok") {
        await displayActivitiesPage();
        displayAlert("Activity '" + activity.name +  "' deleted successfully", "success");
    } else
        displayAlert(result.message, "danger");
}

async function addActivity(activity) {
    const result = await ajax({
        url: 'manage/activities',
        method: 'post',
        jsonObject: activity
    });

    if(result.status === "ok") {
        await displayActivitiesPage();
        displayAlert("Activity '" + activity.name +  "' added successfully", "success");
    } else
        displayAlert(result.message, "danger");
}

function handleFormAddActivitySubmit(event){
    const activity = getActivityFromForm();
    addActivity(activity);
    window.scroll(0,0);
    event.preventDefault();
}

function handleFormEditActivitySubmit(event){
    const activity = getActivityFromForm();
    editActivity(activity);
    window.scroll(0,0);
    event.preventDefault();
}

function getActivityFromForm(){
    const activityId = document.querySelector("#Form_activityID").value.trim();
    const activity = {
        id: activityId !== "" ? parseInt(activityId) : null,
        name: document.querySelector("#Form_activityName").value,
        boatType: document.querySelector("#Form_activityBoatType").value,
        startTime: timeStringToObject(document.querySelector("#Form_activityStartTime").value),
        finishTime: timeStringToObject(document.querySelector("#Form_activityFinishTime").value),
    }
    return activity;
}

function createActivityForm(){
    const activityForm = createForm({fields: [
            {type: "number", label: "ID", id: "Form_activityID", disabled: true, hidden: true},
            {type: "text", label: "Name", id: "Form_activityName", required: true},
            {type: "time", label: "Start Time", id: "Form_activityStartTime", required: true},
            {type: "time", label: "Finish Time", id: "Form_activityFinishTime", required: true},
            {type: "select", label: "Type", id: "Form_activityBoatType",
                options: [{value:""}, {value:"SINGLE"}, {value:"PAIR"}, {value:"COXED_PAIR"}, {value:"DOUBLE"},
                    {value:"COXED_DOUBLE"}, {value:"FOUR"}, {value:"COXED_FOUR"}, {value:"QUAD"}, {value:"COXED_QUAD"},
                    {value:"EIGHT"}, {value:"OCTUPLE"}]},
            {type: "submit", text: "Submit", id: "Form_rowerSubmit", color: "info"}
        ]});
    return activityForm;
}

function updateActivityEditFormValues(formEL, activity){
    setTextFieldValues(formEL,{id:"Form_activityID", value:activity.id});
    setTextFieldValues(formEL,{id:"Form_activityName", value:activity.name});
    setTextFieldValues(formEL,{id:"Form_activityStartTime", value:timeObjectToString(activity.startTime)});
    setTextFieldValues(formEL,{id:"Form_activityFinishTime", value:timeObjectToString(activity.finishTime)});
    setSelectFieldValues(formEL, {id: "Form_activityBoatType", selected: activity.boatType});
}

function formatActivitiesObject(ActivitiesFromServer){
    let activities = [];
    ActivitiesFromServer.forEach(a => {
        activities.push({
            "Activity Name": a.name,
            "Start time": timeObjectToString(a.startTime),
            "Finish time": timeObjectToString(a.finishTime),
            "Boat Type": a.boatType !== undefined ? a.boatType : "",
            "Actions": '<button type="button" class="btn btn-sm btn-primary" id="editActivity' + a.id + '">Edit</button> ' +
                '<button type="button" class="btn btn-sm btn-danger" id="deleteActivity' + a.id + '">Delete</button>'
        })
    });
    return activities;
}

function displayAddNewActivityPage(){
    cleanMainContent("Add Activity");
    const addActivityFormEL = createActivityForm();
    contentEL.appendChild(addActivityFormEL);
    addActivityFormEL.addEventListener("submit", handleFormAddActivitySubmit);
}

function displayEditActivityPage(event, activity){
    cleanMainContent("Edit Activity " + activity.name);
    const addActivityFormEL = createActivityForm();
    updateActivityEditFormValues(addActivityFormEL, activity);
    contentEL.appendChild(addActivityFormEL);
    addActivityFormEL.addEventListener("submit", handleFormEditActivitySubmit);
}


async function displayActivitiesPage(){
    cleanMainContent("Activities");

    const createActivityBtnEL = createButton({id: "displayCreateActivity", color: "info", text: "Add Activity"});
    contentEL.appendChild(createActivityBtnEL);
    createActivityBtnEL.addEventListener('click', displayAddNewActivityPage);

    const importActivityBtnEL = createButton({id: "importActivity", color: "primary", text: "Import"});
    contentEL.appendChild(importActivityBtnEL);
    importActivityBtnEL.addEventListener('click', function(event){
        displayImportXmlPage({
            type: "activities"
        })
    });

    const exportActivityBtnEL = createButton({id: "exportActivity", color: "primary", text: "Export"});
    contentEL.appendChild(exportActivityBtnEL);
    exportActivityBtnEL.addEventListener('click', function(event){
        exportXML("activities")
    });

    const Activities = await getActivitiesForManager();
    const formattedActivities = formatActivitiesObject(Activities);

    const table = createTable(formattedActivities);
    contentEL.appendChild(table);

    // add listeners for delete and edit
    Activities.forEach((activity)=>{
        document.querySelector("#editActivity" + activity.id).addEventListener('click', function(event){
            displayEditActivityPage(event, activity);
        });
        document.querySelector("#deleteActivity" + activity.id).addEventListener('click', function(event){
            deleteActivity(activity);
        });
    });
}