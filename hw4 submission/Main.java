import java.util.List;
import java.util.ArrayList;
import com.cmdComponent.ProcessStarter;
import java.lang.System;

/**
* This class is simply for showing an example of how this particular compnent can be used. Absolutely
* This program works in concert with the batch file FileRenamer.bat. Essentially it simply fills an array
* with tags, then, in a loop, shoves them to our process starter, which then places the return output in a queue. 
* The program then displays all the output of the files, in order, and says which file they came from. Obviously this 
* is the en-US.dic file, but.. yea. If the progra waits more than 10 seconds for output, it quits. 
* You might notice this program doesn't check to see if we're in the PWD and have write privelages. Well, true, but if we 
* don't, the program will simply spit back the error message, it'll display on the screen 5 times (once per tag) and then you'll
* know to move somewhere you have permissions. no big deal really. 
*
*@author William Daniels
*/
public class Main{
	public static void main(String [ ] args) throws InterruptedException{
		buildAndDispayFiles();
	}

	public static void buildAndDispayFiles(){
		//Build the component.
		String temp;
		boolean firstElementAchieved = false;
		double startTime = 0.0; //Some variables for holding time. 
		double resultTimeTaken = 0.0;
		ArrayList<String> tagList = new ArrayList<String>(); //An array for our tag list, feel free to add to this if you want different examples. 
		tagList.add("dra");
		tagList.add("bre");
		tagList.add("boo");
		tagList.add("how");
		tagList.add("prod");

		String output = ""; 
		//create the initial process. We could give it a string to start off with, but what's the point?
		//it just makes the program a little choppier. 
		ProcessStarter myProcess = new ProcessStarter();
		for (int i = 0; i < tagList.size(); i++){
			//build the string we're going to pass in, we could read in from any fil we want. doesn't matter.
			output = "FileRenamer.bat " +tagList.get(i) + " en-US.dic";
			//actually submit the process.
			myProcess.processNewCommand(output); 
		}
		//An array list to hold all of the outputs.
		System.out.println("About to fill the array, or somethin.");
		ArrayList<String> test = new ArrayList<String>();

		//get the start time so we can make a little self-inflicted timeout. 
		startTime = System.currentTimeMillis();
		do {
			//check for new input. 
			temp = (String)myProcess.getNewOutput();
			if (!temp.equals("")){
				if (!firstElementAchieved){
					//this boolean is to give the processbuilder enough time to do it's thing. 
					firstElementAchieved = true;
				}
				test.add(temp);
			}
			resultTimeTaken = (System.currentTimeMillis() - startTime);
		} while((temp != "" || (firstElementAchieved == false)) && ((resultTimeTaken / 1000) < 10)); 
		//This above seems like nonsense, but it just makes sure that the program has waited more than 10 seconds for a response.
		//print out all of them at once. 
		System.out.println("Printing output from all threads and finishing" + test);
		//send the quit message! we're done :) 
		myProcess.processNewCommand("Quit");
	}
}