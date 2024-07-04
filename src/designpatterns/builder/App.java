package designpatterns.builder;

import java.util.HashMap;

public class App {
    public static void main(String[] args) {
        //version 1
//        Student st= new Student("prakhar",21,"M","engineering");
//        Student st2= new Student("prakhar","M");
        //version 2- map
        HashMap<String,Object> map = new HashMap<>();//client creates this map and puts value inside as
        map.put("name","prakhar");
        map.put("age",23);
        map.put("gender","male");
        Student student = new Student(map);


    }
}
