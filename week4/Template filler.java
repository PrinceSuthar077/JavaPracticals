public class TemplateFiller {

public static String fill(String template, String[] names, String[] values) {

for (int i = 0; i < names.length; i++) { template = template.replace(
"{" + names[i] + "}", values[i]
);
}
template = template.replaceAll("\\{\\w+\\}", "[?]"); return template;
 

	}
}
//drivercode

public class TemplateFillerDriver { public static void main(String[] args) {
String template = "Dear {name}, order {id} ships {date}.";

String[] names = {"name", "id"};
String[] values = {"Riya", "A07"};

String result = TemplateFiller.fill(template, names, values);

System.out.println(result); System.out.println("25AIML073");

}
}

