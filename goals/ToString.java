// common mistake here is tostring instead of toString

import java.util.ArrayList;

class Student {
    private ArrayList<Integer> grades = new ArrayList<>();
    private String name;

    public Student(String name) {
        this.name = name;
    }
    
    public String tostring() {
        String output = name + ":";
        for (Integer grade : grades) {
            output += grade;
        }
        
        return output;
    }
}

public class ToString {
    public static void main(String args[]) {
        Student s1 = new Student("Bob Jones");
        System.out.println(s1);
    }
}

