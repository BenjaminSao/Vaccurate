var db = firebase.firestore();

let onSubmit = () => { 
    firebase.auth().signInWithEmailAndPassword(document.getElementById("email").value, document.getElementById("pass").value)
    .then((user) => {
        sessionStorage.setItem("email", document.getElementById("email").value)
        window.location.assign("../overview/overview.html")
    })
    .catch((error) => {
        var errorCode = error.code;
        var errorMessage = error.message;
    });
}