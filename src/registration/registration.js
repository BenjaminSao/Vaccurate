var db = firebase.firestore();

let onSubmit = () => {
    db.collection("clinics").doc(document.getElementById("email").value).set({
        name: document.getElementById("cname").value,
        location: document.getElementById("clocation").value,
        phone: document.getElementById("phone").value,
        doses: document.getElementById("doses").value,
    }).then(function(docRef) {

    })
    .catch(function(error) {
        console.error("Error adding document: ", error);
    });
    firebase.auth().createUserWithEmailAndPassword(document.getElementById("email").value, document.getElementById("pass").value)
    .then((user) => {
        window.location.assign("../registration-success/registration-success.html")
    })
    .catch((error) => {
        var errorCode = error.code;
        var errorMessage = error.message;
        // ..
    });
}
