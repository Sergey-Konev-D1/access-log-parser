import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Введите первое число: ");
        int numb1 = new Scanner(System.in).nextInt();
        System.out.println("Введите второе число: ");
        int numb2 = new Scanner(System.in).nextInt();

        System.out.println("Сумма: " + (numb1+numb2));
        System.out.println("Разность: " + (numb1-numb2));
        System.out.println("Произведение: " + (numb1*numb2));
        System.out.println("Частное: " + (double)(numb1/numb2));


    }
}
