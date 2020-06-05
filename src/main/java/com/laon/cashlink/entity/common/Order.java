package com.laon.cashlink.entity.common;

import lombok.*;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    private String type;
    private String power;

    public static Order parse(String str) {
        String[] arr = str.split("\\|");
        String type, power;
        try {
            if (arr.length == 1) {
                type = arr[0].toUpperCase();
                power = "ASC";
            } else {
                type = arr[0].toUpperCase();
                power = arr[1].toUpperCase();

                if (!power.equals("ASC") && !power.equals("DESC")) {
                    power = "ASC";
                }
            }
        } catch (Exception e) {
            type = "NONE";
            power = "ASC";
        }
        return new Order(type, power);
    }

}