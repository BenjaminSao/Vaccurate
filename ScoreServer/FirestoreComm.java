package vaccurate;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firestore.admin.v1.UpdateFieldRequest;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.common.collect.ImmutableMap;
import com.google.cloud.firestore.DocumentReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirestoreComm {
    

    // C:\\Users\\VincentL\\nw_hacks\\vac_fire_creds.json
    private static String CRED_ADDRESS = "vac_fire_creds.json";
    private Firestore db;
    public long lastUpdated;

    public FirestoreComm() {
        
        try {
            FileInputStream serviceAccount = new FileInputStream(CRED_ADDRESS);
            GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
            FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(credentials)
                .build();
            
            FirebaseApp.initializeApp(options);
            db = FirestoreClient.getFirestore();
            getLastUpdated();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getLastUpdated() throws Exception {

        DocumentReference docRef = db.collection("lastUpdated").document("lastUpdateUNIX");

        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot doc = future.get();
        lastUpdated = (long) doc.get("time");
    }

    public void fillQueue(ConcurrentLinkedQueue<Map<String, Object>> q) {

        try {

            // Grab users created only after the last time pulled
            ApiFuture<QuerySnapshot> query = db.collection("users").whereGreaterThan("time", lastUpdated).get();
            QuerySnapshot querySnapshot = query.get();
            List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();

            // Grab each user, map database fields
            for (QueryDocumentSnapshot document : documents) {
                System.out.println("A document");
                Map<String, Object> userMap = document.getData();
                userMap.put("ID", document.getId());
                q.add(userMap);
            }

            // Update last updated time (UNIX time)
            DocumentReference docRef = db.collection("lastUpdated").document("lastUpdateUNIX");
            HashMap<String, Long> newTime = new HashMap<String, Long>();
            long unixTime = System.currentTimeMillis() / 1000L;
            newTime.put("time", unixTime);
            lastUpdated = unixTime;
            docRef.set(newTime);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void updateFirestoreScores(ConcurrentLinkedQueue<HashMap<String, String>> q) {

        int count = 0;
        // For each item in queue, add calculated score to the database
        while (!q.isEmpty()) {
            count++;
            Map<String, String> user = q.poll();
            try {
                DocumentReference docRef = db.collection("users").document(user.get("ID"));
                ApiFuture<WriteResult> result = docRef.update("score", user.get("score"));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println(count + " users were updated.");
    }

    public Map<String, Object> getWeights() {

        Map<String, Object> weights;
        try {

            DocumentReference docRef = db.collection("weights").document("weights");
            ApiFuture<DocumentSnapshot> future = docRef.get();
            DocumentSnapshot doc = future.get();
            weights = doc.getData();

            return weights;
        }
        catch (Exception e) {
            e.printStackTrace();

            return null;
        }

    }

    public static void main(String[] args) {

        FirestoreComm firestore = new FirestoreComm();
        System.out.println(firestore.lastUpdated);

        HashMap<String, String> toSend = new HashMap<String, String>();

        toSend.put("score", "5.7");
        toSend.put("ID", "alovelace");

        ConcurrentLinkedQueue<Map<String, Object>> q = new ConcurrentLinkedQueue<Map<String, Object>>();
        ConcurrentLinkedQueue<HashMap<String, String>> q1 = new ConcurrentLinkedQueue<HashMap<String,String>>();
        firestore.fillQueue(q);
        Map<String, Object> user1 = q.poll();
        Map<String, Object> user2 = q.poll();
        System.out.println(user1.get("first"));
        System.out.println(user2.get("first"));
        toSend.put("ID", user2.get("ID").toString());
        q1.add(toSend);
        firestore.updateFirestoreScores(q1);

        ScoreCalc calc = new ScoreCalc(firestore.getWeights());
        System.out.println(calc.calcScore(user2));

    }
    

    
}
