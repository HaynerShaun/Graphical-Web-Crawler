import java.awt.*;

import javax.swing.*;

import java.awt.event.*;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element; 
import org.jsoup.select.Elements;

public class View extends JFrame{
	private JLabel startLabel = new JLabel("Start:");
	private JLabel searchLabel = new JLabel("Search:");
	private JLabel limitLabel = new JLabel("Limit:");
	private JLabel limitLabelError = new JLabel("Invalid limit");
	
	private JTextField start = new JTextField(20);
	private JTextField search = new JTextField(20);
	private JTextField limit = new JTextField(20);

	private JButton go = new JButton("Go");
	private JButton reset = new JButton("Reset");
	
	private JTextArea out = new JTextArea(15, 23);
	private JScrollPane output = new JScrollPane(out);
	
	private final int FRAMEWIDTH = 600;
	private final int FRAMEHEIGHT = 300;
	private final int startX = 10;
	private final int startY = 10;
	private int count = 0;
	private int limitNumber = 0;
	
	private Queue<String> websiteQueue = new LinkedList<String>();
	
	public View()
	{
		super.setLayout(null);
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		super.setBackground(Color.black);
		
		setLocations();

		go.addActionListener(new goButtonListener());
		reset.addActionListener(new resetButtonListener());
		
		setStarterText();
		
		super.add(startLabel);
		super.add(start);
		super.add(searchLabel);
		super.add(search);
		super.add(limitLabel);
		super.add(limitLabelError);
		super.add(limit);
		super.add(go);
		super.add(reset);
		super.add(output);
		
		super.setSize(FRAMEWIDTH, FRAMEHEIGHT);
		super.setVisible(true);
		super.setResizable(false);
		super.setLocationRelativeTo(null);
	}
	
	private void setStarterText(){
		start.setText("http://www.derekbarrera.com");
		search.setText("Tulips");
		limit.setText("15");
	}
	
	private void search(){
		limitLabelError.setVisible(false);
		try{
			limitNumber = Integer.parseInt(limit.getText());
			buildQueueTest2();
			
		}catch(NumberFormatException e){
			e.getMessage();
			limit.setText("");
			limitLabelError.setVisible(true);
		}
	}
	
	private void buildQueueTest2(){
		/*
		add while loop for while queue is not empty and while less then limit.
			add initial website to queue before while. 
			dequeue website, search that website for links, add all links to the queue through the for loop. 
		
		put for loop inside the while loop. 
		
		do not use embedded for loops. 
	*/
		long startTime = System.currentTimeMillis();
		count = 0;
		out.setText("");
		String webpage_link, outputText = "";
	    webpage_link = start.getText();
	    websiteQueue.add(webpage_link);
	    
	    while(websiteQueue.size() > 0 && count < limitNumber){
	    	try{
	    		
	    		Document doc = Jsoup.connect(websiteQueue.remove()).get();

	    		String title = doc.title();
	    		
	    		if(doc.text().contains(search.getText())){
	    			outputText += webpage_link;
	    		}
	    		
	    		Elements links = doc.select("a[href]");
	    		for (Element link : links) {
	    			if(link.attr("href").length() >= 2){
	    				count++;
	    				System.out.println(link.attr("href") + "\n\t" + title + "\n\t" + link.text());
	    				websiteQueue.add(link.attr("href"));
	    				if(link.text().contains(search.getText()))
	    					outputText += link.attr("href") + "\n";
	    			}
	    		}
	    	}catch(Exception e){
	    		e.getMessage();
	    	}
	    }
	    long endTime = System.currentTimeMillis(); 
	    outputText += "End of search.\nSearched " + count + " links\n";
	    outputText += "Search speed: " + (endTime - startTime) + " ms";
	    
	    out.setText(outputText);
	}
	
