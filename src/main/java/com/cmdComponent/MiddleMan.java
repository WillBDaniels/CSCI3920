package com.cmdComponent;

import com.cmdComponent.CmdCaller;
import com.cmdComponent.ProcessStarter;
import com.cmdComponent.Actor;
import com.cmdComponent.ThreadMessages;

import java.util.concurrent.ConcurrentLinkedQueue;


/**
* This class is to properly hold all of the message handling betwen the main output loop and the base-level commands. 
* This implements both Runnable and Actor, for Threading and message passing, respectively. 
* NOTE: You'll notice the only actor it has is for the process Starter. This is because although it does create threads
* with a message hidden in the contstructor, that's the only message ever need from this direction to the cmdCaller
* all other message interactions are from cmdCaller > MiddleMan. this minimizes risk of overlap and deadlock. 
*
*@author William Daniels
*/
public class MiddleMan  implements Actor, Runnable{
	// A build in linked queue for message storing. 
	private ConcurrentLinkedQueue<Object> messageQueue;
    private static final int SLEEP_TIME = 100;
    private Actor processStarter;

    private boolean done = false;
    //Just an empty private default constructor, don't want it to be available. 
    private MiddleMan(){

    }

    //The constructor, want to make sure we set the actor and initialize the queue. 
	public MiddleMan(Actor processStarter){
		this.processStarter = processStarter;
		messageQueue = new ConcurrentLinkedQueue<Object>();
	}

	public void run(){
		//Main message handling loop, runs until a Quit message is achieved. 
		while(!done) {
                dispatchMessageQueue();
                try{
                	Thread.sleep(SLEEP_TIME);
        	}catch(InterruptedException e){
        		e.printStackTrace();
        	}
        }
	}
	//handles messages in the queue, does nothing if the queue is empty. 
	 private void dispatchMessageQueue() {
        Object msg = messageQueue.poll();
        while(msg != null) {
            dispatchMessage(msg);
            msg = messageQueue.poll();
        }
    }
      /**
     * Dispatches on the type of the received message
     */
    private void dispatchMessage(Object msg) {
    	if (msg instanceof ThreadMessages.ReturnCmdMessage){
    		ThreadMessages.ReturnCmdMessage m = 
    			(ThreadMessages.ReturnCmdMessage) msg; 

    		processStarter.sendMsg(m.returnOutput);
    	}
    	if (msg instanceof ThreadMessages.SendCmdMessage){
    		ThreadMessages.SendCmdMessage m = 
    			(ThreadMessages.SendCmdMessage) msg;
    		new CmdCaller(m.newCommand, this).start();
    	}
    	if (msg instanceof ThreadMessages.Quit){
    		done = true;
    	}
    }
    //This method is simply for adding messages to the queue, for other methods to send IT messages. 
    @Override
    public void sendMsg(Object msg) {
        messageQueue.add(msg);
    }
}