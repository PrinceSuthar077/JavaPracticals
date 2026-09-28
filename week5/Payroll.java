abstract class Employee { String name;
String id;


Employee(String name, String id)
{ this.name = name;
this.id = id;
}

abstract double monthlySalary();
}

class FullTime extends Employee
{ double fixedSalary;

FullTime(String name, String id, double fixedSalary)
{	super(name, id);	this.fixedSalary = fixedSalary;
}

@Override double monthlySalary()
{ return fixedSalary; }
}

class PartTime extends Employee 
{ double hours;	double rate;

PartTime(String name, String id, double hours, double rate)
{	super(name, id);	this.hours = hours;	this.rate = rate;
}

@Override double monthlySalary() 
 { return hours * rate; }
}

class Intern extends Employee 
{ double stipend;
Intern(String name, String id, double stipend) 
{ super(name, id);	
 this.stipend = stipend;
}

@Override double monthlySalary() 
 { return stipend ; }
}

public class PayrollApp 
{ public static void main(String[] args) {
Employee[] employees = {
new FullTime("Alice", "FT001", 6000.00),	
new PartTime("Bob", "PT001", 80, 25.00),
new Intern("Charlie", "IN001", 1500.00)
};

double totalPayroll = 0.0;

for (Employee emp : employees) 
{ double salary = emp.monthlySalary(); 
 totalPayroll += salary;

System.out.printf("ID: %s | Name: %s | Salary: $%.2f", emp.id, emp.name, salary);

if (emp instanceof Intern) {
System.out.print(" (Note: Intern stipend applied)");
}
System.out.println();
}

System.out.printf("%nTotal Monthly Payroll: $%.2f%n", totalPayroll);
}
}
