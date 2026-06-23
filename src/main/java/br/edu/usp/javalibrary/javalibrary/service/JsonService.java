package br.edu.usp.javalibrary.javalibrary.service;

import com.google.gson.Gson;
// import com.google.gson.reflect.TypeToken;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
// import java.nio.file.attribute.FileAttribute;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class JsonService {
    private final Gson converter;

    private static JsonService instance;
    public static JsonService getInstance(){
        if (instance == null) instance = new JsonService();
        return instance;
    }

    private JsonService(){
        this.converter = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>) (src, typeOfSrc, context) -> 
                        new JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (json, typeOfT, context) -> 
                        LocalDateTime.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .setPrettyPrinting() // Deixa os arquivos .json indentados e fáceis de ler
                .create();
    }

    public <T> T loadJson(String filePath, Type typeOfT) throws IOException {
        final Path path = Paths.get(filePath);
        if (!Files.exists(path)) return null;
        final String json = Files.readString(path, StandardCharsets.UTF_8);
        return converter.fromJson(json, typeOfT);
    }

    public <T> void saveJson(String filePath, T data) throws IOException {
        final Path path = Paths.get(filePath);
        final String json = converter.toJson(data);
        Files.writeString(path, json, StandardCharsets.UTF_8);
    }

}
