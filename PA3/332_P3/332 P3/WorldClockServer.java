import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

// Jaimen Hovender (u20464348) & Priyul Mahabeer (u20421169)

public class WorldClockServer {
    private static final int PORT = 54321;
    private static final Map<String, String> CITY_TIMEZONES = new HashMap<>();

    static {
        CITY_TIMEZONES.put("New York", "America/New_York");
        CITY_TIMEZONES.put("London", "Europe/London");
        CITY_TIMEZONES.put("Tokyo", "Asia/Tokyo");
        CITY_TIMEZONES.put("Sydney", "Australia/Sydney");
    }

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/", new WorldClockHandler());
        server.start();
        System.out.println("World Clock Server started on port " + PORT);
    }

    static class WorldClockHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String requestedCity = exchange.getRequestURI().getQuery();
            String htmlContent = generateHtmlContent(requestedCity);
            exchange.sendResponseHeaders(200, htmlContent.length());
            OutputStream os = exchange.getResponseBody();
            os.write(htmlContent.getBytes());
            os.close();
        }

        private String generateHtmlContent(String requestedCity) {
            StringBuilder html = new StringBuilder();
            html.append("<!DOCTYPE html>")
                .append("<html>")
                .append("<head>")
                .append("<meta charset=\"UTF-8\">")
                .append("<meta http-equiv=\"REFRESH\" content=\"1\">")
                .append("<title>World Clock</title>")
                .append("</head>")
                .append("<body>");

            LocalDateTime southAfricaTime = LocalDateTime.now(ZoneId.of("Africa/Johannesburg"));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

            html.append("<h2>South Africa Time: ").append(southAfricaTime.format(formatter)).append("</h2>");

            if (requestedCity != null && CITY_TIMEZONES.containsKey(requestedCity)) {
                ZoneId cityTimeZone = ZoneId.of(CITY_TIMEZONES.get(requestedCity));
                LocalDateTime cityTime = LocalDateTime.now(cityTimeZone);
                html.append("<h2>")
                    .append(requestedCity)
                    .append(" Time: ")
                    .append(cityTime.format(formatter))
                    .append("</h2>");
            }

            html.append("<h3>World Cities:</h3>");
            for (String city : CITY_TIMEZONES.keySet()) {
                html.append("<a href=\"?").append(city).append("\">").append(city).append("</a><br>");
            }

            html.append("</body>")
                .append("</html>");

            return html.toString();
        }
    }
}
