import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
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

            Statistics stat = new Statistics();



            try {
                FileReader fileReader = new FileReader(path);
                BufferedReader reader =
                        new BufferedReader(fileReader);
                int countLine = 0;
                int googlebot = 0;
                int yandexbot = 0;
                String line;
                while ((line = reader.readLine()) != null) {
                    int length = line.length();
                    if (length > 1024){
                        throw new LineTolongExeption("встретилась строка длиннее 1024 символов");
                    }
                    countLine += 1;

                    LogEntry entry = new LogEntry(line);
                    stat.addEntry(entry);

                    String userAgent = searhUserAgent(line);
                    if("Googlebot".equals(userAgent)){
                        googlebot += 1;
                    }else if ("YandexBot".equals(userAgent)){
                        yandexbot += 1;
                    }
                }

                HashMap<String, Double> result = stat.getOsStatistics();

                System.out.println("Количество строк в файле: " + countLine);
                System.out.println("Googlebot: " + googlebot);
                System.out.println("YandexBot: " + yandexbot);

                double reqGooglebot = (double) googlebot/countLine * 100;
                double reqYandexBot = (double)yandexbot/countLine * 100;

                System.out.println("доля запросов Googlebot: " + reqGooglebot);
                System.out.println("доля запросов YandexBot: " + reqYandexBot);
                System.out.println("Статистика: ");
                System.out.println("Средний трафик за час: " + stat.getTrafficRate());

                ArrayList<String> osNames = new ArrayList<>(result.keySet());
                for (int i = 0; i < osNames.size(); i++) {
                    String osName = osNames.get(i);
                    double fraction = result.get(osName);
                    System.out.println(osName + ": " + fraction * 100  + "%");
                }


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

    public static String searhUserAgent(String line){
        // Разбиваю строку по двойным кавычкам и берем последний элемент, сохраняю как новую строку
        String[] splitLineIteration1 = line.split("\"");
        String userAgentPart = splitLineIteration1[splitLineIteration1.length - 1];

        // Разбиваю полученную часть по скобкам и берем последний элемент. Здесь получаю значение строки в которой содержится юзер агент
        String[] splitLineIteration2 = userAgentPart.split("[()]");
        String userAgentInfo = splitLineIteration2[splitLineIteration2.length -1];

        // Разбиваю по точкам с запятой для извлечения информации о боте
        String[] splitLineIteration3 = userAgentInfo.split(";");

        if (splitLineIteration3.length < 2) {
            return ""; // возвращаю пустую строку, если количество элементов в последнем отсплитованном массиве  < 2
        }

        // получаю значение бота google или yandex (тут могут быть и другие значения, позже в основной функции проверяю через сравнение). Избавляюсь от слеша и версии бота
        String botInfo = splitLineIteration3[1];
        String[] splitLineIteration4 = botInfo.split("/");

        return splitLineIteration4[0].trim(); // зачищаю пробелы и возвращаю строку с названием бота
    }
}
