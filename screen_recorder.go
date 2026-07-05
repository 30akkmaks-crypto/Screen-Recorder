// screen_recorder.go
package main

import (
	"flag"
	"fmt"
	"os"
	"os/exec"
	"runtime"
	"strconv"
	"time"
)

func checkFFmpeg() bool {
	cmd := exec.Command("ffmpeg", "-version")
	err := cmd.Run()
	return err == nil
}

func getFFmpegCmd(output string, duration int) []string {
	system := runtime.GOOS
	if system == "windows" {
		return []string{"ffmpeg", "-y", "-f", "gdigrab", "-i", "desktop", "-t", strconv.Itoa(duration), "-c:v", "libx264", "-pix_fmt", "yuv420p", output}
	} else if system == "darwin" {
		return []string{"ffmpeg", "-y", "-f", "avfoundation", "-i", "1", "-t", strconv.Itoa(duration), "-c:v", "libx264", "-pix_fmt", "yuv420p", output}
	} else {
		return []string{"ffmpeg", "-y", "-f", "x11grab", "-i", ":0.0", "-t", strconv.Itoa(duration), "-c:v", "libx264", "-pix_fmt", "yuv420p", output}
	}
}

func main() {
	var output string
	var duration int
	flag.StringVar(&output, "o", "screen_recording.mp4", "Выходной файл")
	flag.IntVar(&duration, "t", 10, "Длительность записи (сек)")
	flag.Parse()

	if !checkFFmpeg() {
		fmt.Fprintf(os.Stderr, "Ошибка: FFmpeg не найден. Установите FFmpeg и добавьте в PATH.\n")
		os.Exit(1)
	}

	cmdArgs := getFFmpegCmd(output, duration)
	fmt.Printf("Начинаем запись на %d секунд в %s...\n", duration, output)
	cmd := exec.Command(cmdArgs[0], cmdArgs[1:]...)
	cmd.Stdout = os.Stdout
	cmd.Stderr = os.Stderr
	err := cmd.Run()
	if err != nil {
		fmt.Fprintf(os.Stderr, "❌ Ошибка записи: %v\n", err)
		os.Exit(1)
	}
	fmt.Printf("✅ Запись завершена. Файл: %s\n", output)
}
