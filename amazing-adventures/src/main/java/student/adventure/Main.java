package student.adventure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import java.io.File;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
//import com.google.gson.Gson;

public class Main {
    public static void main(String[] args) {
        // TODO: Run an Adventure game on the console
        gameplay game = new gameplay();
        game.run();
    }
}