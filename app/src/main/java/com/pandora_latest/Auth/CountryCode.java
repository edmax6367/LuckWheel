package com.pandora_latest.Auth;
public class CountryCode {
    private String code;
    private String name;
    private String numberCode;

    public CountryCode(String code, String name,String numberCode) {
        this.code = code;
        this.name = name;
        this.numberCode = numberCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumberCode() {
        return numberCode;
    }

    public void setNumberCode(String numberCode) {
        this.numberCode = numberCode;
    }

    @Override
    public String toString() {
        return code;
    }
}
