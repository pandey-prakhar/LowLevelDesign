package designpatterns.builder;

import java.util.HashMap;

public class App {
    public static void main(String[] args) {
        //version 3
        Builder builder = new Builder();
        builder.setAge(21);
        builder.setGender("M");
        builder.setName("Prakhar Pandey");

        new Student(builder);

    }
}
