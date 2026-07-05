// screen_recorder.js
#!/usr/bin/env node
'use strict';

const { exec, execSync } = require('child_process');
const os = require('os');

function checkFFmpeg() {
    try {
        execSync('ffmpeg -version', { stdio: 'ignore' });
        return true;
    } catch (e) {
        return false;
    }
}

function getFFmpegCmd(output, duration) {
    const platform = os.platform();
    if (platform === 'win32') {
        return ['ffmpeg', '-y', '-f', 'gdigrab', '-i', 'desktop', '-t', String(duration), '-c:v', 'libx264', '-pix_fmt', 'yuv420p', output];
    } else if (platform === 'darwin') {
        return ['ffmpeg', '-y', '-f', 'avfoundation', '-i', '1', '-t', String(duration), '-c:v', 'libx264', '-pix_fmt', 'yuv420p', output];
    } else {
        return ['ffmpeg', '-y', '-f', 'x11grab', '-i', ':0.0', '-t', String(duration), '-c:v', 'libx264', '-pix_fmt', 'yuv420p', output];
    }
}

function main() {
    const args = process.argv.slice(2);
    let output = 'screen_recording.mp4';
    let duration = 10;
    for (let i = 0; i < args.length; i++) {
        if (args[i] === '-o' && i+1 < args.length) {
            output = args[++i];
        } else if (args[i] === '-t' && i+1 < args.length) {
            duration = parseInt(args[++i], 10);
        } else if (args[i] === '-h' || args[i] === '--help') {
            console.log('Usage: node screen_recorder.js [-o output] [-t seconds]');
            process.exit(0);
        }
    }
    if (!checkFFmpeg()) {
        console.error('Ошибка: FFmpeg не найден. Установите FFmpeg и добавьте в PATH.');
        process.exit(1);
    }
    const cmd = getFFmpegCmd(output, duration);
    console.log(`Начинаем запись на ${duration} секунд в ${output}...`);
    const proc = exec(cmd.join(' '), (error, stdout, stderr) => {
        if (error) {
            console.error(`❌ Ошибка записи: ${error}`);
            process.exit(1);
        }
        console.log(`✅ Запись завершена. Файл: ${output}`);
    });
    process.on('SIGINT', () => {
        proc.kill('SIGINT');
        console.log('\nЗапись прервана пользователем.');
        process.exit(0);
    });
}

main();
