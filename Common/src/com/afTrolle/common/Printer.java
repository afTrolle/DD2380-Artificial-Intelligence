package com.afTrolle.common;

public class Printer {


    public static void printArray(Double[] ans){
        System.out.print("1 "+ans.length);
        for (int i = 0; i < ans.length ; i++) {
            System.out.print(" "+ans[i]);
        }
        System.out.println();
    }
}
