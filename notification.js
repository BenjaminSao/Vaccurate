const accountSid = "ACa48b44de088eb88ece5e55f9a4fbc17c";
const authToken = "e6eba779e86874b6246859222358f4cd"

const http = require("http")
const express = require("express")
const MessagingResponse = require("twilio").twiml.MessagingResponse
const bodyParser = require('body-parser');
const client = require("twilio")(accountSid, authToken)

 
client.messages.create({
    to: "+14034623775",
    from: "+12248885065",
    body: "say YES"
    // body: "\nYour COVID-19 vaccination dates have been confirmed:" +
    // "\n\nYOURNAME\nFirst Dose: FIRSTDATE\nSecond Dose: SECONDDATE\nClinic Location: LOCATION" +
    // "\n\nAre you able to attend both vaccination sessions? " +
    // "Reply YES to confirm your attendence, or register again at Vaccurate www.something.com to modify your appointments."
})

client.messages.create({
    to: "+15878899728",
    from: "+12248885065",
    body: "say YES"
})


const app = express()

app.use(bodyParser.urlencoded({ extended: false }));

app.post('/sms', (req, res) => {
    const twiml = new MessagingResponse();
    console.log(req.body.Body)
    if(req.body.Body == 'YES'){
        twiml.message("Thanks for confirming. (process notes)")
    } else {
        twiml.message("see dashboard (link)")
    }
    res.writeHead(200, {'Content-Type': "text/xml"})
    res.end(twiml.toString())
})

http.createServer(app).listen(1234, () => {
    console.log("listening on port 1234")
})