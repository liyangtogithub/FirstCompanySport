package com.desay.sport.update;

import java.io.InputStream;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TextUtils {

		public static  HashMap<String, String> parseXml (InputStream inStream) throws Exception{
			if(inStream == null){
				return null;
			}
	        HashMap<String, String> hashMap = new HashMap<String, String>();  
	        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
	        DocumentBuilder builder = factory.newDocumentBuilder();  
	        Document document = builder.parse(inStream);  
	        Element root = document.getDocumentElement();  
	        NodeList childNodes = root.getChildNodes();  
	        for(int i = 0; i < childNodes.getLength(); i++) {  
	            Node childNode = (Node) childNodes.item(i);  
	            if(childNode.getNodeType() == Node.ELEMENT_NODE) {  
	                Element childElement = (Element) childNode;  
	                if("version".equals(childElement.getNodeName())) {  
	                    hashMap.put("version", childElement.getFirstChild().getNodeValue());  
	                } else if("name".equals(childElement.getNodeName())) {  
	                    hashMap.put("name", childElement.getFirstChild().getNodeValue());  
	                } else if("url".equals(childElement.getNodeName())) {  
	                    hashMap.put("url", childElement.getFirstChild().getNodeValue());  
	                }  
	            }  
	              
	        }
	        inStream.close();
	        return hashMap;  
	    }  
}
