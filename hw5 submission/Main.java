import java.util.List;
import java.util.ArrayList;
import java.lang.Thread;
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
		fileFiller thread1 = new fileFiller("boo");
		thread1.start();
		fileFiller thread2 = new fileFiller("hah");
		thread2.start();
		fileFiller thread3 = new fileFiller("pen");
		thread3.start();
		int i = 0;
		while (i < 3)
		{
			if (thread1.readyForReading()){
				System.out.println("first process output: \n" + thread1.outputFromProcess());
				i++;
			}
			if (thread2.readyForReading()){
				System.out.println("second process output: \n" + thread2.outputFromProcess());
				i++;
			}
			if (thread3.readyForReading()){
				System.out.println("third process output: \n" + thread1.outputFromProcess());
				i++;
			}

			Thread.sleep(100);
		}
		System.out.println("Done!");
	}
	
}