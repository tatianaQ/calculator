import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class Main {
    private static final String[] ARABIC_NUMERALS = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    private static final String[] ROMAN_NUMERALS = {"I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"};
    private static final int[] EQUIVALENTS = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    private static final Map<String, Integer> ARABIC_MAP = createMap(ARABIC_NUMERALS, EQUIVALENTS);
    private static final Map<String, Integer> ROMAN_MAP = createMap(ROMAN_NUMERALS, EQUIVALENTS);

    public static void main(String[] args) throws IOException{
        System.out.print("Ввод: ");
        Scanner in = new Scanner(System.in);
        String input = in.nextLine();
        String result = calc(input);
        System.out.println("Вывод: " + result);
    }

    private static Map<String, Integer> createMap(String[] keys, int[] values) {
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < keys.length; i++) {
            map.put(keys[i], values[i]);
        }
        return map;
    }

    private static String calc(String input) throws IOException {
        String[] parsed = input.trim().split(" ");

        if (parsed.length != 3) {
            throw new IOException("Разрешено только два операнда и один оператор");
        }

        if (!isValidOperator(parsed[1])) {
            throw new IOException("Оператор должен быть одним из (+ - * /)");
        }

        boolean isRoman1 = isRoman(parsed[0]);
        boolean isRoman2 = isRoman(parsed[2]);
        if (isRoman1 && !isRoman2 || !isRoman1 && isRoman2) {
            throw new IOException ("Используются одновременно разные системы счисления");
        }

        int num1 = getNumericValue(parsed[0]);
        int num2 = getNumericValue(parsed[2]);
        int result = calculate(num1, num2, parsed[1]);

        return isRoman1 ? toRoman(result) : String.valueOf(result);
    }

    private static int calculate(int num1, int num2, String operator) throws IOException{
        switch (operator) {
            case "+":
                return num1 + num2;
            case "-":
                return num1 - num2;
            case "*":
                return num1 * num2;
            case "/":
                if (num2 == 0) {
                    throw new ArithmeticException("Деление на ноль запрещено");
                }
                return num1 / num2;
            default:
                throw new IOException("Произошла неожиданная ошибка");
        }
    }

    private static boolean isValidInput(String value, String[] validNumbers) {
        for (String s : validNumbers) {
            if (value.equals(s)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isValidOperator(String value) {
        return "+-*/".contains(value) && value.length() == 1;
    }

    private static int getNumericValue(String value)throws IOException  {
        Map<String, Integer> map = isRoman(value) ? ROMAN_MAP : ARABIC_MAP;
        if (isValidInput(value, map.keySet().toArray(new String[0]))) {
            return map.get(value);
        } else {
            throw new IOException("Неверный операнд: " + value);
        }
    }

    private static boolean isRoman(String value) {
        return value.matches("[IVXLCDM]+");
    }

    private static String toRoman(int num) {
        if (num <= 0) {
            throw new ArithmeticException("Римское число не может быть отрицательным или нулем");
        } else if (num <= 10) {
            return ROMAN_NUMERALS[num - 1];
        } else {
            return convertToRoman(num);
        }
    }

    private static String convertToRoman(int num) {
        StringBuilder result = new StringBuilder();
        int[] values = {100, 90, 50, 40, 10, 9, 5, 4, 1};
        String[] numerals = {"C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};

        for (int i = 0; i < values.length; i++) {
            while (num >= values[i]) {
                num -= values[i];
                result.append(numerals[i]);
            }
        }
        return result.toString();
    }
}