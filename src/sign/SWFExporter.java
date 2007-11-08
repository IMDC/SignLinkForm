/**
 * 
 */
package sign;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.imageio.ImageIO;

/**
 * @author Martin Gerdzhev
 * 
 * @version $Id: SWFExporter.java 52 2007-11-08 03:56:17Z martin $
 *
 */
public class SWFExporter
{
	private ArrayList<Sign> signList;
	private String pathToImages;
	private String outputFolder;
	private String movieName;
	private String htmlText;
	public static int WIDTH = 400;
	public static int HEIGHT = 100;
	
	public SWFExporter(ArrayList<Sign> signs, String path,String outputFolder, String movieName, String htmlText)
	{
		
		this.outputFolder = outputFolder + File.separator;
		this.htmlText = htmlText;
		signList = signs;
		pathToImages = path;
		this.movieName = movieName;
		System.out.println("Movie name: " +this.movieName);
	}
	public boolean export()
	{
		boolean flag = false;
		this.createThumbnails();
		if (this.buildSWF(movieName))
		{
			flag = true;
		}
		System.out.println("Deletion successful: "+ SignUtils.deleteDir(new File(pathToImages)));
		return flag;
	}
	
	private boolean buildSWF(String filename)
	{
		String asPath = SignUtils.FLEXFOLDER+File.separator;
		this.writeAS(asPath+"XMLData.as");
		FlexBuilder flex = new FlexBuilder();
		boolean build = flex.buildSWF(asPath.replaceAll("\\\\", "/"), outputFolder.replaceAll("\\\\", "/")+movieName.substring(0, movieName.lastIndexOf("."))+".swf"); 
		System.out.println("\nBuild successful: "+build);
		System.out.println("removal of XMLData.as: "+this.removeAS(asPath+"XMLData.as"));
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
				Sign link = signList.get(i);
				out.println("\t\t\timagesArray["+i+"] = new Image"+(i+1)+"();"); //the bitmap array
				out.println("\t\t\ttimesArray["+(2*i)+"] = "+((float)link.getMStart()/1000)+";"); //start time
				out.println("\t\t\ttimesArray["+(2*i+1)+"] = "+((float)link.getMEnd()/1000)+";"); //end time
				out.println("\t\t\tlinksArray["+i+"] = \"" + link.getUrl()+"\";"); //links Array
				out.println("\t\t\tnewWindowArray["+i+"] = " + link.isNewWindow()+";"); //newWindow Array
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
