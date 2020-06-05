package com.laon.cashlink.common.lib;

import java.util.Random;

public class AccountLib {

    public static String preset = "QWERTYUIOPASDFGHJKLZXCVBNM1234567890";
    public static Integer length = 9;

    public static String make() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            sb.append(preset.charAt(random.nextInt(preset.length())));
        }

        return sb.toString();
    }

}
