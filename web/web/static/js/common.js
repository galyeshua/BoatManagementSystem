const displayAlertsEL = document.querySelector("#displayAlerts");

function displayAlert(Msg, color="danger"){
    displayAlertsEL.setAttribute("style", "display: block;")
    displayAlertsEL.innerHTML = Msg;
    displayAlertsEL.className = "alert alert-" + color;
}