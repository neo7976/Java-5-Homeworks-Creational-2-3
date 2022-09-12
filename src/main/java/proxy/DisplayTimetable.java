package proxy;

//3 Клиентский класс
public class DisplayTimetable {
//    private TimetableTrains timetableTrains = new TimetableElectricTrains(); // Когда не было заместителя
    private final TimetableTrains timetableTrains = new TimetableElectricTrainsProxy(); //Когда добавили заместитель

    public void printTimetable() {
        String[] timetable = timetableTrains.getTimetable();
        String[] tmpArr;
        System.out.println("Поезд\tОткуда\tКуда\t\tВремя отправления\tВремя прибытия\tВремя в пути");
        for (int i = 0; i < timetable.length; i++) {
            tmpArr = timetable[i].split(";");
            System.out.printf("%s\t%s\t%s\t\t%s\t\t\t\t%s\t\t\t%s\n", tmpArr[0], tmpArr[1], tmpArr[2], tmpArr[3], tmpArr[4], tmpArr[5]);
        }
    }

}
