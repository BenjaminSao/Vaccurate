package vaccurate;

import java.util.Map;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class ScoreCalc {

    HashMap<String, Double> weights;
    List<String> props = Arrays.asList("age", "wwcv", "ess", 
                            "vulnerableGroup", "sc", "mc", "fn", "covid");

    public ScoreCalc(Map<String, Object> weightMap) {

        this.weights = parseWeights(weightMap);
    }

    private HashMap<String, Double> parseWeights(Map<String, Object> weightMap) {

        // Convert the String-Object map to a String-Double Hash with the weights
        HashMap<String, Double> parsed = new HashMap<String, Double>();
        for (String key : weightMap.keySet()) {
            parsed.put(key, Double.parseDouble(weightMap.get(key).toString()));
        }

        return parsed;
    }

    private boolean verifyUserInfo(Map<String, Object> user){  


        // Validate if proper information is availible
        Set<String> userFields = user.keySet();

        for (String prop : props) {
            if (!userFields.contains(prop)) return false;
        }

        return true;
    }   

    private double calcAgeScore(int age) {

        // Grab weights based off age
        if (age >= 0 && age < 18) {
            return weights.get("age0-17");
        }
        else if (age >= 18 && age <= 35) {
            return weights.get("age18-35");
        }   
        else if (age >= 36 && age <= 45) {
            return weights.get("age36-45");
        }
        else if (age >= 46 && age <= 55) {
            return weights.get("age46-55");
        }
        else if (age >= 56 && age <= 65) {
            return weights.get("age56-65");
        }
        else if (age >= 66 && age <= 75) {
            return weights.get("age66-75");
        }
        else {
            return weights.get("age75+");
        }
    }

    public double calcScore(Map<String, Object> user) {

        if (!verifyUserInfo(user)) {
            System.out.println("Not enough information");
            return -1.0;
        }

        double score = 0;

        // Calculate score based on stored weights
        score += calcAgeScore(Integer.parseInt(user.get("age").toString()));
        if (user.get("wwcv").equals("y")) score += 1.0;
        // if (user.get("wrcv").equals("y")) score += 0.8;
        // if (user.get("dwcv").equals("y")) score += 0.5;
        if (user.get("ess").equals("y")) score += 0.6;
        if (user.get("vulnerableGroup").equals("y")) score += 0.8;
        if (user.get("sc").equals("y")) score += 0.65;
        if (user.get("mc").equals("y")) score += 0.3;
        if (user.get("fn").equals("y")) score += 1.0;
        if (user.get("covid").equals("n")) score += 0.4;

        return score;
    }
    
}
