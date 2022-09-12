# Расписание поездов

https://javarush.ru/groups/posts/2368-pattern-proektirovanija-proxy

**Теперь пойдем по шагам внедрения нашего паттерна:**

- Определить интерфейс, который позволяет использовать вместо оригинального объекта новый заместитель. В нашем примере
  это `TimetableTrains`.

- Создать класс заместителя. В нем должна быть ссылка на сервисный объект (создать в классе или передать в конструкторе)
  ;

**Вот наш класс-заместитель:**

```java
public class TimetableElectricTrainsProxy implements TimetableTrains {
    // Ссылка на оригинальный объект
    private TimetableTrains timetableTrains = new TimetableElectricTrains();

    private String[] timetableCache = null

    @Override
    public String[] getTimetable() {
        return timetableTrains.getTimetable();
    }

    @Override
    public String getTrainDepartureTime(String trainId) {
        return timetableTrains.getTrainDepartureTime(trainId);
    }

    public void clearCache() {
        timetableTrains = null;
    }
}

```

На этом этапе просто создаем класс со ссылкой на оригинальный объект и передаем все вызовы ему.

- Реализовываем логику класса-заместителя. В основном вызов всегда перенаправляется оригинальному объекту.

```java
public class TimetableElectricTrainsProxy implements TimetableTrains { // Ссылка на оригинальный объект private
    TimetableTrains timetableTrains = new TimetableElectricTrains();

    private String[] timetableCache = null;

    @Override
    public String[] getTimetable() {
        if (timetableCache == null) {
            timetableCache = timetableTrains.getTimetable();
        }
        return timetableCache;
    }

    @Override
    public String getTrainDepartureTime(String trainId) {
        if (timetableCache == null) {
            timetableCache =
                    timetableTrains.getTimetable();
        }
        for (int i = 0; i < timetableCache.length; i++) {
            if (timetableCache[i].startsWith(
                    trainId + ";")) return timetableCache[i];
        }
        return "";
    }

    public void clearCache() {
        timetableTrains = null;
    }
} 
```

Метод `getTimetable()` проверяет, закэширован ли массив расписания в память. Если нет, он посылает запрос для загрузки
данных с диска, сохраняя результат. Если же запрос уже выполняется, он быстро вернет объект из памяти.

Благодаря простому функционалу, метод `getTrainDepartireTime()` не пришлось перенаправлять в оригинальный объект. Мы
просто дублировали его функционал в новый метод.

Так делать нельзя. Если пришлось дублировать код или производить подобные манипуляции, значит что-то пошло не так, и
нужно посмотреть на проблему под другим углом. В нашем простом примере иного пути нет, но в реальных проектах, скорее
всего, код будет написан более корректно.

- Заменить в клиентском коде создание оригинального объекта на объект-заместитель:

```java
public class DisplayTimetable { // Измененная ссылка private TimetableTrains timetableTrains = new
    TimetableElectricTrainsProxy();

    public void printTimetable() {
        String[] timetable = timetableTrains.getTimetable();
        String[] tmpArr;
        System.out.println("Поезд\tОткуда\tКуда\t\tВремя отправления\tВремя прибытия\tВремя в пути");
        for (int i = 0; i <
                timetable.length; i++) {
            tmpArr = timetable[i].split(";");
            System.out.printf("%s\t%s\t%s\t\t%s\t\t\t\t%s\t\t\t%s\n",
                    tmpArr[0], tmpArr[1], tmpArr[2], tmpArr[3], tmpArr[4], tmpArr[5]);
        }
    }
} 
```

**Проверка**

```
Поезд     Откуда     Куда     Время отправления    Время прибытия     Время в пути
9B-6854   Лондон     Прага    13:43                21:15              07:32
BA-1404   Париж      Грац     14:25                21:25              07:00
9B-8710   Прага      Вена     04:48                08:49              04:01
9B-8122   Прага      Грац     04:48                08:49              04:01

```

Отлично, работает корректно.

Можно также рассмотреть вариант с фабрикой, которая будет создавать как оригинальный объект, так и объект-заместитель в
зависимости от определенных условий.