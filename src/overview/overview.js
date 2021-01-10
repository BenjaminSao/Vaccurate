var db = firebase.firestore();
var email = sessionStorage.getItem("email")

var usersRef = db.collection("users");
var clinicsRef = db.collection("clinics").doc(email);
var doses;


clinicsRef.get().then(function(doc) {
    if (doc.exists) {
        this.doses = doc.data().doses;
        usersRef.orderBy("score", "desc").limit(this.doses).get().then(function(querySnapshot) {
            querySnapshot.forEach(function(doc) {
                var list = document.createElement('ul');
                var score = document.createElement('li');
                score.appendChild(document.createTextNode(doc.data().score));
                list.appendChild(score);
                var name = document.createElement('li');
                name.appendChild(document.createTextNode(doc.data().first + " " + doc.data().last));
                list.appendChild(name);
                var phone = document.createElement('li');
                phone.appendChild(document.createTextNode(doc.data().phone));
                list.appendChild(phone);
                var email = document.createElement('li');
                email.appendChild(document.createTextNode(doc.data().email));
                list.appendChild(email);
                document.getElementById("list").appendChild(list);
            });
        });
    } else {
        console.log("No such document!");
    }
}).catch(function(error) {
    console.log("Error getting document:", error);
});
