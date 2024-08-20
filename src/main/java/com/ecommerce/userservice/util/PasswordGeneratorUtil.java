package com.ecommerce.userservice.util;

import com.ecommerce.userservice.config.ApplicationProperties;
import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.stereotype.Component;

@Component
public class PasswordGeneratorUtil {
    private final PasswordGenerator passwordGenerator;
    private final CharacterRule lowerCaseRule;
    private final CharacterRule upperCaseRule;
    private final CharacterRule digitRule;
    private final CharacterRule splCharRule;
    private final ApplicationProperties applicationProperties;
    
    public PasswordGeneratorUtil(ApplicationProperties applicationProperties) {
        passwordGenerator = new PasswordGenerator();
        
        // Lower case rule
        lowerCaseRule = new CharacterRule(EnglishCharacterData.LowerCase);
        lowerCaseRule.setNumberOfCharacters(applicationProperties.getNumberOfLowerCaseCharacter());
        
        // Upper case rule
        upperCaseRule = new CharacterRule(EnglishCharacterData.UpperCase);
        upperCaseRule.setNumberOfCharacters(applicationProperties.getNumberOfUpperCaseCharacter());
        
        // Digit rule
        digitRule = new CharacterRule(EnglishCharacterData.Digit);
        digitRule.setNumberOfCharacters(applicationProperties.getNumberOfDigitCharacter());
        
        // Special characters rule
        CharacterData specialChars = new CharacterData() {
            public String getErrorCode() {
                return null;
            }
            
            public String getCharacters() {
                return applicationProperties.getSpecialCharacters();
            }
        };
        splCharRule = new CharacterRule(specialChars);
        splCharRule.setNumberOfCharacters(applicationProperties.getNumberOfSpecialCharacter());
        this.applicationProperties = applicationProperties;
    }
    
    public String generatePassword() {
        return passwordGenerator.generatePassword(applicationProperties.getPasswordGeneratedLength(),
                splCharRule, lowerCaseRule,
                upperCaseRule, digitRule);
    }
}
