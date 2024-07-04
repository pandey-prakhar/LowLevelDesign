package designpatterns.builder;

public class Student {
    //lets assume there are multiple properties
    private int id;
    private String name;
    private int age;
    private String gender;
    private String major;
    private int score;
    private int psp;

    public static Builder getBuilder(){
        return new Builder();
    }

    private Student(Builder builder){
        //we can add any validations in this and in map as well.
        this.id= builder.getId();
        this.name= builder.getName();
        this.age= builder.getAge();
        this.gender= builder.getGender();
        this.major= builder.getMajor();
        this.score= builder.getScore();
        this.psp= builder.getPsp();
    }

     static class Builder {
         private int id;
         private String name;
         private int age;
         private String gender;
         private String major;
         private int score;
         private int psp;

         public Student build() {
             // Inside build we can have all the validations
             if (name == null || name.isEmpty()) {
                 throw new IllegalArgumentException("Name cannot be null or empty");
             }
             if (age <= 5) {
                 throw new IllegalArgumentException("Age must be greter than 5 to take admission");
             }
             return new Student(this);
         }

        public int getId() {
            return id;
        }

        public Builder setId(int id) {
            this.id = id;
            return this;
        }

        public String getName() {
            return name;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public int getAge() {
            return age;
        }

        public Builder setAge(int age) {
            this.age = age;
            return this;
        }

        public String getGender() {
            return gender;
        }

        public Builder setGender(String gender) {
            this.gender = gender;
            return this;
        }

        public String getMajor() {
            return major;
        }

        public Builder setMajor(String major) {
            this.major = major;
            return this;
        }

        public int getScore() {
            return score;
        }

        public Builder setScore(int score) {
            this.score = score;
            return this;
        }

        public int getPsp() {
            return psp;
        }

        public void setPsp(int psp) {
            this.psp = psp;
        }
    }
}


