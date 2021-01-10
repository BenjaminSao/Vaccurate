var db = firebase.firestore();
var email = sessionStorage.getItem("email")

var usersRef = db.collection("users");
var clinicsRef = db.collection("clinics").doc(email);
var clinicObj;
var doses;

// usersRef = usersRef.orderBy("score").limit();

clinicsRef.get().then(function(doc) {
    if (doc.exists) {
        doses = doc.data().doses;
        console.log(doses);
    } else {
        console.log("No such document!");
    }
}).catch(function(error) {
    console.log("Error getting document:", error);
});

console.log(doses);