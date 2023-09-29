// common mistake here is forgetting break in switch cases

class Student {
    private String name;
    private char grade;

    public Student(String name, char grade) {
        this.name = name;
        this.grade = grade;
    }
    
    public void report() {
        System.out.println(name + ": " + grade);
        switch (grade) {
            case 'A':
                System.out.println("Nice job!");
            case 'B':
                System.out.println("Well done");
            case 'C':
                System.out.println("Alright");
            case 'D':
                System.out.println("Try harder next time");
            case 'F':
                System.out.println(":(");
        }
    }
}

public class SwitchCase {
    public static void main(String args[]) {
        Student s1 = new Student("Alice", 'B');
        Student s2 = new Student("George", 'D');
        
        s1.report();
        s2.report();
    }
}

