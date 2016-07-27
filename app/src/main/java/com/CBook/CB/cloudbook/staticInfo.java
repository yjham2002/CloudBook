package com.CBook.CB.cloudbook;

import java.util.ArrayList;

public class staticInfo {

    public static String URL_UPLOAD_ART = "http://yjham2002.woobi.co.kr/cb_directory/insertArticles.php";
    public static String URL_GET_DETAIL = "http://yjham2002.woobi.co.kr/cb_directory/getDetail.php";

    private static String group="Undefined", name="Undefined", number="Undefined", id="Undefined", pw="Undefined", expl="Undefined";
    public static String netID, netPW, netNM, netEM, netCT, netPR, error, caller;
    public static double latitude=0.0, longitude=0.0;
    private static int idx;
    public static int whoscall = 0;
    public static ArrayList<String> spinItems;
    public static boolean push_msg = true, push_req = true;
    public static void setAuthInfo(String idp, String pwp)
    {
        id=idp; pw=pwp;
    }
    public static void setSharingInfo(String groupP, String nameP, String numberP, String explp, int idex)
    {
        group = groupP; name = nameP; number = numberP; expl = explp; idx = idex;
    }
    public static String distance(double lat1, double lon1, double lat2, double lon2) {
        if(lat1 == 0.0 || lon1 == 0.0 || lat2 == 0.0 || lon2 == 0.0) return "거리 정보 없음";
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344 * 100.0;
        int temp = (int)dist;
        dist = (double)temp / 100.0;
        return Double.toString(dist)+"km";
    }
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

    public static String DetalId, DetailDate, DetailContent, DetailTaker, DetailNumber;
    public static int DetailIndex, DetailTaken;
    public static int contactMode = 0;
    public static int detail = -1;
    public static int isRun = 0;
    public static ListViewAdapterM mAdapter;
    public static ListViewAdapterM mAdaptergo;
    public static ListViewAdapterM mAdapterMe;
    public static int async = 0;
    public static int auth = 0;
    public static int getIdx() {return idx;}
    public static String getID() {return id;}
    public static String getPW() {return pw;}
    public static String getGroup() {return group;}
    public static String getName()
    {
        return name;
    }
    public static String getNumber()
    {
        return number;
    }
    public static String getExpl() {return expl;}
}
