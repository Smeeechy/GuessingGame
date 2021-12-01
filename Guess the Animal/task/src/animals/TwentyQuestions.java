package animals;

import java.time.LocalTime;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class TwentyQuestions {
    private static final Scanner SCANNER = new Scanner(System.in);
    private NodeIdGenerator nodeIdGenerator;
    private Node root;

    private final List<String> YES_WORDS = List.of("Y", "YES", "YEAH", "YEP", "SURE", "RIGHT", "AFFIRMATIVE",
            "CORRECT", "INDEED", "YOU BET", "EXACTLY", "YOU SAID IT");
    private final List<String> NO_WORDS = List.of("N", "NO", "NO WAY", "NAH", "NOPE", "NEGATIVE",
            "I DON'T THINK SO", "YEAH NO");

    public TwentyQuestions(Node root) {
        this.root = root;
    }

    public void start() {
        if (this.root == null) {
            startWithoutRoot();
        } else {
            startWithRoot();
        }
    }

    private void startWithoutRoot() {
        greet();
        this.root = startupWithUserInput();
        mainLoop();
        farewell();
    }

    private void startWithRoot() {
        greet();
        startupWithoutUserInput();
        mainLoop();
        farewell();
    }

    public Node getRoot() {
        return this.root;
    }

    private Node startupWithUserInput() {
        System.out.println("I want to learn about animals. \nWhich animal do you like most?");
        Node animal = getAnimalFromUser();
        System.out.println("Wonderful! I've learned so much about animals!\n" +
                "Let's play a game!");
        return animal;
    }

    private void startupWithoutUserInput() {
        System.out.println("I know a lot about animals.\nLet's play a game!");
    }

    private void mainLoop() {
        boolean running = true;
        while (running) {
            explainRulesAndConfirm();
            evaluateFromNode(null, root);
            System.out.println("Do you want to play again?");
            running = validateFromUser();
        }
    }

    private void explainRulesAndConfirm() {
        System.out.println("You think of an animal, and I guess it.\n" +
                "Press enter when you're ready.");
        SCANNER.nextLine();
    }

    private void evaluateFromNode(Node parent, Node currentNode) {
        if (currentNode.isAnimal()) {
            if (!guess(currentNode)) {
                // guess was wrong, need to add a fact in place of this animal and the correct one
                learn(parent, currentNode);
                System.out.println("Nice! I've learned so much about animals!");
            } else {
                System.out.println("I win");
            }
        } else if (currentNode.isFact()) {
            System.out.println((currentNode).asQuestion());
            if (validateFromUser()) {
                evaluateFromNode(currentNode, currentNode.getRight());
            } else {
                evaluateFromNode(currentNode, currentNode.getLeft());
            }
        }
    }

    private void learn(Node parent, Node currentNode) {
        System.out.println("I give up. What animal do you have in mind?");
        Node actual = getAnimalFromUser();
        Node fact = learnDifference(currentNode, actual);
        if (root == currentNode) {
            root = fact;
        } else {
            if (parent.getRight() == currentNode) {
                parent.setRight(fact);
            } else {
                parent.setLeft(fact);
            }
        }
    }

    private boolean guess(Node animal) {
        System.out.println("Is it " + animal.toStringIndefinite() + "?");
        return validateFromUser();
    }

    private Node learnDifference(Node firstAnimal, Node secondAnimal) {
        Node fact = getFactFromUser(firstAnimal, secondAnimal);
        System.out.println("Is the statement correct for " + secondAnimal.toStringIndefinite() + "?");
        boolean trueForSecondAnimal = validateFromUser();
        System.out.println("I learned the following facts about animals:");
        if (trueForSecondAnimal) {
            fact.setLeft(firstAnimal);
            fact.setRight(secondAnimal);
            System.out.println("T" + firstAnimal.toStringDefinite().substring(1) + " " + fact.negate() + ".");
            System.out.println("T" + secondAnimal.toStringDefinite().substring(1) + " " + fact + ".");
        } else {
            fact.setLeft(secondAnimal);
            fact.setRight(firstAnimal);
            System.out.println("T" + firstAnimal.toStringDefinite().substring(1) + " " + fact + ".");
            System.out.println("T" + secondAnimal.toStringDefinite().substring(1) + " " + fact.negate() + ".");
        }
        System.out.println("I can distinguish these animals by asking the question:");
        System.out.println(fact.asQuestion());
        return fact;
    }

    private void askForFact(Node firstAnimal, Node secondAnimal) {
        System.out.println("Specify a fact that distinguishes " +
                firstAnimal.toStringIndefinite() +
                " from " +
                secondAnimal.toStringIndefinite() + ".");
        System.out.println("The sentence should be of the format: 'It can/has/is ...'.");
    }

    private Node getFactFromUser(Node firstAnimal, Node secondAnimal) {
        String input;
        boolean valid = false;
        do {
            askForFact(firstAnimal, secondAnimal);
            input = SCANNER.nextLine().trim();
            if (input.toLowerCase().matches("^(it) (can|has|is) .*")) {
                valid = true;
            } else {
                System.out.println("The examples of a statement:\n" +
                        " - It can fly\n - It has a horn\n - It is a mammal");
            }
        } while (!valid);
        String[] splitInput = input.toLowerCase().split("\\s+", 3);
        return new Node(this.nodeIdGenerator.getNextId(), splitInput[1], splitInput[2]);
    }

    private void farewell() {
        if (new Random().nextBoolean()) {
            System.out.println("Goodbye");
        } else {
            System.out.println("Farewell");
        }
    }

    private boolean validateFromUser() {
        boolean correct;
        while (true) {
            String response = SCANNER.nextLine().trim();
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

    private Node getAnimalFromUser() {
        String input = SCANNER.nextLine().trim().toLowerCase();
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
        return new Node(this.nodeIdGenerator.getNextId(), article, noun);
    }

    public void setNodeIdGenerator(NodeIdGenerator nodeIdGenerator) {
        this.nodeIdGenerator = nodeIdGenerator;
    }

    public int getNodeCount() {
        return getNodeCount(root);
    }

    private int getNodeCount(Node node) {
        int sum = 1;
        if (node.hasLeft()) {
            sum += getNodeCount(node.getLeft());
        }
        if (node.hasRight()) {
            sum += getNodeCount(node.getRight());
        }
        return sum;
    }
}
