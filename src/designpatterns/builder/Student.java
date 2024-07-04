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

    public Student(Builder builder){
        //we can add any validations in this and in map as well.
        this.id= builder.getId();
        this.name= builder.getName();
        this.age= builder.getAge();
        this.gender= builder.getGender();
        this.major= builder.getMajor();
        this.score= builder.getScore();
        this.psp= builder.getPsp();
    }
}
//this is first version of builder design pattern
//IN this clients comes and then see he/she needs builder object then he will go inside builder then create
//create builder object then pass

