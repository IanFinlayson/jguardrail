// common mistake here is shadowing a variable with consdtructor parameter

class Student {
    private String name;

    public Student(String name) {
        name = name;
    }
    
    public void report() {
        System.out.println(name + ":");
    }
}

public class ShadowConstructor {
    public static void main(String args[]) {
        Student s1 = new Student("Bob Jones");
        s1.report();
    }
}

