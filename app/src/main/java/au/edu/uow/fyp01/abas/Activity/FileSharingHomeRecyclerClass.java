package au.edu.uow.fyp01.abas.Activity;

/**
 * Created by Manish on 18/05/2018.
 */

public class FileSharingHomeRecyclerClass {

    private String Date_Expire;
    private String Time_Expire;
    private String File_Name;
    private String File_Type;
    private String ID;
    private String Receiver;
    private String Sender;
    private String Link;

    public FileSharingHomeRecyclerClass() {

    }

    public FileSharingHomeRecyclerClass(String date_Expire, String time_Expire, String file_Name, String file_Type, String ID, String receiver, String sender, String link) {
        Date_Expire = date_Expire;
        Time_Expire = time_Expire;
        File_Name = file_Name;
        File_Type = file_Type;
        this.ID = ID;
        Receiver = receiver;
        Sender = sender;
        Link = link;
    }

    public String getDate_Expire() {
        return Date_Expire;
    }

    public void setDate_Expire(String date_Expire) {
        Date_Expire = date_Expire;
    }

    public String getTime_Expire() {
        return Time_Expire;
    }

    public void setTime_Expire(String time_Expire) {
        Time_Expire = time_Expire;
    }

    public String getFile_Name() {
        return File_Name;
    }

    public void setFile_Name(String file_Name) {
        File_Name = file_Name;
    }

    public String getFile_Type() {
        return File_Type;
    }

    public void setFile_Type(String file_Type) {
        File_Type = file_Type;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getReceiver() {
        return Receiver;
    }

    public void setReceiver(String receiver) {
        Receiver = receiver;
    }

    public String getSender() {
        return Sender;
    }

    public void setSender(String sender) {
        Sender = sender;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }
}
