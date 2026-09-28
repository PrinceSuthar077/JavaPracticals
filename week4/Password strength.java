public class Passwordstrength {

public static String strength(String pw) { int count = 0;
if (pw.length() >= 8) count++;

if (pw.matches(".*[A-Z].*")) count++;

if (pw.matches(".*[0-9].*")) count++;

if (pw.matches(".*[^a-zA-Z0-9].*")) count++;

if (count <= 1) return "Weak";
else if (count <= 3) return "Medium";
else
return "Strong";
}
}

//drivercode

public class passwordstrengthdriver {

public static void main(String[] args) {
 

String p1 = "abc";
String p2 = "Abcd1234!";

System.out.println(p1 + " -> " + Passwordstrength.strength(p1));
System.out.println(p2 + " -> " + Passwordstrength.strength(p2));
System.out.println("25AIML073");

}
}
