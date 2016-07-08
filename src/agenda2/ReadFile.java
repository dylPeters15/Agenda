package agenda2;
import java.io.*;
//import java.net.URISyntaxException;
//import java.nio.file.Path;

//import javax.print.DocFlavor.URL;

public class ReadFile {
	
	private String path;
	
	public ReadFile(String file_path){
		path = file_path;
	}
	
	public String[] openFile() throws IOException {
		FileReader fr = new FileReader(path);
		BufferedReader textReader = new BufferedReader(fr);
		
		int numLines = readLines();
		String[] textData = new String[numLines];
		
		for (int i=0; i < numLines; i++){
			textData[i] = textReader.readLine();
		}
		textReader.close();
		return textData;
	}
	
	public int readLines() throws IOException {
		FileReader fileToRead = new FileReader(path);
		BufferedReader bf = new BufferedReader(fileToRead);
		
		//String aLine;
		int numLines = 0;
		
		while ((/*aLine = */bf.readLine()) != null) {
			numLines++;
		}
		bf.close();
		//System.out.println(aLine);
		return numLines;
		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//String filePath = this.PARENT_LOC + "/asdf.txt";
		
		/*java.net.URL theurl = ReadFile.class.getClassLoader().getResource("qwerty.txt");
	    //System.out.println(theurl.getPath());
	    
	    
	    String path = theurl.getPath();
		//path = "/Users/dylanpeters/Programming/Eclipse/Personal Java Workspace/CSVReadWrite/bin/qwerty.txt";
		//path = java.net.URLDecoder.decode(theurl.getPath());
		try {
		path = new java.net.URI(theurl.getPath()).getPath();
		//System.out.println("Yay" + path);
		} catch (URISyntaxException ex){
			
		}
		//System.out.println(path);
		ReadFile thing = new ReadFile(path);
		String[] text;
		try{
		text = thing.openFile();
		//System.out.println();
		//for (String fdsa: text){
			//System.out.println(fdsa);
		//}
		} catch (IOException asdf){
			
		}*/
		
	}

}
