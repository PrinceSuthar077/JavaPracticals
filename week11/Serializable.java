import java.io.*;
class Student implements Serializable {
int id;
String name;
transient String password;
Student(int id, String name, String password) {
    this.id = id;
    this.name = name;
    this.password = password;
}
}
public class Main {
public static void main(String[] args) throws Exception {
Student[] students = {
new Student(1, "Prince", "abc123"),
new Student(2, "Rahul", "xyz456")
};
    ObjectOutputStream out = new ObjectOutputStream(
        new FileOutputStream("students.dat")
    );

    out.writeObject(students);
    out.close();

    ObjectInputStream in = new ObjectInputStream(
        new FileInputStream("students.dat")
    );

    Student[] loadedStudents = (Student[]) in.readObject();
    in.close();

    for (Student s : loadedStudents) {
        System.out.println("ID: " + s.id);
        System.out.println("Name: " + s.name);
        System.out.println("Password: " + s.password);
        System.out.println();
    }
}
}
