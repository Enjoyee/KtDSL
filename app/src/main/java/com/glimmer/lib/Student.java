package com.glimmer.lib;

public class Student {
    private String name;

    private Student(String name) {
        this.name = name;
    }

    private String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
