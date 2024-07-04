package designpatterns.builder;

import java.util.Map;

public class Student {
    //lets assume there are multiple properties
    int id;
    String name;
    int age;
    String gender;
    String major;
    int score;
    int psp;

/*version1 -normal condtructor drawbacks
 prone to errors
 we can have normal constrictor and it will create objects but what if we want object to be created
with different set of parameters then the possible combinations are 2^noOfParams
 additionally client need to make sure the orderening of params in constructors.
at some point constrictor overloading is not even possible.

 */
//    public Student(String name, int age, String gender, String major) {
//        this.name = name;
//        this.age = age;
//        this.gender = gender;
//        this.major = major;
//    }
//
//    public Student(String name, String gender) {
//        this.name = name;
//        this.gender = gender;
//    }

    /*
    version 2- now we can rectify this multiple constructors by using a constructor taking map as param

    problems with this code is this that it is error prone client map write as map.put("Name", "rakesh") or
    map.put("age", "21")- typecast error
     */
    public Student( Map<String, Object> map){
        /*
        for easyness we are only checking for few properties
         */
        if(map.containsKey("name")){
            this.name = (String) map.get("name");
        }
        if(map.containsKey("age")){
            this.age = (int) map.get("age");
        }
        if(map.containsKey("gender")){
            this.gender = (String) map.get("gender");
        }
        if(map.containsKey("major")){
            this.major = (String) map.get("major");
        }
        //similarly for all others fields


    }

}

