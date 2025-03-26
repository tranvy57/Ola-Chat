package vn.edu.iuh.fit.olachatbackend.utils;

public class FormatPhoneNumber {
    public static String formatPhoneNumberTo84(String phoneNumber) {
        if (phoneNumber.startsWith("0")) {
            return "+84" + phoneNumber.substring(1);
        }
        return phoneNumber; // Nếu đã có +84 thì giữ nguyên
    }

    public static String formatPhoneNumberTo0(String phoneNumber) {
        if (phoneNumber.startsWith("+84")) {
            return "0" + phoneNumber.substring(3);
        }
        return phoneNumber; // Nếu đã có 0 thì giữ nguyên
    }
}
