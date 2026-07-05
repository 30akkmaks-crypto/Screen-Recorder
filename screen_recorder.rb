#!/usr/bin/env ruby
# screen_recorder.rb
# encoding: UTF-8

require 'optparse'

def check_ffmpeg
  system('ffmpeg -version > /dev/null 2>&1')
end

def get_ffmpeg_cmd(output, duration)
  case RbConfig::CONFIG['host_os']
  when /mswin|mingw|cygwin/
    "ffmpeg -y -f gdigrab -i desktop -t #{duration} -c:v libx264 -pix_fmt yuv420p #{output}"
  when /darwin/
    "ffmpeg -y -f avfoundation -i 1 -t #{duration} -c:v libx264 -pix_fmt yuv420p #{output}"
  else
    "ffmpeg -y -f x11grab -i :0.0 -t #{duration} -c:v libx264 -pix_fmt yuv420p #{output}"
  end
end

options = { output: 'screen_recording.mp4', duration: 10 }
OptionParser.new do |opts|
  opts.banner = "Usage: screen_recorder.rb [options]"
  opts.on('-o', '--output FILE', 'Выходной файл') { |v| options[:output] = v }
  opts.on('-t', '--time SEC', Integer, 'Длительность записи (сек)') { |v| options[:duration] = v }
  opts.on('-h', '--help', 'Справка') { puts opts; exit }
end.parse!

unless check_ffmpeg
  STDERR.puts 'Ошибка: FFmpeg не найден. Установите FFmpeg и добавьте в PATH.'
  exit 1
end

cmd = get_ffmpeg_cmd(options[:output], options[:duration])
puts "Начинаем запись на #{options[:duration]} секунд в #{options[:output]}..."
system(cmd)
if $?.success?
  puts "✅ Запись завершена. Файл: #{options[:output]}"
else
  STDERR.puts "❌ Ошибка записи: код #{$?.exitstatus}"
  exit 1
end
