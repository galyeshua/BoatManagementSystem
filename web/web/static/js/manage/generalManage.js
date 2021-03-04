async function getNotificationsForManager() {
    return getNotificationsForUser('manage');
}

async function deleteNotification(notification) {
    const result = await ajax({
        url: 'manage/notifications',
        method: 'delete',
        jsonObject: notification
    });

    if(result.status === "ok") {
        await displayGeneralManagePage();
        displayAlert("Notification deleted successfully", "success");
    } else
        displayAlert(result.message, "danger");
}


async function addNotification(notification) {
    const result = await ajax({
        url: 'manage/notifications',
        method: 'post',
        jsonObject: notification
    });

    if(result.status === "ok") {
        await displayGeneralManagePage();
        displayAlert("Notification added successfully", "success");
    } else
        displayAlert(result.message, "danger");
}

function handleFormAddNotificationSubmit(event){
    const message = document.querySelector("#Form_notificationText").value;

    addNotification({message: message}).then();

    window.scroll(0,0);
    event.preventDefault();
}

function createAddNotificationForm(){
    const addNotificationForm = createForm({fields: [
            {type: "text", label: "Add general notification for all rowers", id: "Form_notificationText"},
            {type: "submit", text: "Submit", id: "Form_addNotificationSubmit", color: "info"}
        ]});
    return addNotificationForm;
}

function formatNotificationsObject(notificationsFromServer){
    let notifications = [];
    notificationsFromServer.forEach(n => {
        notifications.push({
            "Message": n.message,
            "Actions": '<button type="button" class="btn btn-sm btn-danger" id="deletenotification' + n.id + '">Delete</button>'
        })
    });
    return notifications;
}

async function displayGeneralManagePage(){
    cleanMainContent("Management");

    const addAddNotificationFormEL = createAddNotificationForm();
    contentEL.appendChild(addAddNotificationFormEL);
    addAddNotificationFormEL.addEventListener("submit", handleFormAddNotificationSubmit);

    const generalNotifications = await getNotificationsForManager();

    const formattedNotifications = formatNotificationsObject(generalNotifications);

    const table = createTable(formattedNotifications);
    contentEL.appendChild(table);

    // add listeners for delete and edit
    generalNotifications.forEach((notification)=>{
        document.querySelector("#deletenotification" + notification.id).addEventListener('click', function(event){
            deleteNotification(notification);
        });
    });
}