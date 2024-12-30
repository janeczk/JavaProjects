package org.example.java3;

import java.io.File;
import java.util.Objects;
import java.util.StringTokenizer;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.ArrayList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;



class Handler_1 extends DefaultHandler {
 String loc_name;
 private ArrayList<Double> x_p = new ArrayList<>();
 private ArrayList<Double> y_p = new ArrayList<>(); // Dodałem y_p

 @Override
 public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
  for (int i = 0; i < attributes.getLength(); i++) {
   String loc_name = attributes.getQName(i);
   System.out.println("attr name : " + loc_name + " value:" + attributes.getValue(loc_name));
   double startX = 0, startY = 0;  // To store the starting point if 'z' is used

   if (Objects.equals(loc_name, "d")) {
    StringTokenizer token = new StringTokenizer(attributes.getValue(loc_name), " ");

    double currentX = 0;
    double currentY = 0;
    boolean closePath = false;

    while (token.hasMoreTokens()) {
     String currentToken = token.nextToken().trim();

     if (currentToken.startsWith("M")) {
      String remaining = currentToken.substring(1).trim();
      if (remaining.isEmpty()) {
       remaining = token.nextToken().trim();
      }

      String[] coordinates = remaining.split(",");
      try {
       currentX = Double.parseDouble(coordinates[0]);
       currentY = Double.parseDouble(coordinates[1]);

       startX = currentX;
       startY = currentY;

       x_p.add(currentX);
       y_p.add(currentY);
      } catch (NumberFormatException e) {
       System.out.println("Error parsing coordinates: " + remaining);
      }
     }
     else if (currentToken.startsWith("l")) {
      String remaining = currentToken.substring(1).trim();
      if (remaining.isEmpty()) {
       remaining = token.nextToken().trim();
      }
      String[] coordinates = remaining.split(",");
      try {
       double deltaX = Double.parseDouble(coordinates[0]);
       double deltaY = Double.parseDouble(coordinates[1]);

       currentX += deltaX;
       currentY += deltaY;

       x_p.add(currentX);
       y_p.add(currentY);
      } catch (NumberFormatException e) {
       System.out.println("Error parsing relative coordinates: " + remaining);
      }
     }
     else if (currentToken.equals("z")) {
      closePath = true;
     }
     else {
      String[] coordinates = currentToken.split(",");
      try {
       double deltaX = Double.parseDouble(coordinates[0]);
       double deltaY = Double.parseDouble(coordinates[1]);

       currentX += deltaX;
       currentY += deltaY;

       x_p.add(currentX);
       y_p.add(currentY);
      } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
       System.out.println("Error parsing relative coordinates: " + currentToken);
      }
     }
    }
    if (closePath) {
     x_p.add(startX);
     y_p.add(startY);
    }
    x_p.add(999.);
    y_p.add(999.);
   }
  }
 }
 public void print_loc(){

 }
 public ArrayList<Double> getX_p() {
  return x_p;
 }

 public ArrayList<Double> getY_p() {
  return y_p;
 }

 @Override
 public void endElement(String uri, String localName, String qName) throws SAXException {
  if (qName.equalsIgnoreCase("svg")) {
   System.out.println("End Element :" + qName);
  }
 }

 @Override
 public void characters(char ch[], int start, int length) throws SAXException {
  System.out.println(new String(ch, start, length));
 }
}

public class Parser_1 {
 private Handler_1 handler_1;

 public void parseXML() {
  try {
   File inputFile = new File("D:\\Java\\HamudaJava\\Java3parser1\\src\\points.xml");
   SAXParserFactory factory = SAXParserFactory.newInstance();
   SAXParser saxParser = factory.newSAXParser();

   handler_1 = new Handler_1();
   saxParser.parse(inputFile, handler_1);
  } catch (Exception e) {
   e.printStackTrace();
  }
 }

 public Handler_1 getHandler() {
  return handler_1;
 }
}
