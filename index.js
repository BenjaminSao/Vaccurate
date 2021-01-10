var db = firebase.firestore();
var userData = []; // Data about the user
var userPrompt = ["First Name", "What's your last name?", "What's your phone number?", "What's your email?", "What's your address?", "Are you an healthcare worker? (y/n)", "Are you an essential worker (y/n)",
"Are you apart of a vulnerable group? (y/n)", "How old are you?", "Do you have any serious chronic illness? (y/n)", "Are you a part of the minority? (i.e. first nation) (y/n)",
"Have you ever had/have COVID-19? (Y/N)", "Submit"] // Prompt Questions
var currQuestion = 0; // Current question the user is on

function nextQuestion(){
    if(currQuestion != userPrompt.length - 1){
        userData.push(document.getElementById("input").value);
        currQuestion++;
        document.getElementById("prompt").innerHTML = userPrompt[currQuestion];
        if(currQuestion != 12){
            document.getElementById("counter").innerHTML = currQuestion + 1 + "/12";
        } else {
            document.getElementById("next").innerText = "Submit"
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
        wwcv: userData[5],
        ess: userData[6],
        vulnerableGroup: userData[7],
        age: userData[8],
        sc: userData[9],
        fn: userData[10],
        covid: userData[11],
        time: Date.now()/1000
    }).then(function(docRef) {
        console.log("Document written with ID: ", docRef.id);
    })
    .catch(function(error) {
        console.error("Error adding document: ", error);
    });
}
