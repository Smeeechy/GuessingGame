package animals;

import java.time.LocalTime;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class TwentyQuestions {
    private final Scanner scanner = new Scanner(System.in);

    private final List<String> YES_WORDS = List.of("Y", "YES", "YEAH", "YEP", "SURE", "RIGHT", "AFFIRMATIVE",
            "CORRECT", "INDEED", "YOU BET", "EXACTLY", "YOU SAID IT");
    private final List<String> NO_WORDS = List.of("N", "NO", "NO WAY", "NAH", "NOPE", "NEGATIVE",
            "I DON'T THINK SO", "YEAH NO");

    public TwentyQuestions() {
        init();
    }

    private void init() {
        greet();

        System.out.println("Enter the first animal:");
        Animal firstAnimal = getAnimalFromUser();
        System.out.println("Enter the second animal:");
        Animal secondAnimal = getAnimalFromUser();
        Fact fact = getFactFromUser(firstAnimal, secondAnimal);
        System.out.println("Is it correct for " + secondAnimal.toStringIndefinite() + "?");
        boolean trueForSecondAnimal = validateFromUser();
        System.out.println("I learned the following facts about animals:");
        if (trueForSecondAnimal) {
            System.out.println("T" + firstAnimal.toStringDefinite().substring(1) + " " + fact.negate() + ".");
            System.out.println("T" + secondAnimal.toStringDefinite().substring(1) + " " + fact + ".");
        } else {
            System.out.println("T" + firstAnimal.toStringDefinite().substring(1) + " " + fact + ".");
            System.out.println("T" + secondAnimal.toStringDefinite().substring(1) + " " + fact.negate() + ".");
        }
        System.out.println("I can distinguish these animals by asking the question:");
        System.out.println(fact.asQuestion());

        farewell();
    }

    private void askForFact(Animal firstAnimal, Animal secondAnimal) {
        System.out.println("Specify a fact that distinguishes " +
                firstAnimal.toStringIndefinite() +
                " from " +
                secondAnimal.toStringIndefinite() + ".");
        System.out.println("The sentence should be of the format: 'It can/has/is ...'.");
    }

    private Fact getFactFromUser(Animal firstAnimal, Animal secondAnimal) {
        String input;
        boolean valid = false;
        do {
            askForFact(firstAnimal, secondAnimal);
            input = scanner.nextLine().trim();
            if (input.toLowerCase().matches("^(it) (can|has|is) .*")) {
                valid = true;
            } else {
                System.out.println("The examples of a statement:\n" +
                        " - It can fly\n - It has horn\n - It is a mammal");
            }
        } while (!valid);
        String[] splitInput = input.toLowerCase().split("\\s+", 3);
        return new Fact(splitInput[1], splitInput[2]);
    }

    private void farewell() {
        if (new Random().nextBoolean()) {
            System.out.println("Goodbye");
        } else {
            System.out.println("Farewell");
        }
    }

    // getting a 'yes'- or 'no'-adjacent response from the user
    private boolean validateFromUser() {
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
        return correct;
    }

    private void makeGuess(String[] articleAndNoun) {
        System.out.println("Is it " + articleAndNoun[0] + " " + articleAndNoun[1] + "?");
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

    // getting input from user
    private Animal getAnimalFromUser() {
        String input = scanner.nextLine().trim().toLowerCase();
        String article, noun;
        if (input.matches("^(a|an)\\s.*")) {
            article = input.split("\\s+")[0];
            noun = input.split("\\s+", 2)[1];
        } else if (input.matches("^the\\s.*")) {
            if (input.split("\\s", 2)[1].matches("^([aeiou]).*")) {
                article = "an";
            } else {
                article = "a";
            }
            noun = input.split("\\s+", 2)[1];
        } else {
            if (input.matches("^([aeiou]).*")) {
                article = "an";
            } else {
                article = "a";
            }
            noun = input;
        }
        return new Animal(article, noun);
    }
}
