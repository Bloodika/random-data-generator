package com.bloodika.randomdatagenerator;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class ObjectGenerator {

    public static String generate(final GenerationOptions generatedOptions, Object... additionalCharacters) {
        final List<Object> charactersToGenerate = new ArrayList<>(Arrays.stream(additionalCharacters).toList());
        final Random randomNumber = new Random();
        handleLetterInclusion(generatedOptions.isIncludeLetters(), 'a', 'z', charactersToGenerate);
        handleLetterInclusion(generatedOptions.isIncludeUpperCaseLetters(), 'A', 'Z', charactersToGenerate);
        handleNumberInclusion(generatedOptions.isIncludeNumbers(), charactersToGenerate);
        handleSpecialCharacterInclusion(generatedOptions.isIncludeSpecialCharacters(), charactersToGenerate);
        return generateString(generatedOptions, charactersToGenerate, randomNumber);

    }

    private static String generateString(GenerationOptions generatedOptions, List<Object> charactersToGenerate, Random randomNumber) {
        StringBuilder generatedValue = new StringBuilder();
        for (int i = 0; i < generatedOptions.getGeneratedLength(); i++) {
            generatedValue.append(charactersToGenerate.get(randomNumber.nextInt(charactersToGenerate.size())).toString());
        }
        return generatedValue.toString();
    }

    private static void handleSpecialCharacterInclusion(final boolean includeSpecialCharacters, final List<Object> charactersToGenerate) {
        if (includeSpecialCharacters) {
            charactersToGenerate.addAll(Arrays.stream("<([{\\^-=$!|]})?*+.>".split("")).toList());
        }
    }

    private static void handleNumberInclusion(boolean includeNumbers, final List<Object> charactersToGenerate) {
        if (includeNumbers) {
            for (int number = 0; number <= 9; number++) {
                charactersToGenerate.add(number);
            }
        }
    }

    private static void handleLetterInclusion(final boolean needToInclusion, final char fromLetter, final char toLetter, final List<Object> charactersToGenerate) {
        if (needToInclusion) {
            for (char letter = fromLetter; letter <= toLetter; letter++) {
                charactersToGenerate.add(letter);
            }
        }
    }

    public static void main(String[] args) {
        MongoConfig mongoConfig = new MongoConfig();
        MongoClient mongoClient = mongoConfig.mongoClient();
        MongoCollection<Document> collection = mongoClient.getDatabase("local").getCollection("documents");


        GenerationOptions generationOptions = GenerationOptions.builder().includeLetters(true).includeNumbers(true).includeUpperCaseLetters(true).generatedLength(10).build();
        Random random = new Random();
        final List<String> fields = List.of("PONumber", "Queue", "Process", "DocumentNumber");
        final List<String> queues = List.of("Queue1", "Queue2", "");
        for (int i = 0; i < 1000_000; i++) {
            final Document document = new Document();
            for (String field : fields) {
                if (field.equals("Queue")) {
                    document.put("Queue", queues.get(random.nextInt(queues.size())));
                } else {
                    final String generatedValue = ObjectGenerator.generate(generationOptions);
                    document.put(field, generatedValue);
                }
            }
            collection.insertOne(document);
        }
    }

}
