import java.util.Scanner;

public class Test1 {
    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);

        switch (in.nextInt()) {
            case 0:
                System.out.println("zero");
            case 1:
                System.out.println("one");
            case 2:
                System.out.println("two");
            default:
                System.out.println("rather more");
        }
    }
}

