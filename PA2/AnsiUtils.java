public class AnsiUtils {

    public enum Color {
        BLACK(0),
        RED(1),
        GREEN(2),
        YELLOW(3),
        BLUE(4),
        MAGENTA(5),
        CYAN(6),
        WHITE(7),
        RESET(9);

        private final int value;

        Color(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public static String color(Color color, String text) {
        return "\u001B[3" + color.getValue() + "m" + text + "\u001B[3" + Color.RESET.getValue() + "m";
    }

    public static String moveTo(int row, int col) {
        return "\u001B[" + row + ";" + col + "H";
    }

    public static String clearScreen() {
        return "\u001B[2J";
    }
}
