package animals;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Node root = loadTree(args);
        TwentyQuestions game;
        if (root == null) {
            game = new TwentyQuestions(null);
            game.setNodeIdGenerator(new NodeIdGenerator(1));
        } else {
            game = new TwentyQuestions(root);
            game.setNodeIdGenerator(new NodeIdGenerator(game.getNodeCount()));
        }
        game.start();
        saveTree(args, game.getRoot());
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
            System.out.println(ioException.getMessage());
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
             /* maybe jackson's yamlmapper is busted? it breaks everything.
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
