package com.bloodika.randomdatagenerator;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Builder
public class GenerationOptions {
    boolean includeLetters;
    boolean includeUpperCaseLetters;
    boolean includeSpecialCharacters;
    boolean includeNumbers;
    int generatedLength;

}
