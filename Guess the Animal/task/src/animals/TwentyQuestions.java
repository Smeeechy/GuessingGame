package animals;

import java.time.LocalTime;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class TwentyQuestions {
    private final Scanner scanner = new Scanner(System.in);

//    private final String ANSWER;
    private final List<String> ARTICLES = List.of("THE", "A", "AN");
    private final List<String> YES_WORDS = List.of("Y", "YES", "YEAH", "YEP", "SURE", "RIGHT", "AFFIRMATIVE",
            "CORRECT", "INDEED", "YOU BET", "EXACTLY", "YOU SAID IT");
    private final List<String> NO_WORDS = List.of("N", "NO", "NO WAY", "NAH", "NOPE", "NEGATIVE",
            "I DON'T THINK SO", "YEAH NO");
    private final List<String> GOODBYES = List.of("Goodbye", "Farewell");

    public TwentyQuestions() {
//        this.ANSWER = answer;
        init();
    }

    private void init() {
        greet();

        // getting input from user
        System.out.println("Enter an animal:");
        String input = scanner.nextLine().trim().toLowerCase();
        String article, noun;
        if (input.matches("^(a|an)\\s.*")) {
            article = input.split("\\s+")[0];
            noun = input.split("\\s+", 2)[1];
        } else if (input.matches("^the\\s.*")) {
            if (input.split("\\s", 2)[1].matches("^(a|e|i|o|u).*")) {
                article = "an";
            } else {
                article = "a";
            }
            noun = input.split("\\s+", 2)[1];
        } else {
            if (input.matches("^(a|e|i|o|u).*")) {
                article = "an";
            } else {
                article = "a";
            }
            noun = input;
        }

        // making a guess
        StringBuilder builder = new StringBuilder("Is it ")
                .append(article).append(" ")
                .append(noun).append("?");
        System.out.println(builder.toString());

        // getting a 'yes'- or 'no'-adjacent response from the user
        boolean correct;
        while (true) {
            String response = scanner.nextLine().trim();
            if (response.endsWith(".") || response.endsWith("!")) {
                response = response.substring(0, response.length() - 1);
            }
            if (YES_WORDS.contains(response.toUpperCase())) {
                correct = true;
                break;
            } else if (NO_WORDS.contains(response.toUpperCase())) {
                correct = false;
                break;
            } else {
                if (new Random().nextBoolean()) {
                    System.out.println("Please use yes or no responses only");
                } else {
                    System.out.println("Bad input: enter yes or no");
                }
            }
        }
        System.out.println("You answered: " + (correct ? "Yes" : "No"));

        // farewell
        if (new Random().nextBoolean()) {
            System.out.println("Goodbye");
        } else {
            System.out.println("Farewell");
        }
    }

    private void greet() {
        int hour = LocalTime.now().getHour();
        if (hour < 5 || hour >= 18) {
            System.out.println("Good evening\n");
        } else if (hour < 12) {
            System.out.println("Good morning\n");
        } else {
            System.out.println("Good afternoon\n");
        }
    }
}
