import javax.swing.*;
import java.io.File;
import java.io.PrintWriter;

/**
 * DLCManager handles the DLC-Files. Which are needed to save downloads for a later purpose
 *
 * @author R3DST0RM
 * @version 0.1
 */
public class DLCManager {
    private FreshUI freshUI;

    public DLCManager(FreshUI freshUI){
        this.freshUI = freshUI;
    }

    public void ExportDLC(){
        ExportDialog exDia = new ExportDialog();
        String path = exDia.askSave();
        if(writeLinksToFile(path)){
            JOptionPane.showMessageDialog(freshUI, "Successfully exported current download list",
                    "Download list exported", JOptionPane.INFORMATION_MESSAGE);
        }else{
            // Print error
        }
    }

    public String[] ImportDLC(){

        return null;
    }

    private boolean writeLinksToFile(String filePath){
        try {
            File dlc = new File(filePath);
            PrintWriter out = new PrintWriter(dlc);
            for (int i = 0; i < freshUI.dTableModel.getRowCount(); i++) {
                out.println(freshUI.dTableModel.getValueAt(i, 0));
            }
            out.close();
            return true;
        }catch (Exception e){
            System.err.println("Error while saving DLC");
            return false;
        }
    }
}

class ImportDialog extends JDialog {

}

class ExportDialog extends JDialog {

    public ExportDialog(){

    }

    public String askSave(){
        return "";
    }
}
