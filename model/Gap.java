package com.example.michellenguy3n.studybreak.model;

import java.io.Serializable;

/**
 * This class models the Gap object. Gaps are when a student has free time in their schedule and are defined by their start and end times.
 * 
 * Created by michellenguy3n on 12/7/16.
 */
public class Gap implements Serializable {
    int startHour;
    int startMinute;
    int endHour;
    int endMinute;

    public Gap(int _startHour, int _startMinute, int _endHour, int _endMinute) {
        startHour = _startHour;
        startMinute = _startMinute;
        endHour = _endHour;
        endMinute = _endMinute;

    }

    public int getStartHour() {
        return startHour;
    }

    public int getStartMinute() {
        return startMinute;
    }

    public int getEndHour() {
        return endHour;
    }

    public int getEndMinute() {
        return endMinute;
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    public void setStartMinute(int startMinute) {
        this.startMinute = startMinute;
    }

    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }

    public void setEndMinute(int endMinute) {
        this.endMinute = endMinute;
    }

    public String getProperlyFormattedGap() {
        return "" + getHour(this.getStartHour()) + ":" + getMinute(this.getStartMinute()) + " " + getTimePeriod(this.getStartHour()) + " - " + getHour(this.getEndHour()) + ":" + getMinute(this.getEndMinute()) + " " + getTimePeriod(this.getEndHour());
    }

    private String getTimePeriod(int hour) {
        String period = "";

        if (hour >= 12 && hour <= 23) {
            period = "pm";
        }
        if (hour == 24 || (hour >= 0 && hour <= 11)) {
            period = "am";
        }

        return period;
    }

    private int getHour(int hour) {
        int convertedHour = 0;

        if (hour >= 1 & hour <= 12) {
            convertedHour = hour;
        } else {
            switch (hour) {
                case 0:
                    convertedHour = 12;
                    break;
                case 24:
                    convertedHour = 12;
                    break;
                case 13:
                    convertedHour = 1;
                    break;
                case 14:
                    convertedHour = 2;
                    break;
                case 15:
                    convertedHour = 3;
                    break;
                case 16:
                    convertedHour = 4;
                    break;
                case 17:
                    convertedHour = 5;
                    break;
                case 18:
                    convertedHour = 6;
                    break;
                case 19:
                    convertedHour = 7;
                    break;
                case 20:
                    convertedHour = 8;
                    break;
                case 21:
                    convertedHour = 9;
                    break;
                case 22:
                    convertedHour = 10;
                    break;
                case 23:
                    convertedHour = 11;
                    break;
            }
        }

        return convertedHour;
    }

    private String getMinute(int minute) {
        if (minute == 0) {
            return "00";
        } else {
            return "" + minute;
        }
    }

    public boolean checkEquals(Gap gap) {
        if ((this.getStartHour() == gap.getStartHour()) && (this.getStartMinute() == gap.getStartMinute()) && (this.getEndHour() == gap.endHour) && (this.endMinute == gap.endMinute)) {
            return true;
        } else {
            return false;
        }
    }
}
