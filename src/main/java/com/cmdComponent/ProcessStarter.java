package com.cmdComponent;


import java.util.concurrent.ConcurrentLinkedQueue;
import com.cmdComponent.Actor;
import com.cmdComponent.ThreadMessages;
import com.cmdComponent.CmdCaller;


/**
* This class is the main entry point into the component. It contains two constructors, 
* one for taking in an initial command for a process, which executes immediately, and a 
* default constructor that simply creates the object and does nothing until more information is needed
*
*Implements the Actor interface for a very simple message system (it can only receive one type of message)
*
*@author William Daniels
*/
public class ProcessStarter implements Actor {
	Actor middleMan;
	private ConcurrentLinkedQueue<Object> messageQueue;
	private String initialInput = null;
	public ProcessStarter() {
		this("");
	}
	public ProcessStarter(String initialInput) {
		middleMan = new MiddleMan(this);
		new Thread((MiddleMan)middleMan).start();
		this.initialInput = initialInput;
		messageQueue = new ConcurrentLinkedQueue<Object>();
		if (initialInput != "")
			middleMan.sendMsg(new ThreadMessages.SendCmdMessage(initialInput));
	
	}
	
	public void processNewCommand(String newCommand){
		if (newCommand.toLowerCase().equals("quit"))
			middleMan.sendMsg(ThreadMessages.Quit.getInstance());
		else
			middleMan.sendMsg(new ThreadMessages.SendCmdMessage(newCommand));

	}

	@Override
    public void sendMsg(Object msg) {
        messageQueue.add(msg);
    }
	public Object getNewOutput(){
		if (!messageQueue.isEmpty()){
			return messageQueue.poll();
		}
		else{
			return "";
		}
	}
}