package com.example.cody_.studentchat.Models;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import com.orm.SugarApp;
import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringReader;
import java.lang.reflect.Type;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Cody_ on 10/12/2017.
 */

public class StudyGroup extends SugarRecord {

    @Expose
    private String groupName;
    @Expose
    private String subject;
    @Expose
    private String startDate;
    @Expose
    private String startTime;
    @Expose
    private String latitude;
    @Expose
    private String longitude;
    @Expose
    private String groupAdmin;
    @Expose
    private String jsonGroupMemberList;

    @Ignore
    private ArrayList<String> groupMembers = new ArrayList<>();

    public StudyGroup(){}

    public StudyGroup(String groupAdmin, String groupName, LatLng location, String subject, String startDate, String startTime){

        this.groupAdmin = groupAdmin;
        this.groupName = groupName;
        this.latitude = String.valueOf(location.latitude);
        this.longitude = String.valueOf(location.longitude);
        this.subject = subject;
        this.startDate = startDate;
        this.startTime = startTime;
    }

    public void addGroupMember(User groupmember){
        if (this.groupMembers == null){
            getGroupMembers();
        }
        this.groupMembers.add(groupmember.getUsername());
    }

    public void removeGroupMember(User groupMember){
        if (this.groupMembers == null){
            getGroupMembers();
        }
        for (Iterator<String> iter = groupMembers.listIterator(); iter.hasNext();){
            String member = iter.next();
            if (member.equals(groupMember.getUsername())){
                this.groupMembers.remove(member);
                jsonGroupMemberList = new Gson().toJson(groupMembers, new TypeToken<List<User>>(){}.getType());
                return;
            }
        }
    }

    public void setLatitude(String lat){
        this.latitude = lat;
    }

    public void setLongitude(String lng){
        this.longitude = lng;
    }

    public String getGroupName(){return this.groupName;}

    public String getGroupAdmin(){return this.groupAdmin;}

    public String getSubject(){return this.subject;}

    public String getStartDate(){return this.startDate;}

    public String getStartTime(){return this.startTime;}

    public String getLatitude(){return this.latitude;}

    public String getLongitude(){return this.longitude;}

    public String getJsonGroupMemberList(){return this.jsonGroupMemberList;}

    public List<String> getGroupMembers(){
        return this.groupMembers;
    }

   public void setJsonGroupMemberList(String groupMembersJson){
       this.jsonGroupMemberList = groupMembersJson;
       try {
           JSONArray jsonArray = new JSONArray(jsonGroupMemberList);
           int length = jsonArray.length();
           groupMembers.clear();
           for (int i = 0; i < length; i++){
               JSONObject jsonObject = jsonArray.getJSONObject(i);
               String username = jsonObject.getString("username");
               groupMembers.add(username);
           }
       }catch(JSONException ex){
           ex.printStackTrace();
           try {
               // if we trip this far, then the json is of only username form instead of the entire object
               JSONArray jsonArray = new JSONArray(jsonGroupMemberList);
               int length = jsonArray.length();
               for (int i = 0; i < length; i++) {
                   String username = jsonArray.getString(i);
                   groupMembers.add(username);
               }
           }catch(JSONException error) {
               error.printStackTrace();
           }
       }
   }
    @Override
    public long save(){
        jsonGroupMemberList = new Gson().toJson(groupMembers, new TypeToken<List<User>>(){}.getType());
        return super.save();
    }
}
