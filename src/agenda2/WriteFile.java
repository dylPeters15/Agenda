package agenda2;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class WriteFile {
	
	private String path;
	private boolean append_to_file = false;
	
	public WriteFile(String file_path){
		path = file_path;
	}
	
	public WriteFile(String file_path, boolean append){
		path = file_path;
		append_to_file = append;
	}
	
	public void writeToFile(String textLine) throws IOException {
		FileWriter write = new FileWriter(path, append_to_file);
		PrintWriter print = new PrintWriter(write);
		print.printf("%s" + "%n", textLine);
		print.close();
		System.out.println("just wrote to file");
	}
	
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		/*java.net.URL theurl = ReadFile.class.getClassLoader().getResource("qwerty.txt");
	    System.out.println(theurl.getPath());
	    
	    
	    String path = theurl.getPath();
		//path = "/Users/dylanpeters/Programming/Eclipse/Personal Java Workspace/CSVReadWrite/bin/qwerty.txt";
		//path = java.net.URLDecoder.decode(theurl.getPath());
		try {
		path = new java.net.URI(theurl.getPath()).getPath();
		System.out.println("Yay" + path);
		} catch (URISyntaxException ex){
			
		}
		System.out.println(path);
		
		String file_name = path;
		
		try {
			ReadFile file = new ReadFile(file_name);
			String[] aryLines = file.openFile();
			int i;
			for (i= 0; i < aryLines.length; i++){
				System.out.println(aryLines[i]);
			}
		} catch (IOException ex){
			System.out.println("didn't work");
		}
		
		try {
			WriteFile data = new WriteFile(file_name, true);
			data.writeToFile("\nnew line of text");
			
		} catch (IOException ex){
			System.out.println("didn't work again");
		}*/

	}

}