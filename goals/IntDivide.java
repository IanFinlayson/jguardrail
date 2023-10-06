// common mistake here is using a fraction with integer division

class Student {
    private String name;
    private int credits;

    public Student(String name, int credits) {
        this.name = name;
        this.credits = credits;
    }
    
    public void report() {
        System.out.println(name + ":");
    }
    
    public int avgNumCourses() {
        return credits * (1/3);
    }
}

public class IntDivide {
    public static void main(String args[]) {
        Student s1 = new Student("Bob Jones", 33);
        System.out.println(s1.avgNumCourses());
    }
}

