package animals;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Node root = loadTree(args);
        NodeIdGenerator generator;
        TwentyQuestions game;
        if (root == null) {
            generator = new NodeIdGenerator(1);
            game = new TwentyQuestions(generator);
        } else {
            generator = loadIdGenerator();
            game = new TwentyQuestions(generator, root);
        }
        saveIdGenerator(generator);
        saveTree(args, game.getRoot());
    }

    private static void saveTree(String[] args, Node root) {
        ObjectMapper mapper = getObjectMapper(args);
        File file = getFile(args);
        if (file != null && !file.exists()) {
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
        File file = getFile(args);
        if (file != null && !file.exists()) {
            return null;
        } else {
            try {
                return mapper.readValue(file, Node.class);
            } catch (JsonParseException parseException) {
                System.out.println(parseException.getMessage());
            } catch (JsonMappingException mappingException) {
                System.out.println(mappingException.getMessage());
            } catch (IOException ioException) {
                System.out.println(ioException.getMessage());
            }
        }
        return null;
    }

    private static void saveIdGenerator(NodeIdGenerator nodeIdGenerator) {
        ObjectMapper mapper = new JsonMapper();
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File("id_generator.json"), nodeIdGenerator);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private static NodeIdGenerator loadIdGenerator() {
        NodeIdGenerator nodeIdGenerator = null;
        ObjectMapper mapper = new JsonMapper();
        try {
            nodeIdGenerator = mapper.readValue(new File("id_generator.json"), NodeIdGenerator.class);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return nodeIdGenerator;
    }

    private static ObjectMapper getObjectMapper(String[] args) {
        ObjectMapper mapper = null;
        if (args.length == 2 && args[0].equals("-type")) {
            switch (args[1]) {
                case "json":
                    mapper = new JsonMapper();
                    break;
                case "xml":
                    mapper = new XmlMapper();
                    break;
                case "yaml":
                    mapper = new YAMLMapper();
                    break;
                default:
                    break;
            }
        }
        return mapper;
    }

    private static File getFile(String[] args) {
        if (args.length == 2 && args[0].equals("-type")) {
            switch (args[1]) {
                case "json":
                    return new File("animals.json");
                case "xml":
                    return new File("animals.xml");
                case "yaml":
                    return new File("animals.yaml");
                default:
                    break;
            }
        }
        return null;
    }
}
