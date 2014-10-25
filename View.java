import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/*
 * The View class builds the GUI, handles the user input, and executes the web search based on user input
 */

public class View extends JFrame{
	private JLabel startLabel = new JLabel("Start:");
	private JLabel searchLabel = new JLabel("Search:");
	private JLabel startLabelError = new JLabel("URL must start with http:// or https://");
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
	private int count = 0, foundCount = 0;
	private int limitNumber = 0;

	private Queue<String> websiteQueue;
	private Queue<String> searchedWebsites;
	
	/*
	 * View() represents the constructor for the view class. 
	 */
	public View()
	{
		super.setLayout(null);
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		super.setBackground(Color.black);
		
		setLocations();

		go.addActionListener(new goButtonListener());
		reset.addActionListener(new resetButtonListener());
		
		super.add(startLabel);
		super.add(start);
		super.add(searchLabel);
		super.add(startLabelError);
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
	
	/*
	 * the search method instantiates the queues and calls the buildQueue method
	 */
	private void search(){
		websiteQueue = new LinkedList<String>();
		searchedWebsites = new LinkedList<String>();
		limitLabelError.setVisible(false);
		startLabelError.setVisible(false);
		try{
			limitNumber = Integer.parseInt(limit.getText());
			if(!(start.getText().contains("http"))){
				throw new Exception();
			}
			count = 0;
			foundCount = 0;
			out.setText("");
			buildQueue();
			
		}catch(NumberFormatException e){
			e.getMessage();
			limit.setText("");
			limitLabelError.setVisible(true);
		}catch(Exception e){
			e.getMessage();
			start.setText("");
			startLabelError.setVisible(true);
		}
	}
	
	/*
	 * the buildQueue method gets user input. Then starts searching the users first link, grabs all 
	 * links off that page. then continues to search links to it on each page it searched until the 
	 * limit is reached
	 */
	private void buildQueue(){
		String temp = "%";
		long startTime = System.currentTimeMillis();
		String webpage_link, outputText = "";
	    webpage_link = start.getText();
	    websiteQueue.add(webpage_link);
	    while(!(websiteQueue.isEmpty()) && count < limitNumber){
	    	try{
	    		temp = websiteQueue.remove();
	    		searchedWebsites.add(temp);
	    		Document doc = Jsoup.connect(temp).timeout(3000).get();
	    		if(doc.text().contains(search.getText())){
	    			outputText += temp + "\n";
	    			foundCount++;
	    		}
	    		Elements links = doc.select("a[href]");
	    		for (Element link : links) {
	    			temp = link.attr("abs:href");
	    			if(websiteQueue.size() < limitNumber)
	    				if(!(temp.isEmpty()))
	    					if(!(websiteQueue.contains(temp)))
		    					if(!(searchedWebsites.contains(temp)))
		    						if(temp.charAt(temp.length() - 1) != '#')
		    							websiteQueue.add(temp);
	    		}
	    		count++;
	    	}catch(IOException e){
	    		e.getMessage();
	    	}
	    	System.out.println("link " + count + " - queue size " + websiteQueue.size());
	    	
	    }
	    long endTime = System.currentTimeMillis(); 
	    outputText += "End of search.\nFound " + foundCount + " matches in " + count + " links in " + (endTime - startTime) + " ms";  
	    out.setText(outputText);
	} 
	
	/*
	 * the SetLocations method sets the locations of the GUI items.
	 */
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
		size = startLabelError.getPreferredSize();
		startLabelError.setBounds(start.getX(),start.getY() + start.getHeight() + 5, size.width, size.height);	
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
		startLabelError.setVisible(false);
	}
	
	/*
	 * the goButtonListener listens for the go button to be pressed, then runs the search method
	 */
	private class goButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			search();
		}
	}
	
	/*
	 * the resetButtonListener listens for the reset button to be pressed, then clears the GUI of all information
	 */
	private class resetButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			start.setText("");
			search.setText("");
			limit.setText("");
			out.setText("");
			startLabelError.setVisible(false);
			limitLabelError.setVisible(false);
		}
	}
}
