package com.teksen.newsapp.util;

public class PasswordValidator {

    private int minLength;
    private int maxLength;
    private boolean requireLetters;
    private boolean requireNumbers;
    private boolean requireSpecialCharacters;

    public PasswordValidator() {
    }

    public PasswordValidator(int minLength, int maxLength, boolean requireLetters, boolean requireNumbers, boolean requireSpecialCharacters) {
        this.minLength = minLength;
        this.maxLength = maxLength;
        this.requireLetters = requireLetters;
        this.requireNumbers = requireNumbers;
        this.requireSpecialCharacters = requireSpecialCharacters;
    }

    public void setMinLength(int minLength) {
        this.minLength = minLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public void setRequireLetters(boolean requireLetters) {
        this.requireLetters = requireLetters;
    }

    public void setRequireNumbers(boolean requireNumbers) {
        this.requireNumbers = requireNumbers;
    }

    public void setRequireSpecialCharacters(boolean requireSpecialCharacters) {
        this.requireSpecialCharacters = requireSpecialCharacters;
    }

    public boolean validate(String password) {
        if (password.length() < minLength || password.length() > maxLength) {
            return false;
        }

        if (requireLetters && !containsLetters(password)) {
            return false;
        }

        if (requireNumbers && !containsNumbers(password)) {
            return false;
        }

        if (requireSpecialCharacters && !containsSpecialCharacters(password)) {
            return false;
        }

        return true;
    }

    private boolean containsLetters(String password) {
        return password.matches(".*[a-zA-Z]+.*");
    }

    private boolean containsNumbers(String password) {
        return password.matches(".*\\d+.*");
    }

    private boolean containsSpecialCharacters(String password) {
        return password.matches(".*[!@#$%^&*()\\-=_+\\[\\]{}|;':\",.<>/?]+.*");
    }

    public String getErrorDetails(String password) {
        StringBuilder errorDetails = new StringBuilder();

        if (password.length() < minLength) {
            errorDetails.append("Minimum " + minLength + " karakter gereklidir. ");
        }

        if (password.length() > maxLength) {
            errorDetails.append("Maksimum " + maxLength + " karakter olmalıdır. ");
        }

        if (requireLetters && !containsLetters(password)) {
            errorDetails.append("En az bir harf içermelidir. ");
        }

        if (requireNumbers && !containsNumbers(password)) {
            errorDetails.append("En az bir rakam içermelidir. ");
        }

        if (requireSpecialCharacters && !containsSpecialCharacters(password)) {
            errorDetails.append("En az bir özel karakter içermelidir. ");
        }

        return errorDetails.toString();
    }
}
