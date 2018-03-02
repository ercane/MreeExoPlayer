package mree.cloud.music.player.common.ref;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by eercan on 14.07.2017.
 */

public enum AppTheme {

    BASE(1, "Base"),
    DARK(2, "Dark"),
    PINK(3, "Pink");

    private Integer code;
    private String desc;

    AppTheme(int code, String desc) {
        this.code = code;
        this.desc = desc;

    }

    public static AppTheme get(Integer code) {

        if (code == null) {
            return null;
        }

        for (AppTheme v : values()) {
            if (v.code == code) {
                return v;
            }
        }
        throw new IllegalArgumentException("No matching type: " + code);
    }

    public static AppTheme get(String desc) {

        if (desc == null) {
            return null;
        }

        for (AppTheme v : values()) {
            if (v.desc == desc) {
                return v;
            }
        }
        throw new IllegalArgumentException("No matching desc: " + desc);
    }

    public static String encode(Set<AppTheme> csSet) {
        StringBuilder enc = new StringBuilder();

        for (AppTheme cs : csSet) {
            enc.append(cs.getCode()).append("-");
        }

        if (enc.length() > 1) {
            enc.replace(enc.length() - 1, enc.length(), "");
        }

        return enc.toString();
    }

    public static Set<AppTheme> decode(String encoded) {

        Set<AppTheme> csList = new HashSet<AppTheme>();

        if (encoded != null) {

            String[] split = encoded.split("-");

            for (String s : split) {
                try {
                    int c = Integer.parseInt(s);
                    csList.add(get(c));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return csList;
    }

    public static Set<Integer> getCodes(Set<AppTheme> csSet) {
        Set<Integer> codeSet = new HashSet<Integer>();

        for (AppTheme cs : csSet) {
            codeSet.add(cs.getCode());
        }

        return codeSet;
    }

    public static Set<Integer> getCodes(String encoded) {
        return getCodes(decode(encoded));
    }

    public static String[] getCodes() {
        String[] str = new String[AppTheme.values().length];
        for (AppTheme appTheme : AppTheme.values()) {
            str[appTheme.getCode() - 1] = appTheme.getCode().toString();
        }
        return str;
    }

    public static String[] getStringValues() {
        String[] str = new String[AppTheme.values().length];
        for (AppTheme appTheme : AppTheme.values()) {
            str[appTheme.getCode() - 1] = appTheme.getDesc();
        }
        return str;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
