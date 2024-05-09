package org.example.Entity;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
public class UserUtils {
    private static Set<Integer> usedIDs = new HashSet<>();
    private static Random random = new Random();

    public static int generateUserID() {
        int userID;
        do {
            userID = random.nextInt(Integer.MAX_VALUE);
        } while (usedIDs.contains(userID));

        usedIDs.add(userID);
        return userID;
    }
}