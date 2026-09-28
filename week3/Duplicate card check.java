// Card.java


import java.util.Objects;


public class Card {
private String rank; private String suit;

public Card(String rank, String suit) {
 
this.rank = rank; this.suit = suit;
}


@Override
public String toString() {
return rank + " of " + suit;
}


@Override
public boolean equals(Object obj) { if (this == obj)
return true;
if (!(obj instanceof Card)) return false;

Card c = (Card) obj;
return rank.equals(c.rank) && suit.equals(c.suit);
}


@Override
public int hashCode() {
return Objects.hash(rank, suit);
}
}
 

// Driver.java


import java.util.Scanner;


public class Driver {
public static void main(String[] args) { Scanner sc = new Scanner(System.in);

System.out.print("Enter number of cards: "); int n = sc.nextInt();
sc.nextLine();


Card[] cards = new Card[n]; boolean duplicateFound = false;

for (int i = 0; i < n; i++) {
System.out.print("Enter rank: "); String rank = sc.nextLine();

System.out.print("Enter suit: "); String suit = sc.nextLine();

Card newCard = new Card(rank, suit);
 
for (int j = 0; j < i; j++) {
if (newCard.equals(cards[j])) {
System.out.println("Duplicate found: " + newCard); duplicateFound = true;
break;
}
}


cards[i] = newCard;


if (duplicateFound) break;
}


if (!duplicateFound) {
System.out.println("No duplicate card found.");
}


sc.close();
}
}
