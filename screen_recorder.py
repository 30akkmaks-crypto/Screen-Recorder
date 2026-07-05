# screen_recorder.py
#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import subprocess
import sys
import argparse
import platform
import os
import time

def check_ffmpeg():
    try:
        subprocess.run(["ffmpeg", "-version"], stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL)
        return True
    except FileNotFoundError:
        return False

def get_ffmpeg_cmd(output, duration):
    system = platform.system()
    if system == "Windows":
        cmd = ["ffmpeg", "-y", "-f", "gdigrab", "-i", "desktop", "-t", str(duration), "-c:v", "libx264", "-pix_fmt", "yuv420p", output]
    elif system == "Darwin":
        cmd = ["ffmpeg", "-y", "-f", "avfoundation", "-i", "1", "-t", str(duration), "-c:v", "libx264", "-pix_fmt", "yuv420p", output]
    else:  # Linux
        cmd = ["ffmpeg", "-y", "-f", "x11grab", "-i", ":0.0", "-t", str(duration), "-c:v", "libx264", "-pix_fmt", "yuv420p", output]
    return cmd

def main():
    parser = argparse.ArgumentParser(description="Простая запись экрана")
    parser.add_argument('-o', '--output', default='screen_recording.mp4', help='Выходной файл')
    parser.add_argument('-t', '--time', type=int, default=10, help='Длительность записи (сек)')
    args = parser.parse_args()

    if not check_ffmpeg():
        print("Ошибка: FFmpeg не найден. Установите FFmpeg и добавьте в PATH.", file=sys.stderr)
        sys.exit(1)

    cmd = get_ffmpeg_cmd(args.output, args.time)
    print(f"Начинаем запись на {args.time} секунд в {args.output}...")
    try:
        subprocess.run(cmd, check=True)
        print(f"✅ Запись завершена. Файл: {args.output}")
    except subprocess.CalledProcessError as e:
        print(f"❌ Ошибка записи: {e}", file=sys.stderr)
        sys.exit(1)

if __name__ == "__main__":
    try:
        main()
    except KeyboardInterrupt:
        print("\nЗапись прервана пользователем.")
        sys.exit(0)
