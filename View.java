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
			out.setText("Search running...");
			buildQueue();
			
		}catch(NumberFormatException e){
			e.getMessage();
			limit.setText("");
			limitLabelError.setVisible(true);
		}
	}
	
	private void buildQueue(){
		long startTime = System.currentTimeMillis();
		count = 0;
		out.setText("");
		String webpage_link, outputText = "";
	    webpage_link = start.getText();
	    websiteQueue.add(webpage_link);
	    
	    while(websiteQueue.size() > 0 && count < limitNumber){
	    	try{
	    		
	    		Document doc = Jsoup.connect(websiteQueue.remove()).get();

	    		if(doc.text().contains(search.getText())){
	    			outputText += doc.attr("href");
	    		}
	    		
	    		Elements links = doc.select("a[href]");
	    		for (Element link : links) {
	    			
	    			//Check to see if link is a valid link to another page.
	    			count++;
	    			System.out.println(link.attr("abs:href"));
	    			websiteQueue.add(link.attr("abs:href"));
	    			if(link.text().contains(search.getText()))
	    				outputText += link.attr("href") + "\n";
	    		}
	    	}catch(IOException e){
	    		e.getMessage();
	    	}
	    }
	    long endTime = System.currentTimeMillis(); 
	    outputText += "End of search.\nSearched " + count + " links in " + (endTime - startTime) + " ms";
	    
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
