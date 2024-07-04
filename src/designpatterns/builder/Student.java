package designpatterns.builder;

public class Student {
    //lets assume there are multiple properties
    int id;
    String name;
    int age;
    String gender;
    String major;
    int score;
    int psp;


    public Student(String name, int age, String gender, String major) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.major = major;
    }

    public Student(String name, String gender) {
        this.name = name;
        this.gender = gender;
    }
}
//version1 -normal condtructor drawbacks
// prone to errors
// we can have normal constrictor and it will create objects but what if we want object to be created
//with different set of parameters then the possible combinations are 2^noOfParams
// additionally client need to make sure the orderening of params in constructors.
//at some point constrictor overloading is not even possible.
