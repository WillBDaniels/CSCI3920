
package com.cmdComponent;

import java.io.IOException;
import java.io.Reader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import com.cmdComponent.Actor;
import com.cmdComponent.ThreadMessages;

/**
* THis class is where all of the meat and potatoes of the program lies. 
* It takes in an input string as part of the constructor, as well as an instance
* of a class that implements the Actor interface, and then it builds a process based 
* off of the input string. Although it does guard against many types of invalid commands
* that would cause the program to crash, there is still a small 'bug' (I use the term loosely
* since it's sort of advanced user error) where is you use the format of the string 
* (name of process root-directory initialInput) and you give an error with one of the 
* FIRST TWO parameters, there is a solid chance that the program will crash. I can't account
* for every possiblity without some extreme measues, so, I'm leaving it in. 
*
*
*@author William Daniels
*
*
*/
public class CmdCaller extends Thread {
    private final String inputString;
    private Actor middleMan;

    public CmdCaller(final String inputString, Actor middleMan){
        this.middleMan = middleMan;
        this.inputString = inputString;
    }

    public void run() {
        String output = "";
        String fileSeperator = System.getProperty("file.separator");
        
        //This check is to try and filter out a lot of invalid types of processes.
        //The main exception this does not catch is noted above.  
        if (((inputString.substring(0, 3).contains(fileSeperator))) || (!(inputString.contains(" ")))){
            String temp = (inputString.substring(0, inputString.length()));
            File myFile = new File(temp);
            if (!myFile.exists()){
                middleMan.sendMsg(new ThreadMessages.ReturnCmdMessage("Invalid process path"));
                return;
            }
        }
        try{
            Runtime runtime = Runtime.getRuntime();            
            final Process process = runtime.exec(inputString);
            
            //THIS line is the magic to avoid the deadlock condition entirely, although the whole method contributes. 
            //Without this, there is absolutely no hope in stopping it, as the buffer on Windows XP (and possible 7)
            //WILL overflow and cause the thread to deadlock, DO NOT REMOVE OR CHANGE.
            process.getOutputStream().close();
            
            //gobble up the other streams concurrently to further avoid buffer overflow. 
            StreamGobbler outputGobbler = new 
                StreamGobbler(process.getInputStream());
            StreamGobbler errorGobbler = new 
                StreamGobbler(process.getErrorStream());            

            // kick them off
            outputGobbler.start();
            errorGobbler.start();

            //wait for it to finish. 
            process.waitFor();
            //return the output, add it to the final string.

            output = outputGobbler.returnOutput();
            output = output + errorGobbler.returnOutput();
        }catch(IOException e){
            e.printStackTrace();
        }
        catch(Throwable t){
            t.printStackTrace();
        }
        finally{
            middleMan.sendMsg(new ThreadMessages.ReturnCmdMessage(output));
        }
    }

    /**
     * Class to read string from thread via input string and buffered reader
     *
     * @author William Daniels
     */
    class StreamGobbler extends Thread{
        InputStream is;
        String type;
        String output = "";
        Thread thread;
        
        //build the stream. 
        StreamGobbler(InputStream is)
        {
            this.is = is;
        }
        
        /**
        * standard overriden run() method from exending Thread. 
        */
        @Override
        public void run()
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = "";
            try {
                //while there is still input in the stream, build an output stream out of it. 
                while((line = br.readLine()) != null){
                    output = output + "\r\n" + line;
                }
            }catch (IOException ioe){
                    ioe.printStackTrace();  
            }
        }
      
        public String returnOutput(){
            return output;
        }
    }
}