	//attempt with embedded for loops. 
	private void buildQueueTest(){
		count = 0;
		out.setText("");
		String webpage_link, outputText = "";
	    webpage_link=start.getText();
	    try{
	    	Document doc = Jsoup.connect(webpage_link).get();

	    	String title = doc.title();
	     
	    	Elements links = doc.select("a[href]");
	    	for (Element link : links) {
	    		count++;
    	    	System.out.println(link.attr("href") + "\n" + title + "\n" + link.text() + "\n");
    	    	websiteQueue.add(link.attr("href"));
    	    	Document doc2 = Jsoup.connect(link.attr("href")).get();
    	    	String title2 = doc2.title();
    	    	Elements links2 = doc2.select("a[href]");
    	    	if(links2.size() > 0)
    	    	{
    	    		for (Element link2 : links2) {
    	    			websiteQueue.add(link2.attr("href"));
    	    			count++;
    	    			System.out.println("\t" + link2.attr("href") + "\n\t" + title2 + "\n\t" + link2.text() + "\n");
    	    			//Document doc3 = Jsoup.connect(link.attr("href")).get();
    	    	    	//String title3 = doc3.title();
    	    	    	//Elements links3 = doc3.select("a[href]");
    	    	    	//if(links3.size() > 0)
    	    	    	//{
    	    	    	//	for (Element link3 : links3) {
    	    	    	//		websiteQueue.add(link3.attr("href"));
    	    	    	//		count++;
    	    	    	//		System.out.println("\t\t" + link3.attr("href") + "\n\t\t" + title3 + "\n\t\t" + link3.text() + "\n");		
    	    	    	//	}
    	    	    	//}
    	    		}
    	    	}
	    	}
	    }catch(Exception e){
	    	e.getMessage();
	    }
	    System.out.println();
	    System.out.println(websiteQueue);
	    System.out.println(websiteQueue.size());
	    System.out.println();
	    outputText += "End of search.\nSearched " + count + " links";
	    out.setText(outputText);
	}
	
	//this just grabs all the links on the first website. 
	private void buildQueue(){
		count = 0;
		out.setText("");
		String webpage_link, outputText = "";
	    webpage_link=start.getText();
	    try{
	    	Document doc = Jsoup.connect(webpage_link).get();

	    	String title = doc.title();
	     
	    	Elements links = doc.select("a[href]");
	    	for (Element link : links) {
	    		count++;
    	    	System.out.println(link.attr("href") + "\n" + title + "\n" + link.text() + "\n");
	    		if(link.text().contains(search.getText()) || title.contains(search.getText()))
	    		{
	    	    	outputText += link.attr("href") + "\n";
	    		}
	    	}
	    }catch(Exception e){
	    	e.getMessage();
	    }
	    outputText += "End of search.\nSearched " + count + " links";
	    out.setText(outputText);
	}
	  
	private void setLocations(){
		Dimension size;
		
		size = startLabel.getPreferredSize();
		startLabel.setBounds(startX,startY, size.width, size.height);
		size = start.getPreferredSize();
		start.setBounds(startLabel.getX() + startLabel.getWidth() + 15,startLabel.getY(), size.width, size.height);
		size = searchLabel.getPreferredSize();
		searchLabel.setBounds(startLabel.getX(),startLabel.getY() + 75, size.width, size.height);	
		size = search.getPreferredSize();
		search.setBounds(searchLabel.getX() + searchLabel.getWidth() + 15,searchLabel.getY(), size.width, size.height);	
		size = limitLabel.getPreferredSize();
		limitLabel.setBounds(startLabel.getX(),searchLabel.getY() + 75, size.width, size.height);
		size = limit.getPreferredSize();
		limit.setBounds(limitLabel.getX() + limitLabel.getWidth() + 15,limitLabel.getY(), size.width, size.height);		
		size = limitLabelError.getPreferredSize();
		limitLabelError.setBounds(limit.getX(),limit.getY() + limit.getHeight() + 5, size.width, size.height);		
		size = go.getPreferredSize();
		go.setBounds(startLabel.getX() + 25,limitLabel.getY() + 75, size.width, size.height);	
		size = reset.getPreferredSize();
		reset.setBounds(go.getX() + go.getWidth() + 50,go.getY(), size.width, size.height);	
		size = output.getPreferredSize();
		output.setBounds(start.getX() + start.getWidth() + 50,startLabel.getY(), size.width, size.height);
		
		out.setEditable(false);
		
		limitLabelError.setVisible(false);
	}
	
	private class goButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			search();
		}
	}
	
	private class resetButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			start.setText("");
			search.setText("");
			limit.setText("");
			out.setText("");
		}
	}
}
