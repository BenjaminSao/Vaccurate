# Vaccurate
A data service to help prioritize the distribution COVID-19 vaccine and reduce vaccine wastage.

![](/gif/vaccuratefinal.gif)

## What is Vaccurate?
Vaccurate is a web application that aids in efficient distribution of the upcomming COVID-19 vaccine by determining who needs it most. Users who sign-up are asked to answer some questions about themselves, questions topics include occupation, health and lifestyle. Using the answers to the questions, the vaccurate server computes a score and attaches it to the user, the higher the score the higher the vaccine priority is for this individual. Nearby clinics can then use this score to determine who needs the vaccine first.

## Questions asked
These are the questions we asked in order to identify and calculate a score for a person. This can be updated to encompass a wider variety of situations and calculate a more accurate score.
* Age
* Are a Health-care Worker at risk for exposure? (Y/N)
* Do you live with/come into daily contact with vulnerable persons? (Y/N)
* Do/Did you have any form of heart/lung disease? (Y/N)
* Do/Did you have any form of diabetes? (Y/N)
* Are you immunocompromised? (Y/N)
* Are you part of Vulnerable Groups (i.e. first nations, ...)? (Y/N)
* Have you ever had/have COVID-19? (Y/N)

## Calculations
For now, Vaccurate uses a simple, weighted scoring system in our Java based server. The server pulls user information form our Firestore database where all user responses are stored. Given the question responses the server caluculates the score of a user with these weights:

|Question|Weight|
|---|---|
|Age: 0-17|0.6|
|Age: 18-35|0|
|Age: 36-45|0.4|
|Age: 46-55|0.6|
|Age: 56-65|0.7|
|Age: 66-75|0.8|
|Age: 75+|0.9|
|Healthcare worker|1|
|Essential Worker|0.6|
|Vulnerable Group|0.6|
