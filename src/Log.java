import java.io.FileWriter;
import java.io.IOException;

public class Log {
    public static void write(String message, String logPath) {
        writeLog(message, logPath);
    }

    private static void writeLog(String message, String logPath) {
        try (FileWriter fw = new FileWriter(logPath, true)) {
            fw.write(message + "\n");
        } catch (IOException e) {
            writeLog("" + e, logPath);
        }
    }

    public static void clearLog(String logPath) {
        try (FileWriter fw = new FileWriter(logPath, false)) {
            fw.write("");
        } catch (IOException e) {
            writeLog("" + e, logPath);
        }
    }
}
