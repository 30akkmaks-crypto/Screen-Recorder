// screen_recorder.java
import java.io.*;
import java.util.*;

public class screen_recorder {
    private static boolean checkFFmpeg() {
        try {
            ProcessBuilder pb = new ProcessBuilder("ffmpeg", "-version");
            pb.redirectErrorStream(true);
            Process p = pb.start();
            p.waitFor();
            return p.exitValue() == 0;
        } catch (Exception e) {
            return false;
        }
    }

    private static String getFFmpegCmd(String output, int duration) {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            return "ffmpeg -y -f gdigrab -i desktop -t " + duration + " -c:v libx264 -pix_fmt yuv420p " + output;
        } else if (os.contains("mac")) {
            return "ffmpeg -y -f avfoundation -i 1 -t " + duration + " -c:v libx264 -pix_fmt yuv420p " + output;
        } else {
            return "ffmpeg -y -f x11grab -i :0.0 -t " + duration + " -c:v libx264 -pix_fmt yuv420p " + output;
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        String output = "screen_recording.mp4";
        int duration = 10;
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-o") && i+1 < args.length) {
                output = args[++i];
            } else if (args[i].equals("-t") && i+1 < args.length) {
                duration = Integer.parseInt(args[++i]);
            } else if (args[i].equals("-h") || args[i].equals("--help")) {
                System.out.println("Usage: java screen_recorder [-o output] [-t seconds]");
                return;
            }
        }
        if (!checkFFmpeg()) {
            System.err.println("Ошибка: FFmpeg не найден. Установите FFmpeg и добавьте в PATH.");
            System.exit(1);
        }
        String cmd = getFFmpegCmd(output, duration);
        System.out.println("Начинаем запись на " + duration + " секунд в " + output + "...");
        ProcessBuilder pb = new ProcessBuilder(cmd.split(" "));
        pb.redirectErrorStream(true);
        pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        Process p = pb.start();
        int code = p.waitFor();
        if (code != 0) {
            System.err.println("❌ Ошибка записи: код " + code);
            System.exit(1);
        }
        System.out.println("✅ Запись завершена. Файл: " + output);
    }
}
