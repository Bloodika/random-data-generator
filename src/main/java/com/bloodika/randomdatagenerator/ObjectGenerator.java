package com.bloodika.randomdatagenerator;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ObjectGenerator {

    public static String generate(final GenerationOptions generatedOptions) {
        final List<Object> charactersToGenerate = new ArrayList<>();
        final Random randomNumber = new Random();
        handleLetterInclusion(generatedOptions.isIncludeLetters(), 'a', 'z', charactersToGenerate);
        handleLetterInclusion(generatedOptions.isIncludeUpperCaseLetters(), 'A', 'Z', charactersToGenerate);
        handleNumberInclusion(generatedOptions.isIncludeNumbers(), charactersToGenerate);
        handleSpecialCharacterInclusion(generatedOptions.isIncludeSpecialCharacters(), charactersToGenerate);
        String generatedValue = "";
        for (int i = 0; i < generatedOptions.getGeneratedLength(); i++) {
            generatedValue += charactersToGenerate.get(randomNumber.nextInt(charactersToGenerate.size())).toString();
        }
        return generatedValue;

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

        final String generatedValue = ObjectGenerator.generate(GenerationOptions.builder().includeLetters(true).includeNumbers(true).includeUpperCaseLetters(true).includeSpecialCharacters(true).generatedLength(10).build());
        System.out.println(generatedValue);

    }

}
