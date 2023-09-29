// common mistake here is making a constructor void

import java.util.ArrayList;

class Student {
    private ArrayList<Integer> grades;

    public void Student() {
        this.grades = new ArrayList<>();
    }

    public void addGrade(int grade) {
        grades.add(grade);
    }

    public void report() {
        for (Integer grade : grades) {
            System.out.println(grade);
        }
    }
}

public class VoidConstructors {
    public static void main(String args[]) {
        Student s1 = new Student();
        s1.addGrade(87);
        s1.addGrade(92);
        s1.report();
    }
}

