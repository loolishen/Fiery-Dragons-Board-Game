/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dizhen.game;

/**
 *
 * @author DavidL
 */
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageNameExtractor {
    private String name;
    private int id;

    // Inner class to hold the result
    public class Result {
        private String name;
        private int id;

        public Result(String name, int id) {
            this.name = name;
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public int getId() {
            return id;
        }
    }

    public Result extractNameAndNumber(String imageName) {
        // Regular expression to match the name and number in the file name
        Pattern pattern = Pattern.compile("^(\\w+) \\((\\d+)\\)\\.jpg$");
        Matcher matcher = pattern.matcher(imageName);

        if (matcher.find()) {
            this.name = matcher.group(1); // Extract the name
            this.id = Integer.parseInt(matcher.group(2)); // Extract the number
            return new Result(this.name, this.id);
        } else {
            System.out.println("No match found.");
            return null;
        }
    }

    // Getters for name and id if needed outside of the Result object
    public String getName() {
        return this.name;
    }

    public int getId() {
        return this.id;
    }
}

