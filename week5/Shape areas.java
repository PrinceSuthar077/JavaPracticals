abstract class Shape
  { abstract double area();
}


class Circle extends Shape 
{ double radius;
Circle(double radius) 
 { this.radius = radius; }
@Override double area() 
 { return Math.PI * radius * radius; } 
@Override public String toString() 
 { return "Circle"; }
}

class Rectangle extends Shape
  { double width, height;
Rectangle(double width, double height)
   { this.width = width; 
    this.height = height; } 
@Override double area()
   { return width * height; }
@Override public String toString()
   { return "Rectangle"; }
}

class Triangle extends Shape
{ double base, height;
Triangle(double base, double height)
 { this.base = base; this.height = height; } 
 @Override double area()
 { return 0.5 * base * height; }
@Override public String toString()
 { return "Triangle"; }
}


public class ShapeAreaApp 
{	public static void main(String[] args)
 { Shape[] shapes = {	new Circle(5.0),		new Rectangle(4.0, 6.0),	new Triangle(4.0, 5.0),
new Circle(2.5)
};


double totalArea = 0.0; 
  double maxArea = -1.0;
Shape largestShape = null;

for (Shape shape : shapes) { double currentArea = shape.area(); totalArea += currentArea;

System.out.printf("%s Area: %.2f | Running Total: %.2f%n", shape, currentArea, totalArea);


if (currentArea > maxArea)
{ maxArea = currentArea;
largestShape = shape;
}
}


System.out.printf("%nLargest Shape: %s with area %.2f%n", largestShape, maxArea);
}
}
