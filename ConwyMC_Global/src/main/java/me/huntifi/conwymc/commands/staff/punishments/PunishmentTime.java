package me.huntifi.conwymc.commands.staff.punishments;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.sql.Timestamp;
import java.text.DecimalFormat;

/**
 * Shared time functions for punishments
 */
public class PunishmentTime {

    /**
     * Notify a command sender of the correct time format.
     * @param s The command sender to notify
     */
    public static void wrongFormat(CommandSender s) {
        s.sendMessage(
                ChatColor.DARK_RED + "Please supply a duration in the form " + ChatColor.RED + "0t"
                + ChatColor.DARK_RED + ", where " + ChatColor.RED + "0" + ChatColor.DARK_RED
                + " is any positive number and " + ChatColor.RED + "t" + ChatColor.DARK_RED
                + " is one of the following units:\n"
                + ChatColor.RED + "y(ears), M(onths), d(ays), h(ours), m(inutes), s(econds)"
        );
    }

    /**
     * Get the duration in milliseconds.
     * @param duration A string representation of the duration
     * @return The converted duration, 0 if an invalid duration was supplied
     */
    public static long getDuration(String duration) {
        char unit = duration.charAt(duration.length() - 1);
        long num;
        try {
            num = Long.parseLong(duration.substring(0, duration.length() - 1));
            if (num <= 0) {
                return 0;
            }
        } catch (NumberFormatException e) {
            return 0;
        }

        switch (unit) {
            case 'y':
                num *= 12;
            case 'M':
                num *= 30.42;
            case 'd':
                num *= 24;
            case 'h':
                num *= 60;
            case 'm':
                num *= 60;
            case 's':
                num *= 1000;
                break;
            default:
                return 0;
        }
        return num;
    }

    /**
     * Get the duration in string representation.
     * @param duration The duration input string
     * @return The converted duration, empty string if invalid duration (should never happen)
     */
    public static String getExpire(String duration) {
        char unit = duration.charAt(duration.length() - 1);
        String num = duration.substring(0, duration.length() - 1);

        switch (unit) {
            case 'y':
                return num + " year(s)";
            case 'M':
                return num + " month(s)";
            case 'd':
                return num + " day(s)";
            case 'h':
                return num + " hour(s)";
            case 'm':
                return num + " minute(s)";
            case 's':
                return num + " second(s)";
        }
        return "";
    }

    /**
     * Get a string representation of the remaining punishment duration.
     * @param end The end time of the punishment
     * @return The converted duration
     */
    public static String getExpire(Timestamp end) {
        long duration = (end.getTime() - System.currentTimeMillis()) / 1000;
        DecimalFormat df = new DecimalFormat("0");

        if (duration >= 60) {
            duration /= 60;
        } else {
            return df.format(duration) + " second(s)";
        }

        if (duration >= 60) {
            duration /= 60;
        } else {
            return df.format(duration) + " minute(s)";
        }

        if (duration >= 24) {
            duration /= 24;
        } else {
            return df.format(duration) + " hour(s)";
        }

        if (duration >= 30.42) {
            duration /= 30.42;
        } else {
            return df.format(duration) + " day(s)";
        }

        if (duration >= 12) {
            duration /= 12;
        } else {
            return df.format(duration) + " month(s)";
        }

        return df.format(duration) + " year(s)";
    }
}
