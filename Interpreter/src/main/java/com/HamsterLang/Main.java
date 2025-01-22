package com.HamsterLang;

import java.util.Objects;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        String user = System.getProperty("user.name");
        System.out.println("Hello " + user + " This is the Hamster programming language!");
        while (true)
        {
            System.out.print(">> ");
            Scanner myObj = new Scanner(System.in);  // Create a Scanner object
            String userInput = myObj.nextLine();  // Read user input
            if (Objects.equals(userInput, "exit"))
                break;
//            Repl.Repl.ProcessInput(userInput);
        }
    }
}