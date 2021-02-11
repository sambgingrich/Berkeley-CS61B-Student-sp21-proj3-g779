/** Class that prints the Collatz sequence starting from a given number.
 *  @author Samuel Gingrich
 */
public class Collatz {
    public static int nextNumber(int n){
        if (n%2 == 0)
            return n/2;
        else
            return n*3 + 1;
    }
    public static void main(String[] args) {
        int n = 5;
        while (n !=1) {
            System.out.print(n + " ");
            n = nextNumber(n);
        }
       System.out.print("1");

    }
}

