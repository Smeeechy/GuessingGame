package animals;

import java.time.LocalTime;
import java.util.*;

public class GuessingGame {
    private static final Scanner SCANNER = new Scanner(System.in);
    private final List<String> YES_WORDS = List.of("Y", "YES", "YEAH", "YEP", "SURE", "RIGHT", "AFFIRMATIVE",
            "CORRECT", "INDEED", "YOU BET", "EXACTLY", "YOU SAID IT");
    private final List<String> NO_WORDS = List.of("N", "NO", "NO WAY", "NAH", "NOPE", "NEGATIVE",
            "I DON'T THINK SO", "YEAH NO");
    private final TreeSet<String> animals = new TreeSet<>();
    private NodeIdGenerator nodeIdGenerator;
    private Node root;

    public GuessingGame(Node root) {
        this.root = root;
    }

    public void startup() {
        if (this.root == null) {
            System.out.println("I want to learn about animals. \nWhich animal do you like most?");
            this.root = getAnimalFromUser();
            animals.add(root.getData2());
        }
        animals.addAll(getListOfAnimals(root));
    }

    public void playGuessingGame() {
        boolean running = true;
        while (running) {
            explainRulesAndConfirm();
            playNode(null, root);
            System.out.println("Do you want to play again?");
            running = validateFromUser();
        }
    }

    private boolean makeGuess(Node animal) {
        System.out.println("Is it " + animal.toStringIndefinite() + "?");
        return validateFromUser();
    }

