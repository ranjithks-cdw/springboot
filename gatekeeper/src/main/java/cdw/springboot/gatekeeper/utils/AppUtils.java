package cdw.springboot.gatekeeper.utils;

import java.util.Random;

public class AppUtils {
    public static String generateRandomPasskey() {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        return String.format("%06d", number);
    }
}
