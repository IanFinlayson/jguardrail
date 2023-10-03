import java.util.Scanner;

public class Test1 {
    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);

        switch (in.nextInt()) {
            case 0:
                int y;
                System.out.println("zero");
                break;
            case 1:
                System.out.println("one");
                {System.out.println("a"); System.out.println("b");System.out.println("c");}
            case 2:
                System.out.println("two");
                break;
            default:
                System.out.println("rather more");
        }
    }
}

