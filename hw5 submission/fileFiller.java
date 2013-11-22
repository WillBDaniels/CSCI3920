import java.util.List;
import java.util.ArrayList;
import java.lang.Thread;
import com.cmdComponent.ProcessStarter;
import java.lang.System;

public class fileFiller extends Thread
{
	private String tag = "";
	private ArrayList<String> outputInfo = new ArrayList<>();
	private boolean readyForReading = false;
	private fileFiller()
	{
		//just to make sure they don't use the default constructor. none of that. 
	}
	public fileFiller(String tag)
	{
		this.tag = tag;
	}

	public void run() 
	{
		buildFile();
	}
	private void buildFile(){
		//Build the component.
		String temp;
		boolean firstElementAchieved = false;
		double startTime = 0.0; //Some variables for holding time. 
		double resultTimeTaken = 0.0;

		String output = ""; 
		//create the initial process. We could give it a string to start off with, but what's the point?
		//it just makes the program a little choppier. 
		ProcessStarter myProcess = new ProcessStarter();
		//build the string we're going to pass in, we could read in from any fil we want. doesn't matter.
		output = "FileRenamer.bat " +tag + " en-US.dic";
		//actually submit the process.
		myProcess.processNewCommand(output); 
		//An array list to hold all of the outputs.
		System.out.println("About to fill the array, or somethin.");

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
				outputInfo.add(temp);
			}
			resultTimeTaken = (System.currentTimeMillis() - startTime);
		} while((temp != "" || (firstElementAchieved == false)) && ((resultTimeTaken / 1000) < 10)); 
		//This above seems like nonsense, but it just makes sure that the program has waited more than 10 seconds for a response.
		//print out all of them at once. 
		//send the quit message! we're done :) 
		setReadyForReading(true);
		myProcess.processNewCommand("Quit");
	}
	public boolean readyForReading(){
		return readyForReading;
	}
	private void setReadyForReading(boolean ready)
	{
		readyForReading = true;
	}
	public ArrayList<String> outputFromProcess()
	{
		if (readyForReading)
		{
			return outputInfo;
		}
		return null;
	}
}