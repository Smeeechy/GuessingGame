package animals;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Node root = loadTree(args);
        GuessingGame game;
        if (root == null) {
            game = new GuessingGame(null);
            game.setNodeIdGenerator(new NodeIdGenerator(1));
        } else {
            game = new GuessingGame(root);
            HashMap<String, Integer> nodeCounts = game.getNodeCount();
            int nodeTotal = nodeCounts.getOrDefault("animals", 0) + nodeCounts.getOrDefault("facts", 0);
            game.setNodeIdGenerator(new NodeIdGenerator(nodeTotal));
        }
        game.greet();
        game.startup();
        saveTree(args, game.getRoot());
        System.out.println("Welcome to the animal expert system!\n");
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        while (running) {
            displayMenu();
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    game.playGuessingGame();
                    saveTree(args, game.getRoot());
                    break;
                case 2:
                    game.printListOfAnimals();
                    break;
                case 3:
                    game.printFactsAboutAnimal();
                    break;
                case 4:
                    game.showStats();
                    break;
                case 5:
                    game.printTree();
                    break;
                case 0:
                default:
                    running = false;
                    break;
            }
        }
        game.farewell();
    }

    private static void displayMenu() {
        System.out.println("What do you want to do:\n" +
                "\n" +
                "1. Play the guessing game\n" +
                "2. List of all animals\n" +
                "3. Search for an animal\n" +
                "4. Calculate statistics\n" +
                "5. Print the Knowledge Tree\n" +
                "0. Exit");
    }

    private static void saveTree(String[] args, Node root) {
        ObjectMapper mapper = getObjectMapper(args);
        File file = getTreeFile(args);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ioException) {
                System.out.println("Could not create new file");
            }
        }
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, root);
        } catch (IOException ioException) {
            System.out.println("Could not write value of " + root + " to " + file);
            System.out.println(ioException.getMessage());
        }
    }

    private static Node loadTree(String[] args) {
        ObjectMapper mapper = getObjectMapper(args);
        File file = getTreeFile(args);
        try {
            return mapper.readValue(file, Node.class);
        } catch (IOException ioException) {
            return null;
        }
    }

    private static ObjectMapper getObjectMapper(String[] args) {
        ObjectMapper mapper;
        if (args.length == 2 && args[0].equals("-type")) {
            switch (args[1]) {
                case "xml":
                    mapper = new XmlMapper();
                    break;
             /* maybe jackson's yaml mapper is busted? it breaks everything.
                serializes just fine, but refuses to deserialize properly */
//                case "yaml":
//                    mapper = new YAMLMapper();
//                    break;
                default:
                    mapper = new JsonMapper();
                    break;
            }
        } else {
            mapper = new JsonMapper();
        }
        return mapper;
    }

    private static File getTreeFile(String[] args) {
        if (args.length == 2 && args[0].equals("-type")) {
            switch (args[1]) {
                case "xml":
                    return new File("animals.xml");
                case "yaml":
                    return new File("animals.yaml");
                default:
                    return new File("animals.json");
            }
        }
        return new File("animals.json");
    }
}
