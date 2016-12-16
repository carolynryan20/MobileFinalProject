package hu.ait.android.mobilefinalproject.model;

import android.content.res.Resources;

import hu.ait.android.mobilefinalproject.R;

/**
 * Created by Carolyn on 11/10/16.
 */
public class User {

    private String email;
    private String username;
    private UserIcon userIcon;
    private String location;

    public User() {
    }

    public User(String email, String username, UserIcon userIcon, String location) {
        this.email = email;
        this.username = username;
        this.userIcon = userIcon;
        this.location = location;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLocation() { return location; }

    public void setLocation(String location) { this.location = location; }

    public UserIcon getIcon() {
        return userIcon;
    }

    public void setIcon(UserIcon userIcon) {
        this.userIcon = userIcon;
    }

    public enum UserIcon {
        ALEX(0, R.drawable.circle_alex),
        CAL(1, R.drawable.circle_cal),
        CAROLYN1(2, R.drawable.circle_carolyn1),
        CAROLYN2(3, R.drawable.circle_carolyn2),
        CHARLIE(4, R.drawable.circle_charlie),
        DYLAN(5, R.drawable.circle_dylan),
        SAM(6, R.drawable.circle_sam),
        FARIDA(7, R.drawable.circle_farida),
        KEITH(8, R.drawable.circle_keith),
        MO(9, R.drawable.circle_mo),
        NICK(10, R.drawable.circle_nick),
        OSCAR(11, R.drawable.circle_oscar);


        private int value;
        private int iconId;

        private UserIcon(int value, int iconId) {
            this.value = value;
            this.iconId = iconId;
        }

        public int getValue() {
            return value;
        }

        public int getIconId() {
            return iconId;
        }

        public static User.UserIcon fromInt(int value) {
            for (User.UserIcon s : User.UserIcon.values()) {
                if (s.value == value) {
                    return s;
                }
            }
            return FARIDA;
        }

        public static User.UserIcon toUserIconFromId(int iconId) {
            for (User.UserIcon s : User.UserIcon.values()) {
                if (s.getIconId() == iconId) {
                    return s;
                }
            }
            return FARIDA;
        }

        public static String fromIconId(int id) {
            switch (id) {
                case R.drawable.circle_alex:
                    return "ALEX";
                case R.drawable.circle_cal:
                    return "CAL";
                case R.drawable.circle_carolyn1:
                    return "CAROLYN1";
                case R.drawable.circle_carolyn2:
                    return "CAROLYN2";
                case R.drawable.circle_charlie:
                    return "CHARLIE";
                case R.drawable.circle_dylan:
                    return "DYLAN";
                case R.drawable.circle_sam:
                    return "SAM";
                case R.drawable.circle_farida:
                    return "FARIDA";
                case R.drawable.circle_keith:
                    return "KEITH";
                case R.drawable.circle_mo:
                    return "MO";
                case R.drawable.circle_nick:
                    return "NICK";
                case R.drawable.circle_oscar:
                    return "OSCAR";
            }
            return Resources.getSystem().getString(R.string.farida);
        }
    }
}

