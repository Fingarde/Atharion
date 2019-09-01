package fr.fingarde.atharion.utils;

public class TimestampConverter
{
    public static String getTime(long time)
    {

        String toReturn = "";

        long diffSeconds = time / 1000 % 60;
        long diffMinutes = time / (60 * 1000) % 60;
        long diffHours = time / (60 * 60 * 1000) % 24;
        long diffDays = time / (24 * 60 * 60 * 1000);

        if (diffDays != 0) toReturn += diffDays + ((diffDays == 1) ? "jour " : "jours ");
        if (diffHours != 0) toReturn += diffHours + ((diffHours == 1) ? "heure " : "heures ");
        if (diffMinutes != 0) toReturn += diffMinutes + ((diffMinutes == 1) ? "minute " : "minutes ");
        if (diffSeconds != 0) toReturn += diffSeconds + ((diffSeconds == 1) ? "seconde " : "secondes ");

        return toReturn;
    }
}
