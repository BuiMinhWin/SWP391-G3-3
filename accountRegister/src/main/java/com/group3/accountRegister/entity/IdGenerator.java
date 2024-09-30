package com.group3.accountRegister.entity;

import java.util.Random;

public class IdGenerator {

    public static String generateCustomUserId() {
        Random random = new Random();

        char letter1 = (char) (random.nextInt(26) + 'A');
        char letter2 = (char) (random.nextInt(26) + 'A');
        char letter3 = (char) (random.nextInt(26) + 'A');

        int number1 = random.nextInt(10);
        int number2 = random.nextInt(10);
        int number3 = random.nextInt(10);
        int number4 = random.nextInt(10);
        int number5 = random.nextInt(10);

        return "" + letter1 + letter2 + letter3 + number1 + number2 + number3 + number4 + number5;
    }
}

