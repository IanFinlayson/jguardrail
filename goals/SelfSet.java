// common mistake here is setting parameter to itself thinking that
// you are setting the instance variable being shadowed

class Student {
    private String name;

    public Student(String name) {
        name = name;
    }
    
    public void report() {
        System.out.println(name + ":");
    }
}

public class SelfSet {
    public static void main(String args[]) {
        Student s1 = new Student("Bob Jones");
        s1.report();
    }
}

