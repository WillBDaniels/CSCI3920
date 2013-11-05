package com.cmdComponent;
import java.io.File;
import java.util.List;
import java.util.ArrayList;

/**
 * Container class for the various classes of (immutable!) messages that can be
 * sent between our threads.
 */
public class ThreadMessages {
    /**
    * A message for starting the network operations
    */
    public static class ReturnCmdMessage {
        public final String returnOutput;
        public ReturnCmdMessage(final String returnOutput){
            
            this.returnOutput = returnOutput;
        }
    }
    public static class SendCmdMessage {
        public final String newCommand; 
        public SendCmdMessage(final String newCommand){
            this.newCommand = newCommand;
        }
    }
     /**
     * A request for the receiver to terminate.
     * To get an instance, call <code>getInstance()</code>.
     */
    public static class Quit {
        private static Quit instance = null;

        private Quit() {
        }

        public static Quit getInstance() {
            if(instance == null)
                instance = new Quit();
            return instance;
        }
    }
}
