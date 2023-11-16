package de.kevrecraft.wirtschaftssystem.utilitys;

public class Utility {
    public static boolean isInt(String string) {
        for (char c : string.toCharArray()) {
            if (!isInt(c))
                return false;
        }
        return true;
    }

    public static boolean isInt(char c) {
        switch (c) {
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                return true;
        }
        return false;
    }
}
