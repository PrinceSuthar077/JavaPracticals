
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String sentence = sc.nextLine().trim();
        String keyword = sc.nextLine().trim();

        String[] words = sentence.split(" +");
        StringBuilder formatted = new StringBuilder();
        StringBuilder acronym = new StringBuilder();

        String longest = "";
        int keywordCount = 0;

        for (String word : words) {
            String titleWord = word.substring(0, 1).toUpperCase()
                    + word.substring(1).toLowerCase();

            if (formatted.length() > 0) {
                formatted.append(" ");
            }

            formatted.append(titleWord);
            acronym.append(word.charAt(0));

            if (titleWord.length() > longest.length()) {
                longest = titleWord;
            }

            if (word.equalsIgnoreCase(keyword)) {
                keywordCount++;
            }
        }

        System.out.println("Formatted: " + formatted);
        System.out.println("Words: " + words.length);
        System.out.println("Longest word: " + longest);
        System.out.println("Keyword count: " + keywordCount);
        System.out.println("Acronym: " + acronym.toString().toUpperCase());

        sc.close();
    }
}
