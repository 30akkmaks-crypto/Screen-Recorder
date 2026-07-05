.

🎥 Screen Recorder – Простая запись экрана на 7 языках
Легкая утилита для записи экрана через командную строку с использованием FFmpeg.
Поддерживает установку длительности, выбор выходного файла и (опционально) области захвата.
Реализована на 7 языках программирования с единым интерфейсом и кросс-платформенностью.

🚀 Возможности
Запись всего экрана – просто и быстро.

Настройка длительности – установите время записи в секундах.

Выбор выходного файла – укажите имя и формат (по умолчанию .mp4).

Кроссплатформенность – работает на Windows, Linux, macOS (требуется FFmpeg).

Минимальные зависимости – только FFmpeg и интерпретатор языка.

📖 Использование
Синтаксис (единый для всех версий):

bash
<команда> [опции]
Опции
Опция	Описание
-o, --output <файл>	Имя выходного файла (по умолчанию screen_recording.mp4)
-t, --time <сек>	Длительность записи в секундах (по умолчанию 10)
-h, --help	Справка
Примеры
bash
# Записать экран на 10 секунд в screen_recording.mp4
python screen_recorder.py

# Записать на 30 секунд в my_video.mp4
python screen_recorder.py -t 30 -o my_video.mp4
🛠 Установка и запуск
Общие требования
Установленный FFmpeg (должен быть доступен в PATH).

Интерпретатор/компилятор соответствующего языка.

Python
bash
python screen_recorder.py [опции]
Go
bash
go build screen_recorder.go
./screen_recorder [опции]
JavaScript (Node.js)
bash
node screen_recorder.js [опции]
C++
bash
g++ -std=c++11 screen_recorder.cpp -o screen_recorder
./screen_recorder [опции]
C#
bash
csc screen_recorder.cs
mono screen_recorder.exe [опции]   # или dotnet run
Ruby
bash
ruby screen_recorder.rb [опции]
Java
bash
javac screen_recorder.java
java screen_recorder [опции]
🧠 Логика работы
Программа формирует команду FFmpeg с учётом операционной системы:

Windows: -f gdigrab -i desktop

Linux: -f x11grab -i :0.0

macOS: -f avfoundation -i "1" (или "0" для веб-камеры, но для экрана используется 1)

Затем запускает процесс записи с указанной длительностью.

✨ Дополнительные фичи
Автоматическое определение ОС – не нужно вручную указывать параметры.

Цветной вывод – сообщения об успехе/ошибке выделены.

Проверка наличия FFmpeg – предупреждение при отсутствии.

📂 Состав репозитория
Язык	Файл	Статус
Python	screen_recorder.py	✅
Go	screen_recorder.go	✅
JavaScript	screen_recorder.js	✅
C++	screen_recorder.cpp	✅
C#	screen_recorder.cs	✅
Ruby	screen_recorder.rb	✅
Java	screen_recorder.java	✅
🤝 Вклад в проект
Приветствуются улучшения:

Добавление поддержки региона захвата.

Графический интерфейс.

Запись с микрофоном.

Создавайте Issues и Pull Requests.

📜 Лицензия
MIT License – свободное использование, модификация и распространение.

📂 Исходный код
Первая строка каждого файла – его имя. Скопируйте блок целиком и сохраните в
