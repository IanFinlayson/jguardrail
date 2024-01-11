// common mistake here is failure to initialize an instance variable

import java.util.ArrayList;

class Student {
    private ArrayList<Integer> grades = new ArrayList<>();
    private String name;

    public Student(String name) {
        this.name = name;
    }
    
    public void report() {
        System.out.println(name + ":");
        for (Integer grade : grades)
            System.out.println(grade);
            
        if (name.equals(""))
            System.out.println("name is empty");

    }
}

public class RawLoop {
    public static void main(String args[]) {
        Student s1 = new Student("Bob Jones");
        s1.report();
    }
}

