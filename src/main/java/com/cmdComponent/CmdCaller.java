
package com.cmdComponent;
import java.io.IOException;
import java.io.Reader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.ArrayList;

/**
 */
public class CmdCaller {


    private String placeCmdCall(String key) throws IOException {        
        String output = "";
        try{
            Runtime runtime = Runtime.getRuntime();            
            final Process process = runtime.exec(key);
            
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
            return output;
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
