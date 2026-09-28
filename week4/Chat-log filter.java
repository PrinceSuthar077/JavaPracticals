public class ChatFilter {
public static void filter(String[] logs, String keyword){ int count = 0;
StringBuilder result = new StringBuilder(); for (String line : logs) {
String[] data = line.split(" ", 3);
 

	if (data.length < 3) { continue;
}
String message = data[2]; if
(message.toLowerCase().contains(keyword.toLowerCase())) { count++;

result.append(data[0])
.append(" ")
.append(data[1])
.append(": ")
.append(data[2])
.append("\n");
}
}

System.out.println("Matches: " + count); 
System.out.println(result);

}
}

//drivercode

import java.util.Scanner; public class ChatFilterDriver {
public static void main(String[] args) {

String[] logs = {
"10:05 alice Hello there", "10:10 bob How are you?", "10:15 john Good morning", "10:20"
};

Scanner sc = new Scanner(System.in);

System.out.print("Enter keyword: "); String keyword = sc.nextLine();
ChatFilter.filter(logs, keyword); System.out.println("25AIML073");
 

	

sc.close();
}
}
