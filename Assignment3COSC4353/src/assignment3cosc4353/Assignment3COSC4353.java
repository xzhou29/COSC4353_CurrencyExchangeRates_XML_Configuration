/*
11/30/2016
Team Members: Xin Zhou, Xin Zhang
 */
package assignment3cosc4353;

import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.util.*;


/**
 *
 * @author XinZhou
 */
public class Assignment3COSC4353 {

     String pair;
     double targetRate = 0.0;
     double currentRate = 0.0;
     
     public  void pairInput() throws Exception //user input information
     {

        Scanner keyboard = new Scanner(System.in);  
        System.out.print("Please input the pair from the list of pairs above: ");
        pair = keyboard.nextLine();
      //System.out.println("Pair : " + pair);
        //System.out.println("Target rate : " + targetRate);
     }
     public Boolean targetValueInput() throws Exception{
       
        Scanner keyboard = new Scanner(System.in);
        
        System.out.print("Set a target rate (Must be a number): ");  
        
        
        if(keyboard.hasNextDouble()){
            targetRate = keyboard.nextDouble();
            return true;
        }
        else{
            System.out.println("Target rate must be a number(Ex:55, 15.46, etc )");
            return false;
        }
        
 }
     
     
     private Document parseXML() throws Exception //parse XML file from website
     {
         DocumentBuilderFactory objDocumentBuilderFactory = null;
         DocumentBuilder objDocumentBuilder = null;
         Document doc = null;
         
         String urlString = "http://rates.fxcm.com/RatesXML";
         URL url = new URL(urlString);
         URLConnection connection = url.openConnection();
         connection.setRequestProperty("User-Agent", "Mozilla/5.0");
         
         try
         {
             objDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
             objDocumentBuilder = objDocumentBuilderFactory.newDocumentBuilder();
             doc = objDocumentBuilder.parse(connection.getInputStream());            
         }
         catch(Exception ex)
         {
             throw ex;
         }
         
         return doc;
     }
    
     public boolean checkPair() throws Exception //check if pair exists
     {
         boolean isCheckPair = false;
         String test;
         
         Document doc = parseXML();
         doc.getDocumentElement().normalize();
         
         NodeList nList = doc.getElementsByTagName("Rate");
         
         for (int temp = 0; temp < nList.getLength(); temp++)
         {
             Node nNode = nList.item(temp);
             
             if (nNode.getNodeType() == Node.ELEMENT_NODE) 
             {
                 Element eElement = (Element) nNode;
                 test = eElement.getAttribute("Symbol");

                 if (pair != null)
                 {
                     if (pair.equals(test))
                     {
                          isCheckPair = true; 
                     }
                 }
             }   
         }
         
         if (isCheckPair == true)
         {
             return true;
         }
         else
         {
             return false;
         }
     }
     
    public void pairList(){ 
                   
        try{
            Document doc = parseXML();
            doc.getDocumentElement().normalize();


            NodeList nList = doc.getElementsByTagName("Rate");
            
            for (int temp = 0; temp < nList.getLength(); temp++) {
               Node nNode = nList.item(temp);            
               if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                  Element eElement = (Element) nNode;

                  System.out.println("Pair Symbol List : " + eElement.getAttribute("Symbol"));
               }
            }
            
            System.out.println("-------------------------------------------------");
         } catch (Exception e) {
            e.printStackTrace();
         }
     }
     
     
     public boolean checkTargetRate() throws Exception //check if target has been reached
     {
         Document doc = parseXML();
         doc.getDocumentElement().normalize();
         
         NodeList nList = doc.getElementsByTagName("Rate");
         
         for (int temp = 0; temp < nList.getLength(); temp++)
         {
             Node nNode = nList.item(temp);
             
             if (nNode.getNodeType() == Node.ELEMENT_NODE) 
             {
                 Element eElement = (Element) nNode;

                 if (pair != null)
                 {
                     if (pair.equals(eElement.getAttribute("Symbol")))
                     {
                          currentRate = Double.parseDouble(eElement.getElementsByTagName("Bid").item(0).getTextContent()); 
                     }
                 }
             }   
         }
         
         if (targetRate <= currentRate)
         {
             return true;
         }
         else 
         {
             return false;
         }
     }
     
     public  void outputCurrentInformation() throws Exception
     {
         Document doc = parseXML();
         doc.getDocumentElement().normalize();
         
         NodeList nList = doc.getElementsByTagName("Rate");
         
         for (int temp = 0; temp < nList.getLength(); temp++)
         {
             Node nNode = nList.item(temp);
             
             if (nNode.getNodeType() == Node.ELEMENT_NODE) 
             {
                 Element eElement = (Element) nNode;

                 if (pair != null)
                 {
                     if (pair.equals(eElement.getAttribute("Symbol")))
                     {
                            System.out.println("Currency Symbol : " + eElement.getAttribute("Symbol"));

                            System.out.println("Bid  : " + eElement.getElementsByTagName("Bid").item(0).getTextContent());

                            System.out.println("Ask : " + eElement.getElementsByTagName("Ask").item(0).getTextContent());

                            System.out.println("Day High Rate : " + eElement.getElementsByTagName("High").item(0).getTextContent());

                            System.out.println("Day Low rate : "  + eElement.getElementsByTagName("Low").item(0).getTextContent());

                            System.out.println("Direction : "  + eElement.getElementsByTagName("Direction").item(0).getTextContent());

                            System.out.println("Last updated : "  + eElement.getElementsByTagName("Last").item(0).getTextContent()); 
                     }
                 }
             }  
         }
     }
     
     public static void main(String[] args) 
     {
         try 
         {
            Assignment3COSC4353 myUser = new Assignment3COSC4353();
             
            myUser.pairList();
            myUser.pairInput();
            while (myUser.checkPair() == false)
            {
                System.out.println("Invalid Pair, please input again:");
                myUser.pairInput(); 
                System.out.println();
            }
            
             System.out.println("-------------------------------------------------");
             myUser.outputCurrentInformation();
             System.out.println("-------------------------------------------------");
             
             while(!myUser.targetValueInput()){
                 myUser.targetValueInput();
                 
             }
            
             
             System.out.println();
 
             while (myUser.checkTargetRate() == false)
             {
                 myUser.outputCurrentInformation();
                 System.out.println("-------------------------------------------------");
                 System.out.println("Target rate has not been reached");             
                 System.out.println();
                 Thread.sleep(60000);
             }
             
             myUser.outputCurrentInformation();
             System.out.println("Target rate has  been reached");
         }
         catch (Exception e) 
         {
	     e.printStackTrace();
         }
     }
     
}    



