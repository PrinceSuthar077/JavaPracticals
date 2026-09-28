abstract class Media { String title;
int daysLate;


Media(String title, int daysLate) 
{ this.title = title;
this.daysLate = daysLate;
}

abstract double calculateLateFee();
}

class Book extends Media {
Book(String title, int daysLate) 
{ super(title, daysLate); }
  
@Override double calculateLateFee() {
return Math.min(daysLate * 0.50, 15.00);
}
}

class DVD extends Media {
DVD(String title, int daysLate) 
{ super(title, daysLate); }

@Override double calculateLateFee() 
{ return daysLate * 2.00;
}
}


class Equipment extends Media {
Equipment(String title, int daysLate) 
{ super(title, daysLate); }


@Override double calculateLateFee() {
return daysLate > 0 ? 25.00 + (daysLate * 5.00) : 0;
}
}

public class LibraryFeeApp {

public static void main(String[] args) {

Media[] returnedBatch = {	new Book("The Great Gatsby", 4),	new Book("Intro to Algorithms", 40), new DVD("Inception", 3),
new Equipment("Projector", 2)
};

double totalBatchFees = 0.0;

System.out.println("--- Returned Batch Processing ---"); 
for (Media item : returnedBatch) 
{	double fee = item.calculateLateFee();	
totalBatchFees += fee;
System.out.printf("Item: %-20s | Days Late: %2d | Fee: $%.2f%n", item.title, item.daysLate, fee);
}

System.out.printf("%nTotal Fees Collected for Batch: $%.2f%n", totalBatchFees);
}
}
