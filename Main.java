import java.util.List;
import java.util.ArrayList;
import com.cmdComponent.ProcessStarter;

/**
* This class is simply for showing an example of how this particular compnent can be used. Absolutely
* nothing inside of the processStarter itself is relient upon any of this code in any way. 
* NOTE:: this example is built for a windows machine, if you want linux or mac support, please edit the 
* appropriate process input command to match your evironment. 
*
*@author William Daniels
*/
public class Main{
	public static void main(String [ ] args) throws InterruptedException{
		//Build the component. 
		ProcessStarter myProcess = new ProcessStarter();
		myProcess.processNewCommand("FileRenamer.bat boobies test.txt iWin.txt");

		//An array list to hold all of the outputs.
		System.out.println("About to fill the array, or somethin.");
		ArrayList<String> test = new ArrayList<String>();
		String temp = "";
		while (test.size() < 2){
			temp = (String)myProcess.getNewOutput();
			if (!temp.equals(""))
				test.add(temp);
		}
		//print out all of them at once. 
		System.out.println("Printing output from all threads and finishing" + test);
		//send the quit message! we're done :) 
		myProcess.processNewCommand("Quit");
	}
}