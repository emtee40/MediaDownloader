/**
 * Created by dominik on 03.07.2016
 *
 * HTTPAnalyzerCode
 *
 * Is used to return the webcode and provide a custom error message if needed.
 */
public class HTTPAnalyzerCode {
    private boolean bSuccess;
    private String sCustomMesage;
    private int iRetCode;

    /*
        Since not every status code can be associated with a successful execution therefore it should be possible
        to create an object without to define it whether it was successful or not.
        (Should it be always false or always true then??????)
    */

    public HTTPAnalyzerCode(int iRetCode){
        this.iRetCode = iRetCode;
    }

    public HTTPAnalyzerCode(int iRetCode, boolean bSuccess){
        this.iRetCode = iRetCode;
        this.bSuccess = bSuccess;
    }

    public HTTPAnalyzerCode(int iRetCode, String sCustomMessage, boolean bSuccess){
        this.iRetCode = iRetCode;
        this.sCustomMesage = sCustomMessage;
        this.bSuccess = bSuccess;
    }


    // Public Methods

    // Maybe define a better "No details available" message
    public String getMessage(){ if(sCustomMesage != null) return sCustomMesage; else return "No details available"; }

    public int getCode(){ return iRetCode; }

    public boolean isRequestSuccessful() { return bSuccess; }

    // Private Methods

}
