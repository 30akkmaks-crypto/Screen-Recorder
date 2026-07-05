// screen_recorder.cs
using System;
using System.Diagnostics;

class ScreenRecorder
{
    static bool CheckFFmpeg()
    {
        try
        {
            Process.Start("ffmpeg", "-version")?.WaitForExit();
            return true;
        }
        catch
        {
            return false;
        }
    }

    static string GetFFmpegCmd(string output, int duration)
    {
        if (Environment.OSVersion.Platform == PlatformID.Win32NT)
            return $"ffmpeg -y -f gdigrab -i desktop -t {duration} -c:v libx264 -pix_fmt yuv420p {output}";
        else if (Environment.OSVersion.Platform == PlatformID.MacOSX)
            return $"ffmpeg -y -f avfoundation -i 1 -t {duration} -c:v libx264 -pix_fmt yuv420p {output}";
        else
            return $"ffmpeg -y -f x11grab -i :0.0 -t {duration} -c:v libx264 -pix_fmt yuv420p {output}";
    }

    static void Main(string[] args)
    {
        string output = "screen_recording.mp4";
        int duration = 10;
        for (int i = 0; i < args.Length; i++)
        {
            if (args[i] == "-o" && i+1 < args.Length)
                output = args[++i];
            else if (args[i] == "-t" && i+1 < args.Length)
                duration = int.Parse(args[++i]);
            else if (args[i] == "-h" || args[i] == "--help")
            {
                Console.WriteLine("Usage: screen_recorder [-o output] [-t seconds]");
                return;
            }
        }
        if (!CheckFFmpeg())
        {
            Console.Error.WriteLine("Ошибка: FFmpeg не найден. Установите FFmpeg и добавьте в PATH.");
            Environment.Exit(1);
        }
        string cmd = GetFFmpegCmd(output, duration);
        Console.WriteLine($"Начинаем запись на {duration} секунд в {output}...");
        var process = Process.Start("cmd.exe", "/c " + cmd);
        process.WaitForExit();
        if (process.ExitCode != 0)
        {
            Console.Error.WriteLine($"❌ Ошибка записи: код {process.ExitCode}");
            Environment.Exit(1);
        }
        Console.WriteLine($"✅ Запись завершена. Файл: {output}");
    }
}
