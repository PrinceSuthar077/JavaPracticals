// Fraction.java


import java.util.Objects;


public class Fraction { private int num;
private int den;
 
public Fraction(int num, int den) { if (den == 0) {
throw new IllegalArgumentException("Denominator cannot be zero.");
}


int g = gcd(Math.abs(num), Math.abs(den));


this.num = num / g; this.den = den / g;


this.num = -this.num; this.den = -this.den;
}
}


private int gcd(int a, int b) { while (b != 0) {
int temp = b; b = a % b;
a = temp;
}
return a;
}
 
@Override
public String toString() { return num + "/" + den;
}


@Override
public boolean equals(Object obj) { if (this == obj)
return true;
if (!(obj instanceof Fraction)) return false;

Fraction f = (Fraction) obj;
return num == f.num && den == f.den;
}


@Override
public int hashCode() {
return Objects.hash(num, den);
}
}


// Driver.java
import java.util.Scanner; 
public class Driver {
 
public static void main(String[] args) { Scanner sc = new Scanner(System.in);

System.out.print("Enter numerator of first fraction: "); int n1 = sc.nextInt();
System.out.print("Enter denominator of first fraction: "); int d1 = sc.nextInt();

System.out.print("Enter numerator of second fraction: "); int n2 = sc.nextInt();
System.out.print("Enter denominator of second fraction: "); int d2 = sc.nextInt();

System.out.print("Enter numerator of third fraction: "); int n3 = sc.nextInt();
System.out.print("Enter denominator of third fraction: "); int d3 = sc.nextInt();

Fraction f1 = new Fraction(n1, d1); Fraction f2 = new Fraction(n2, d2); Fraction f3 = new Fraction(n3, d3);

System.out.println("\nFractions after reduction:"); System.out.println("Fraction 1: " + f1);
System.out.println("Fraction 2: " + f2);
 
System.out.println("Fraction 3: " + f3);


System.out.println("\nEquality Check:");
System.out.println("f1 equals f2: " + f1.equals(f2)); System.out.println("f2 equals f3: " + f2.equals(f3)); System.out.println("f1 equals f3: " + f1.equals(f3));

sc.close();
}
}
