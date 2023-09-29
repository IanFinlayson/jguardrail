// common mistake here is shadowing a variable with local one

class Student {
    private String name;
    double gpa;
    int credits;

    public Student(String name) {
        this.name = name;
        gpa = 0.0;
        credits = 0;
    }
   
    public void addCourse(double points, int courseCredits) {
        gpa = ((gpa * credits) + (points * courseCredits)) / (credits + courseCredits);
        if (points > 0) {
            credits += courseCredits;
        }
    }
    
    public void reset() {
        double gpa = 0.0;
        int credits = 0;
    }
    
    public void report() {
        System.out.println(name + ": " + gpa);
    }
}

public class ShadowLocal {
    public static void main(String args[]) {
        Student s1 = new Student("Amanda Lewis");
        s1.addCourse(3.7, 3);
        s1.addCourse(3.2, 3);
        s1.addCourse(4.0, 4);
        s1.addCourse(2.3, 1);
        s1.reset();
        s1.report();
    }
}

