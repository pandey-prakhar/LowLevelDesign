package designpatterns.builder;

import java.util.HashMap;

public class App {
    public static void main(String[] args) {
        //version 4
//        Builder builder = Student.getBuilder();
//        Student student = builder.build();
        Student st2= Student.getBuilder().setGender("Male").build();


    }
}
