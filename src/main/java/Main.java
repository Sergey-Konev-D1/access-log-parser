import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Введите первое число: ");
        int numb1 = new Scanner(System.in).nextInt();

        System.out.println("Введите второе число: ");
        int numb2 = new Scanner(System.in).nextInt();

        int sum = numb1+numb2;
        System.out.println("Сумма: " + sum);

        int dif = numb1-numb2;
        System.out.println("Разность: " + dif);

        int prod =  numb1*numb2;
        System.out.println("Произведение: " + prod);

        double quot = (double)numb1/numb2;
        System.out.println("Частное: " + quot);

    }
}
