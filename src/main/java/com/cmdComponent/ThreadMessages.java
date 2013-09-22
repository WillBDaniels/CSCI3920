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
     * Sent to the background thread by the GUI to set the
     * toll free number and access code.
     */
    public static class SetTollFreeAndAccessCode {
        public final String tollFree;
        public final String accessCode;

        public SetTollFreeAndAccessCode(final String tollFree,
                                        final String accessCode) {
            this.tollFree = tollFree;
            this.accessCode = accessCode;
        }
    }

    /**
    * Sent to the GUI thread to indicate progress for zipping.
    * the files.
    */
    public static class CurrentProgress {
        public final double progress;

        public CurrentProgress(double progress){
            this.progress = progress;
        }

    }
    /**
    * Sent to the GUI thread to indicate progress for networking. 
    */
    public static class CurrentNetworkProgress {
        public final double progress;
        
        public CurrentNetworkProgress(double progress){
            this.progress = progress;
        }

    }

    public static class CantCreateFile {
        public static enum Reason {
            TOO_BIG, UNKNOWN
        }

        public final Reason reason;

        public CantCreateFile(Reason reason) {
            this.reason = reason;
        }
    }

    public static class FailureToUpload {
        public final String message;

        public FailureToUpload(String message) {
            this.message = message;
        }
    }

    /**
     * Sent to the background thread by the GUI to indicate additional files
     * to zip.
     */
    public static class AdditionalFiles {
        private final List<File> files;

        /**
         * Constructor which copies the input list and stores it
         * internally.
         * @param files Additional files for the background task to zip.
         */
        public AdditionalFiles(List<File> files) {
            this.files = new ArrayList<File>(files);
        }

        /**
         * Gets a copy of the internal list of files.
         */
        public List<File> getFiles() {
            return new ArrayList<File>(files);
        }
    }

    public static class UploadProgressBar {
        private static UploadProgressBar instance = null;

        private UploadProgressBar(){
        }

        public static UploadProgressBar getInstance(){
            if (instance == null)
                instance = new UploadProgressBar();
            return instance;
        }

    }
    /**
     * Once we're no longer sending additional files, the GUI needs to
     * indicate that the backend should finish writing its zip file.
     *
     * To get an instance, call <code>getInstance()</code>.
     */

    public static class FinishFileZipper {
        private static FinishFileZipper instance = null;

        private FinishFileZipper() {
        }

        public static FinishFileZipper getInstance() {
            if(instance == null)
                instance = new FinishFileZipper();
            return instance;
        }
    }

    public static class UploadZipFile{
        private static UploadZipFile instance = null;

        private UploadZipFile(){
        }
        public static UploadZipFile getInstance(){
            if(instance == null)
                instance = new UploadZipFile();
            return instance;
        }
    }

    public static class FileMoveAction {
        public final File path;

        public FileMoveAction(File path) {
            this.path = path;

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

    /**
    * A message for starting the network operations
    */
    public static class StartNetworkTests {
        private static StartNetworkTests instance = null;

        private StartNetworkTests(){
        }
        public static StartNetworkTests getInstance() {
            if (instance == null)
                instance = new StartNetworkTests();
            return instance;
        }
    }
}
