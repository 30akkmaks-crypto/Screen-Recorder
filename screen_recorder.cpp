// screen_recorder.cpp
#include <iostream>
#include <cstdlib>
#include <string>
#include <sstream>
#include <chrono>
#include <thread>

#ifdef _WIN32
#include <windows.h>
#else
#include <unistd.h>
#endif

using namespace std;

bool checkFFmpeg() {
    int ret = system("ffmpeg -version > nul 2>&1");
    return ret == 0;
}

string getFFmpegCmd(const string& output, int duration) {
#ifdef _WIN32
    return "ffmpeg -y -f gdigrab -i desktop -t " + to_string(duration) + " -c:v libx264 -pix_fmt yuv420p " + output;
#elif __APPLE__
    return "ffmpeg -y -f avfoundation -i 1 -t " + to_string(duration) + " -c:v libx264 -pix_fmt yuv420p " + output;
#else
    return "ffmpeg -y -f x11grab -i :0.0 -t " + to_string(duration) + " -c:v libx264 -pix_fmt yuv420p " + output;
#endif
}

int main(int argc, char* argv[]) {
    string output = "screen_recording.mp4";
    int duration = 10;
    for (int i = 1; i < argc; ++i) {
        string arg = argv[i];
        if (arg == "-o" && i+1 < argc) {
            output = argv[++i];
        } else if (arg == "-t" && i+1 < argc) {
            duration = stoi(argv[++i]);
        } else if (arg == "-h" || arg == "--help") {
            cout << "Usage: screen_recorder [-o output] [-t seconds]" << endl;
            return 0;
        }
    }
    if (!checkFFmpeg()) {
        cerr << "Ошибка: FFmpeg не найден. Установите FFmpeg и добавьте в PATH." << endl;
        return 1;
    }
    string cmd = getFFmpegCmd(output, duration);
    cout << "Начинаем запись на " << duration << " секунд в " << output << "..." << endl;
    int ret = system(cmd.c_str());
    if (ret != 0) {
        cerr << "❌ Ошибка записи: код " << ret << endl;
        return 1;
    }
    cout << "✅ Запись завершена. Файл: " << output << endl;
    return 0;
}
