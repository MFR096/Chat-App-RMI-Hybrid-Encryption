package Service;

import org.json.JSONObject;

public class util {
    public static boolean isValidJSONObject(String str) {
        try {
            new JSONObject(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isValidInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
