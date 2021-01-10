var db = firebase.firestore();
var email = sessionStorage.getItem("email")

var usersRef = db.collection("users");
var clinicsRef = db.collection("clinics").doc(email);
var clinicObj;
var doses;

// usersRef = usersRef.orderBy("score").limit();

clinicsRef.get().then(function(doc) {
    if (doc.exists) {
        this.doses = doc.data().doses;
    } else {
        console.log("No such document!");
    }
}).catch(function(error) {
    console.log("Error getting document:", error);
});
