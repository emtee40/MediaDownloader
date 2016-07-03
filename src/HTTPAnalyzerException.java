/**
 * Simple HTTPAnalyzerException class
 *
 * Will be used in order to describe occured errors inside HTTPAnalyzer more detailed.
 */
public class HTTPAnalyzerException extends Exception {

    public HTTPAnalyzerException(String sMessage){
        super(sMessage);
    }
    public HTTPAnalyzerException(String sMessage, Throwable tCause){
        super(sMessage, tCause);
    }

    public HTTPAnalyzerException(String sMessage, Throwable tCause, boolean bEnableSuppression, boolean bWritableStackTrace){
        super(sMessage, tCause, bEnableSuppression, bWritableStackTrace);
    }
}
