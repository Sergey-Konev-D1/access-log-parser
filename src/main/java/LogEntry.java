import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

enum HttpMethods{
    GET, POST, PUT, DELETE, HEAD, OPTIONS, TRACE, CONNECT
}

public class LogEntry {
    private final String ipAddress;
    private final LocalDateTime dateTime;
    private final HttpMethods method;
    private final String path;
    private final int responseCode;
    private final int responseSize;
    private final String referer;
    private final UserAgent userAgent;

    public LogEntry(String line) {
        String[] splitItr = line.split("\"");

        //Получаю IP адрес, разделив по пробелу IP адрес и время
        String firstPart = splitItr[0];
        this.ipAddress = firstPart.split( " ")[0];

        // Определяю где в строке находится участок со временем и привожу строку к типу LocalDateTime
        int start = firstPart.indexOf("[") + 1;
        int end = firstPart.indexOf("]");
        String dataTime = firstPart.substring(start,end);
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH);
        long sec = java.time.ZonedDateTime.parse(dataTime,format).toEpochSecond();
        this.dateTime = LocalDateTime.ofEpochSecond(sec,0, ZoneOffset.UTC);

        //Определяю метод из строки лога
        String[] reqPart = splitItr[1].split(" ");
        HttpMethods tmpMethod = HttpMethods.GET;
        if(reqPart[0].equals("POST")){
            tmpMethod = HttpMethods.POST;
        } else if (reqPart[0].equals("PUT")) {
            tmpMethod = HttpMethods.PUT;
        } else if (reqPart[0].equals("DELETE")) {
            tmpMethod = HttpMethods.DELETE;
        } else if (reqPart[0].equals("HEAD")) {
            tmpMethod = HttpMethods.HEAD;
        } else if (reqPart[0].equals("OPTIONS")) {
            tmpMethod = HttpMethods.OPTIONS;
        } else if (reqPart[0].equals("TRACE")) {
            tmpMethod = HttpMethods.TRACE;
        } else if (reqPart[0].equals("CONNECT")) {
            tmpMethod = HttpMethods.CONNECT;
        }
        this.method  = tmpMethod;

        // Забираю путь запроса
        this.path = reqPart[1];

        // Забираю код ответа и размер ответа
        String[] responseCodeAndSize = splitItr[2].trim().split(" ");
        this.responseCode = Integer.parseInt(responseCodeAndSize[0]);
        this.responseSize = Integer.parseInt(responseCodeAndSize[1]);

        //referer
        String refererPart = splitItr[3];
        if(refererPart == null || refererPart.isEmpty() || refererPart.equals("-")){
            this.referer = "Данные отсутствуют";
        } else {
            this.referer = refererPart;
        }

        //Получаю User-Agent и отпавляю чтобы вычленить операционную систему и браузер
        this.userAgent = new UserAgent(splitItr[5].trim());
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getPath() {
        return path;
    }

    public HttpMethods getMethod() {
        return method;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public int getResponseSize() {
        return responseSize;
    }

    public String getReferer() {
        return referer;
    }

    public UserAgent getUserAgent() {
        return userAgent;
    }
}





