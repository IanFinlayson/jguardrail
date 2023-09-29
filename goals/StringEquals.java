// common mistake here is using == with strings

class Student {
    private String name;

    public Student(String name) {
        this.name = name;
    }
    
    public void report() {
        if (name == "John") {
            System.out.println("It's John!!!!!");
        } else {
            System.out.println("It's " + name);
        }
    }
}

public class StringEquals {
    public static void main(String args[]) {
        char name[] = {'J', 'o', 'h', 'n'};
        Student s1 = new Student(new String(name));
        Student s2 = new Student("Susan");

        s1.report();
        s2.report();
    }
}

