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
        for (Integer grade : grades);
        {
            System.out.println(grade);
        }
    }
}

public class InstVarInit {
    public static void main(String args[]) {
        Student s1 = new Student("Bob Jones");
        s1.report();
    }
}

