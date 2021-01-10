package vaccurate;

import static java.util.concurrent.TimeUnit.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.Map;
import java.util.HashMap;


public class Runner {
    
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(5);

    public Runner() {

    }

    public void processUsers(FirestoreComm firestore, ConcurrentLinkedQueue<HashMap<String, String>> outQueue, 
                             ConcurrentLinkedQueue<Map<String, Object>> inQueue, ScoreCalc calculator) {

        final Runnable process = new Runnable() {
            public void run() {
                
                double userScore = 0.0;
                System.out.println("Checking for updates...");
                firestore.fillQueue(inQueue);

                if (!inQueue.isEmpty()) {
                    System.out.println("New users found, calculating scores...");
                    while (!inQueue.isEmpty()) {
                        Map<String, Object> user = inQueue.poll();
                        HashMap<String, String> scoreField = new HashMap<String, String>();
                        userScore = calculator.calcScore(user);
                        scoreField.put("ID", user.get("ID").toString());
                        scoreField.put("score", Double.toString(userScore));
                        outQueue.add(scoreField);
                    }
                }

                else {
                    System.out.println("Nothing to update");
                }

                if (!outQueue.isEmpty()) {
                    System.out.println("New scores calculated, pushing to database...");
                    firestore.updateFirestoreScores(outQueue);
                }
                System.out.println("Process completed, next update in 10s");
            }
        };
        scheduler.scheduleAtFixedRate(process, 0, 10, SECONDS);
    }

    public static void main(String[] args) {

        Runner runner = new Runner();
        FirestoreComm firestore = new FirestoreComm();
        ConcurrentLinkedQueue<Map<String, Object>> inQueue = new ConcurrentLinkedQueue<Map<String, Object>>();
        ConcurrentLinkedQueue<HashMap<String, String>> outQueue = new ConcurrentLinkedQueue<HashMap<String, String>>();
        ScoreCalc calculator = new ScoreCalc(firestore.getWeights());
        runner.processUsers(firestore, outQueue, inQueue, calculator);
    }
}
