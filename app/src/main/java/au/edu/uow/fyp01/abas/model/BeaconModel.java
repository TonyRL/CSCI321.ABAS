package au.edu.uow.fyp01.abas.model;

/**
 * Created by Athens on 2018/06/10.
 */

public class BeaconModel {
    String beaconID;
    String classID;
    String schID;
    String sid;

    public BeaconModel(){}

    public BeaconModel(String beaconID, String classID, String schID, String sid){
        this.beaconID = beaconID;
        this.classID = classID;
        this.schID = schID;
        this.sid = sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getSid() {
        return sid;
    }

    public void setSchID(String schID) {
        this.schID = schID;
    }

    public String getSchID() {
        return schID;
    }

    public void setClassID(String classID) {
        this.classID = classID;
    }

    public String getClassID() {
        return classID;
    }

    public void setBeaconID(String beaconID) {
        this.beaconID = beaconID;
    }

    public String getBeaconID() {
        return beaconID;
    }

}
