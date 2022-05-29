package com.example.mqttkotlinsample.mqtt;

public class Message {

    public  String token;
    public  String message;

    public String name;
    public String moduleType;
    public String dataType;
    public  String bdType;
    public long bdIndex;
    public String UserLogin;
    public  String UserPwd;
    public String LoginStatus;
    public String Request;



    public int UID;
    public  static int sUID;
    public int status;
    public String data;


    public int OrderID;
    public int ChallengeNumbers;
    public String ChallengeInfo;
    //public String TypeGateway;
    public String Number;
    /*
     * <Module><BDType>Medicament</BDType><BDIndex>1</BDIndex></Module>
     * */

    public Message(String message,String token)  {
        Bebin( message, token);
    }

    public Message(String message)  {
        Bebin( message,"");
    }

    void Bebin(String message,String token)  {
        this.message = message;
        this.token = token;
        if(isModule()){
            name = ScanText("Name");
            moduleType = ScanText("moduleType");
            dataType = ScanText("DataType");
            UID = ScanInt("UID");
            sUID=UID;
            data = ScanText("Data");
            status=ScanInt("Status");
            bdType = ScanText("BDType");
            bdIndex = ScanLong("BDIndex");
            UserLogin = ScanText("UserLogin");
            UserPwd = ScanText("UserPwd");
            LoginStatus = ScanText("LoginStatus");
            Request= ScanText("Request");

            OrderID = ScanInt("OrderID");
             ChallengeNumbers = ScanInt("ChallengeNumbers");
             ChallengeInfo = ScanText("ChallengeInfo");
           // TypeGateway = ScanText("TypeGateway");
            Number = ScanText("Number");
        }
    }

    String ScanText(String param){
        try {
            return findText(message,param);
        }
        catch (Exception ex ){
            return null;
        }
    }

    int ScanInt(String param){
        try {
            if (param!=null)
                if(message.length()>0)
                    return  Integer.parseInt(findText(message,param));
                else return -1;
            else return -1;
        }
        catch (Exception ex ){
            return -1;
        }
    }

    long ScanLong(String param){
        try {
            if (param!=null)
                if(message.length()>0)  return  Long.parseLong(findText(message,param));
                else return -1L;
            else return -1L;
        }
        catch (Exception ex ){
            return -1L;
        }
    }


    public  boolean isModule(){
        try {
            if (message.length() > 0) {
                int find1 = message.indexOf("<Module>");
                int find2 = message.indexOf("</Module>");
                if(find2-find1>0) return  true;
                else  return false;
            }
            else return  false;
        }
        catch (Exception ex){
            return  false;
        }
    }

    public  boolean isLoginStatusReport(){
        try {
            if (this.isModule()) {

                if (this.LoginStatus.length()>1) return  true;
                else  return false;
            }
            else return  false;
        }
        catch (Exception ex){
            return  false;
        }
    }

    public String getDeviceName(){
        String[] subStr = token.split("/"); // Разделения строки str с помощью метода split()
        return subStr[subStr.length-1];
    }

    public static String GenerateToDevice(String Name,String data){
        String msg = "<Module><Name>"+Name+"</Name><UID>" + String.valueOf( sUID) +
                "</UID><ModuleType>all</ModuleType><Data>" + data +
                "</Data></Module>";
        return msg;
    }
    public static String InXML(String tag,String data){
        return "<"+tag+">"+data+"</"+tag+">";
    }

    static String findText(String str, String findContext)throws  Exception
    {
        int find1 = str.indexOf("<" + findContext + ">");
        int find2 = str.indexOf("</" + findContext + ">");
        String findStr = "";
        if (find1>=0 ||find2>0)
            findStr +=  str.substring(find1+ ("<" + findContext + ">").length(),find2);
//        for (int i = find1 + ("<" + findContext + ">").length(); i < find2; i++)
//        {
//            findStr += (char)str.getBytes()[i];
//        }

        return findStr;
    }
}
