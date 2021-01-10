var db = firebase.firestore();
var userData = []; // Data about the user
var userPrompt = ["First Name", "Last Name", "Phone", "Email", "Address", "Are you an Essential Worker?", "Do you live with/come into daily contact with vulnerable persons> (Y/N)",
"Age", "Do/Did you have any form of heart/lung disease? (Y/N)", "Do/Did you have any form of diabetes? (Y/N)", "Are you immunocompromised? (Y/N)", "Are you apart of Vulnerable Groups (i.e. first nations, ...)? (Y/N)",
"Have you ever had/have COVID-19? (Y/N)", "Are you pregnant? (Y/N)", "Please provide your postal code", "Submit"] // Prompt Questions
var currQuestion = 0; // Current question the user is on

function nextQuestion(){
    if(currQuestion != userPrompt.length - 1){
        userData.push(document.getElementById("input").value);
        currQuestion++;
        document.getElementById("prompt").innerHTML = userPrompt[currQuestion];
        if(currQuestion != 15){
            document.getElementById("counter").innerHTML = currQuestion + 1 + "/15";
        } else {
            document.getElementById("counter").style.display = "none";
            document.getElementById("input").style.display = "none";

        }
        document.getElementById("input").value = "";
    } else {
        addDataToFirestore();
    }
}

function addDataToFirestore(){
    db.collection("users").add({
        first: userData[0],
        last: userData[1],
        phone: userData[2],
        email: userData[3],
        address: userData[4],
        essentialWorker: userData[5],
        dailyContact: userData[6],
        age: userData[7],
        heartLung: userData[8],
        diabetes: userData[9],
        immunocompromised: userData[10],
        vulnerableGroup: userData[11],
        covid: userData[12],
        pregnant: userData[13],
        postal: userData[14],
        time: Date.now()/1000
    }).then(function(docRef) {
        console.log("Document written with ID: ", docRef.id);
    })
    .catch(function(error) {
        console.error("Error adding document: ", error);
    });
}