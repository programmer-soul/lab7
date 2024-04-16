package main;

import java.util.InputMismatchException;
import java.util.Scanner;
/**
 * Класс для ввода значений разных типов с условиями и проверкой
 * @author Matvei Baranov
 */
public class Input {
    /**
     * @param title подсказа для ввода
     * @param CanBeZero - может принимать значение нуля
     * @param CanBeNeg - может быть отрицательным
     * @return введённое с клавиатуры число типа int
     */
    public static int inputInt(String title, boolean CanBeZero, boolean CanBeNeg) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(title);
        while (true) {
            try {
                int n = scanner.nextInt();
                if (n == 0) {
                    if (CanBeZero)
                        return n;
                    else
                        System.out.println("Введите корректное значение (не нуль)!");
                }
                else
                if (n<0) {
                    if (CanBeNeg)
                        return n;
                    else
                        System.out.println("Введите корректное значение (не отрицательное)!");
                }
                else {
                    return n;
                }
            } catch(InputMismatchException ex) {
                System.out.println("Введите корректное значение!");
                scanner.next();//Java 17
            }
        }
    }
    /**
     * @param title подсказа для ввода
     * @param CanBeZero - может принимать значение нуля
     * @param CanBeNeg - может быть отрицаиельным
     * @return введённое с калвиатуры число типа long
     */
    public static long inputLong(String title, boolean CanBeZero, boolean CanBeNeg) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(title);
        while (true) {
            try {
                long n = scanner.nextLong();
                if (n == 0) {                  if (CanBeZero)
                    return n;
                else
                    System.out.println("Введите корректное значение (не нуль)!");
                }
                else
                if (n<0) {
                    if (CanBeNeg)
                        return n;
                    else
                        System.out.println("Введите корректное значение (не отрицательное)!");
                }
                else {
                    return n;
                }
            } catch(InputMismatchException ex) {
                System.out.println("Введите корректное значение!");
                scanner.next();//Java 17
            }
        }
    }
    /**
     * @param title подсказа для ввода
     * @param CanBeZero - может принимать значение нуля
     * @return введённое с калвиатуры число типа float
     */
    public static float inputFloat(String title, boolean CanBeZero) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(title);
        while (true) {
            try {
                float n = scanner.nextFloat();
                if (CanBeZero || n != 0)
                    return n;
                else
                    System.out.println("Введите корректное значение!-");
            } catch(InputMismatchException e) {
                System.out.println("Введите корректное значение!");
                scanner.next();//Java 17
            }
        }
    }
    /**
     * @param title подсказа для ввода
     * @param CanBeZero - может принимать значение нуля
     * @return введённое с калвиатуры число типа double
     */
    public static double inputDouble(String title, boolean CanBeZero) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(title);
        while (true) {
            try {
                double n = scanner.nextDouble();
                if (CanBeZero || n != 0)
                    return n;
                else
                    System.out.println("Введите корректное значение!-");
            } catch(InputMismatchException e) {
                System.out.println("Введите корректное значение!");
                scanner.next();//Java 17
            }
        }
    }
    /**
     * @param title подсказа для ввода
     * @param CanBeZero строка может быть пустой
     * @return введённое с калвиатуры число типа String
     */
    public static String inputString(String title, boolean CanBeZero) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(title);
        while (true) {
            try {
                String str = scanner.nextLine();
                if (CanBeZero || !str.isEmpty())
                    return str;
                else
                    System.out.println("Введите корректное значение!-");
            } catch(InputMismatchException e) {
                System.out.println("Введите корректное значение!");
                scanner.next();//Java 17
            }
        }
    }
    /**
     * @param title подсказа для ввода
     * @return введённый с клавиатуры цвет типа Color
     */
    public static Color inputColor(String title) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(title);
        while (true) {
            try {
                String str = scanner.nextLine();
                if (!str.isEmpty())
                    return Color.valueOf(str);
                else
                    System.out.println("Введите корректное значение!-");
            } catch(IllegalArgumentException e) {//InputMismatchException
                System.out.println("Введите корректное значение!");
                scanner.next();//Java 17
            }
        }
    }
}
