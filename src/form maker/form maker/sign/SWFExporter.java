/**
 * 
 */
package sign;

import java.awt.BorderLayout;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

/**
 * @author Martin Gerdzhev
 *
 */
public class SWFExporter
{
//TODO complete this.
	private ArrayList<SignLink> signList;
	private String pathToImages;
	private String movieName;
	private String htmlText;
	private JProgressBar progressBar;
	private JFrame exportFrame;
	public static int WIDTH = 400;
	public static int HEIGHT = 100;
	private JLabel label;
	private JButton doneButton;
	
	public SWFExporter(ArrayList<SignLink> signs, String path, String movieName, String htmlText)
	{
		//TODO fix the progressBar issue
		JPanel progressPanel = new JPanel();
		progressBar = new JProgressBar();
		progressBar.setIndeterminate(true);
		progressPanel.add(progressBar);
		label = new JLabel("Exporting in progress...");
		label.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		exportFrame = new JFrame("Exporting File...");
		exportFrame.setSize(WIDTH, HEIGHT);
		exportFrame.setIconImage(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/SignEd_icon_16.jpg")));
		exportFrame.setLocation((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2 - WIDTH / 2, (int) Toolkit.getDefaultToolkit()
				.getScreenSize().getHeight()
				/ 2 - HEIGHT / 2);
		exportFrame.getContentPane().add(progressPanel,BorderLayout.CENTER);
		exportFrame.getContentPane().add(label, BorderLayout.NORTH);
		
		doneButton = new JButton("Done");
		doneButton.addActionListener(new DoneListen());
		doneButton.setVisible(false);
		
		//TODO wait here for some time to let the export frame initialize properly
		exportFrame.getContentPane().add(doneButton,BorderLayout.SOUTH);
		exportFrame.setVisible(true);
		exportFrame.getContentPane().repaint();
		exportFrame.getContentPane().validate();
		exportFrame.validate();
		exportFrame.repaint();		
		
		this.htmlText = htmlText;
		signList = signs;
		pathToImages = path;
		this.movieName = movieName;
		this.createThumbnails();
		if (this.buildSWF("test.swf"))
		{
			label.setText("Build Successful");
		}
		else
		{
			label.setText("Build Failed!!!");
		}
		doneButton.setVisible(true);	
		exportFrame.getContentPane().repaint();
		//System.out.println("Deletion successful: "+ this.deleteDir(new File(pathToImages)));
		
	}
	
	private class DoneListen implements ActionListener
	{

		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent arg0)
		{
			// TODO Auto-generated method stub
			exportFrame.dispose();
		}
		
	}
	public boolean buildSWF(String filename)
	{
		//TODO put code here to call flex builder to build the code
		String asPath = new File(pathToImages).getParent()+File.separator;
		//System.out.println(asPath+"XMLData.as");
		this.writeAS(asPath+"XMLData.as");
		boolean build = FlexBuilder.buildSWF(asPath.replaceAll("\\\\", "/"), asPath.replaceAll("\\\\", "/")+movieName.substring(0, movieName.lastIndexOf("."))+".swf"); 
		System.out.println("\nBuild successful: "+build);
		//System.out.println("removal of XMLData.as: "+this.removeAS(asPath+"XMLData.as"));
		return build;
	}
	
	public boolean removeAS(String path)
	{
		//System.out.println(path);
		return new File(path).delete();
	}
	
	public void writeAS(String path)
	{
		htmlText = htmlText.replaceAll("\"", "'");
		htmlText = htmlText.replaceFirst("\n      ","");
		htmlText = htmlText.replaceAll("\n      ","<br>");
		htmlText = htmlText.replaceAll("\n    ","");
		htmlText = htmlText.replaceAll("\n","");
		htmlText = htmlText.replaceAll("<a", "<font color=\'#0000FF\'><u><a"); //to color the link blue and underline it
		htmlText = htmlText.replaceAll("</a>", "</a></u></font>"); //to color the link blue and underline it
		htmlText = htmlText.replaceAll("<p>", "");
		htmlText = htmlText.replaceAll("</p>", "");
		String temp = pathToImages;
		if (File.separator.equals("\\")) //escape the \ for passing to flash string
		{
			temp = pathToImages.replaceAll("\\\\","\\\\\\\\");
		}
		System.out.println(htmlText);
		try
		{
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(path)));
			out.println("package {"); //open package Declaration
			out.println("\tpublic class XMLData {"); //open class Declaration
			out.println("\t\tprivate var numLinks:int = "+signList.size()+";"); //initialized
			out.println("\t\tprivate var imagesArray:Array = new Array("+signList.size()+");");
			out.println("\t\tprivate var timesArray:Array = new Array("+(2*signList.size())+");");
			out.println("\t\tprivate var movieName:String = \""+movieName+"\";"); //initialized
			out.println("\t\tprivate var htmlText:String = " + "\""+htmlText+"\";"); //initialized
			out.println("\t\tprivate var linksArray:Array = new Array("+signList.size()+");");
			out.println("\t\tprivate var newWindowArray:Array = new Array("+signList.size()+");");
			out.println("\t\tprivate var labelsArray:Array = new Array("+signList.size()+");");
			out.println();
			for (int i=1;i<=signList.size();i++) //populate the embed statements
			{
				out.println("\t\t[Embed(source=\""+temp+i+".jpg\")]");
				out.println("\t\t[Bindable]");
				out.println("\t\tpublic var Image"+i+":Class;");
				out.println();
			}
			out.println("\t\tpublic function XMLData() {");
			out.println("\t\t\tinit();");
			out.println("\t\t}");
			out.println();
			out.println("\t\tpublic function init():void {"); //open init function
			for (int i=0;i<signList.size();i++) //populate the arrays
			{
				SignLink link = signList.get(i);
				out.println("\t\t\timagesArray["+i+"] = new Image"+(i+1)+"();"); //the bitmap array
				out.println("\t\t\ttimesArray["+(2*i)+"] = "+((float)link.getStartTime()/1000)+";"); //start time
				out.println("\t\t\ttimesArray["+(2*i+1)+"] = "+((float)link.getEndTime()/1000)+";"); //end time
				out.println("\t\t\tlinksArray["+i+"] = \"" + link.getLinkAddress()+"\";"); //links Array
				out.println("\t\t\tnewWindowArray["+i+"] = " + link.openInNewBrowserWindow()+";"); //newWindow Array
				out.println("\t\t\tlabelsArray["+i+"] = \"" + link.getLabel()+"\";"); //labels Array
				out.println();
			}
			out.println("\t\t}"); //close the init function
			out.println();
			out.println("\t\tpublic function getImagesArray():Array {"); //open getImagesArray
			out.println("\t\t\treturn imagesArray;");
			out.println("\t\t}"); //close getImagesArray
			out.println();
			out.println("\t\tpublic function getTimesArray():Array {"); //open getTimesArray
			out.println("\t\t\treturn timesArray;");
			out.println("\t\t}"); //close getTimesArray
			out.println();
			out.println("\t\tpublic function getLinksArray():Array {"); //open getLinksArray
			out.println("\t\t\treturn linksArray;");
			out.println("\t\t}"); //close getLinksArray
			out.println();
			out.println("\t\tpublic function getNewWindowArray():Array {"); //open getNewWindowArray
			out.println("\t\t\treturn newWindowArray;");
			out.println("\t\t}"); //close getNewWindowArray
			out.println();
			out.println("\t\tpublic function getLabelsArray():Array {"); //open getLabelsArray
			out.println("\t\t\treturn labelsArray;");
			out.println("\t\t}"); //close getLabelsArray
			out.println();
			out.println("\t\tpublic function getMovieName():String {"); //open getMovieName
			out.println("\t\t\treturn movieName;");
			out.println("\t\t}"); //close getMovieName
			out.println();
			out.println("\t\tpublic function getHtmlText():String {"); //open getHtmlText
			out.println("\t\t\treturn htmlText;");
			out.println("\t\t}"); //close getHtmlText
			out.println();
			out.println("\t\tpublic function getNumLinks():int {"); //open getNumLinks
			out.println("\t\t\treturn numLinks;");
			out.println("\t\t}"); //close getNumLinks
			out.println();
			out.println("\t}"); //close class Declaration
			out.println("}"); //close package Declaration
			out.close();
			
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	// Deletes all files and subdirectories under dir.
    // Returns true if all deletions were successful.
    // If a deletion fails, the method stops attempting to delete and returns false.
    public boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
    
        // The directory is now empty so delete it
        return dir.delete();
    }
	
	public void createThumbnails()
	{
		new File(pathToImages).mkdir(); //creates the directory
		for (int i=0;i<signList.size(); i++)
		{
			File output = new File(pathToImages,(i+1)+".jpg");
			BufferedImage bImage = new BufferedImage(signList.get(i).getPreview().getIconWidth(),signList.get(i).getPreview().getIconHeight(),BufferedImage.TYPE_INT_RGB);
			Graphics2D bufImageGraphics = bImage.createGraphics();
			  bufImageGraphics.drawImage(signList.get(i).getPreview().getImage(), 0, 0, null);
			try
			{
				ImageIO.write(bImage , "jpeg", output);
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				System.out.println("image export failed");
				e.printStackTrace();
			}
		}
	}
}