    private void learn(Node parent, Node currentNode) {
        System.out.println("I give up. What animal do you have in mind?");
        Node actual = getAnimalFromUser();
        this.animals.add(actual.getData2());
        Node fact = learnDifferenceBetween(currentNode, actual);
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

    private Node learnDifferenceBetween(Node firstAnimal, Node secondAnimal) {
        Node fact = getFactFromUser(firstAnimal, secondAnimal);
        System.out.println("Is the statement correct for " + secondAnimal.toStringIndefinite() + "?");
        boolean trueForSecondAnimal = validateFromUser();
        System.out.println("I learned the following facts about animals:");
        if (trueForSecondAnimal) {
            fact.setLeft(firstAnimal);
            fact.setRight(secondAnimal);
            System.out.println("T" + firstAnimal.toStringDefinite().substring(1) + " " +
                    String.join(" ", fact.toNegatedStringArray()) + ".");
            System.out.println("T" + secondAnimal.toStringDefinite().substring(1) + " " + fact + ".");
        } else {
            fact.setLeft(secondAnimal);
            fact.setRight(firstAnimal);
            System.out.println("T" + firstAnimal.toStringDefinite().substring(1) + " " + fact + ".");
            System.out.println("T" + secondAnimal.toStringDefinite().substring(1) + " " +
                    String.join(" ", fact.toNegatedStringArray()) + ".");
        }
        System.out.println("I can distinguish these animals by asking the question:");
        System.out.println(fact.asQuestion());
        return fact;
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

    private void askForFact(Node firstAnimal, Node secondAnimal) {
        System.out.println("Specify a fact that distinguishes " +
                firstAnimal.toStringIndefinite() +
                " from " +
                secondAnimal.toStringIndefinite() + ".");
        System.out.println("The sentence should be of the format: 'It can/has/is ...'.");
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

    public void greet() {
        int hour = LocalTime.now().getHour();
        if (hour < 5 || hour >= 18) {
            System.out.println("Good evening\n");
        } else if (hour < 12) {
            System.out.println("Good morning\n");
        } else {
            System.out.println("Good afternoon\n");
        }
    }

    public void farewell() {
        if (new Random().nextBoolean()) {
            System.out.println("Goodbye");
        } else {
            System.out.println("Farewell");
        }
    }

    private void explainRulesAndConfirm() {
        System.out.println("You think of an animal, and I guess it.\n" +
                "Press enter when you're ready.");
        SCANNER.nextLine();
    }

    private void playNode(Node parent, Node currentNode) {
        if (currentNode.isAnimal()) {
            if (!makeGuess(currentNode)) {
                // guess was wrong, need to add a fact in place of this animal and the correct one
                learn(parent, currentNode);
                System.out.println("Nice! I've learned so much about animals!");
            } else {
                System.out.println("I win");
            }
        } else if (currentNode.isFact()) {
            System.out.println((currentNode).asQuestion());
            if (validateFromUser()) {
                playNode(currentNode, currentNode.getRight());
            } else {
                playNode(currentNode, currentNode.getLeft());
            }
        }
    }

    public Node getRoot() {
        return this.root;
    }

    public void setNodeIdGenerator(NodeIdGenerator nodeIdGenerator) {
        this.nodeIdGenerator = nodeIdGenerator;
    }

    public HashMap<String, Integer> getNodeCount() {
        return getNodeCount(0, root);
    }

    private HashMap<String, Integer> getNodeCount(int depth, Node node) {
        HashMap<String, Integer> result = new HashMap<>();
        HashMap<String, Integer> leftResult = new HashMap<>();
        HashMap<String, Integer> rightResult = new HashMap<>();

        // data collection
        if (node.isAnimal()) {
            result.put("animalCount", 1);
            result.put("animalMinDepth", depth);
            result.put("animalMaxDepth", depth);
            result.put("animalDepthSum", depth);
        } else {
            result.put("factCount", 1);
        }

        // recursion
        if (node.hasRight()) {
            rightResult = getNodeCount(depth + 1, node.getRight());
        }
        if (node.hasLeft()) {
            leftResult = getNodeCount(depth + 1, node.getLeft());
        }

        // aggregation
        result.put("animalCount", result.getOrDefault("animalCount", 0) +
                rightResult.getOrDefault("animalCount", 0) +
                leftResult.getOrDefault("animalCount", 0));
        result.put("factCount", result.getOrDefault("factCount", 0) +
                rightResult.getOrDefault("factCount", 0) +
                leftResult.getOrDefault("factCount", 0));
        result.put("animalMinDepth", Math.min(result.getOrDefault("animalMinDepth", Integer.MAX_VALUE),
                Math.min(rightResult.getOrDefault("animalMinDepth", Integer.MAX_VALUE),
                        leftResult.getOrDefault("animalMinDepth", Integer.MAX_VALUE))));
        result.put("animalMaxDepth", Math.max(result.getOrDefault("animalMaxDepth", Integer.MIN_VALUE),
                Math.max(rightResult.getOrDefault("animalMaxDepth", Integer.MIN_VALUE),
                        leftResult.getOrDefault("animalMaxDepth", Integer.MIN_VALUE))));
        result.put("animalDepthSum", result.getOrDefault("animalDepthSum", 0) +
                rightResult.getOrDefault("animalDepthSum", 0) +
                leftResult.getOrDefault("animalDepthSum", 0));
        return result;
    }

    public void printListOfAnimals() {
        System.out.println("Here are the animals I know:");
        this.animals.stream().sorted().forEach(animal -> System.out.println(" - " + animal));
        System.out.println();
    }

    private List<String> getListOfAnimals(Node node) {
        ArrayList<String> animals = new ArrayList<>();
        if (node.isAnimal()) {
            animals.add(node.getData2());
        } else {
            if (node.hasLeft()) {
                animals.addAll(getListOfAnimals(node.getLeft()));
            }
            if (node.hasRight()) {
                animals.addAll(getListOfAnimals(node.getRight()));
            }
        }
        return animals;
    }

    public void printFactsAboutAnimal() {
        System.out.println("Enter the animal:");
        Node target = getAnimalFromUser();
        if (!animals.contains(target.getData2())) {
            System.out.println("No facts about " + target.toStringDefinite() + ".");
        } else {
            System.out.println("Facts about " + target.toStringDefinite() + ":");
            printFactsAboutAnimal(root, target, new ArrayList<>());
        }
    }

    private void printFactsAboutAnimal(Node current, Node target, ArrayList<String[]> facts) {
        if (current.isAnimal()) {
            if (current.equals(target)) {
                facts.forEach(fact -> System.out.println(" - It " + String.join(" ", fact) + "."));
            }
        } else {
            if (current.hasRight()) {
                ArrayList<String[]> result = new ArrayList<>(facts);
                result.add(current.toStringArray());
                printFactsAboutAnimal(current.getRight(), target, result);
            }
            if (current.hasLeft()) {
                ArrayList<String[]> result = new ArrayList<>(facts);
                result.add(current.toNegatedStringArray());
                printFactsAboutAnimal(current.getLeft(), target, result);
            }
        }
    }

    public void showStats() {
        HashMap<String, Integer> nodeCounts = getNodeCount();
        int animalCount = nodeCounts.getOrDefault("animalCount", 1);
        int factCount = nodeCounts.getOrDefault("factCount", 0);
        int totalNodeCount = animalCount + factCount;
        System.out.println("The Knowledge Tree stats\n");
        System.out.println("- root node" + getTabs(6) + (root.isFact() ? "It " + root + "." : root.toStringIndefinite()));
        System.out.println("- total number of nodes" + getTabs(3) + totalNodeCount);
        System.out.println("- total number of animals" + getTabs(2) + animalCount);
        System.out.println("- total number of statements" + getTabs(1) + factCount);
        System.out.println("- height of the tree" + getTabs(3) + nodeCounts.getOrDefault("animalMaxDepth", 1));
        System.out.println("- minimum animal's depth" + getTabs(2) + nodeCounts.getOrDefault("animalMinDepth", 0));
        System.out.println("- average animal's depth" + getTabs(2) + (nodeCounts.getOrDefault("animalDepthSum", 0) / (double) animalCount));
        System.out.println();
    }

    private String getTabs(int num) {
        StringBuilder builder = new StringBuilder();
        builder.append("\t".repeat(Math.max(0, num)));
        return builder.toString();
    }

    public void printTree() {
        printTree("", null, root);
        System.out.println();
    }

    private void printTree(String prefix, Node parent, Node current) {
        if (current == null) return;
        if (parent == null) {
            System.out.println("└ " + (current.isFact() ? current.asQuestion() : current.toStringIndefinite()));
            printTree(prefix + " ", current, current.getRight());
            printTree(prefix + " ", current, current.getLeft());
        } else {
            if (current.isFact()) {
                if (current == parent.getLeft() || !parent.hasLeft()) {
                    System.out.println(prefix + "└ " + current.asQuestion());
                    printTree(prefix + " ", current, current.getRight());
                    printTree(prefix + " ", current, current.getLeft());
                } else {
                    System.out.println(prefix + "├ " + current.asQuestion());
                    printTree(prefix + "│", current, current.getRight());
                    printTree(prefix + "│", current, current.getLeft());
                }
            } else {
                if (current == parent.getLeft() || !parent.hasLeft()) {
                    System.out.println(prefix + "└ " + current);
                } else {
                    System.out.println(prefix + "├ " + current);
                }
            }
        }

    }
}
