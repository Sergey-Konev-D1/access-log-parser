import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int counter = 0;

        while (true) {
            System.out.println("Введите путь к файлу:");
            String path = new Scanner(System.in).nextLine();

            File file = new File(path);
            boolean fileExists = file.exists();
            boolean isDirectory = file.isDirectory();

            if(!fileExists || isDirectory){
                if(!fileExists){
                    System.out.println("Указанный файл не существует");
                }
                else{
                    System.out.println("Указанный путь является путём к папке");
                }
                continue;
            }
            System.out.println("Путь указан верно");
            counter++;
            System.out.println("Это файл номер " + counter);

            try {
                FileReader fileReader = new FileReader(path);
                BufferedReader reader =
                        new BufferedReader(fileReader);
                int countLine = 0;
                int maxLenght = 0;
                int minLenght = Integer.MAX_VALUE;
                String line;
                while ((line = reader.readLine()) != null) {
                    int length = line.length();
                    if (length > 1024){
                        throw new LineTolongExeption("встретилась строка длиннее 1024 символов");
                    }
                    if(length > maxLenght){
                        maxLenght = length;
                    }
                    if (length < minLenght){
                        minLenght = length;
                    }
                    countLine += 1;

                }
                if(countLine == 0){
                    minLenght = 0;
                }
                System.out.println("Количество строк в файле: " + countLine);
                System.out.println("Максимальная длина строки: " + maxLenght);
                System.out.println("Минимальная длина строки: " + minLenght);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }

    static class LineTolongExeption extends RuntimeException{
        public LineTolongExeption(String str){
            super(str);
        }
    }
}
