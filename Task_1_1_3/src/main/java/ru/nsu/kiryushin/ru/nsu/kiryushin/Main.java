package ru.nsu.kiryushin;

import java.util.Scanner;

public class Main{
    public static void main(String[] args){
        Scanner in = new Scanner(System.in);
        String text = in.nextLine();
        Parser parser = new Parser(text);
        Expression e = parser.parse();
        System.out.println(e.toString());
        System.out.println((e.derivative("x")).toString());
        System.out.println(e.eval("x = 5"));
    }
}