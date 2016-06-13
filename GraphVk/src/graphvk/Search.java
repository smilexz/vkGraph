/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphvk;

import static graphvk.GraphVk.fieldsZaprosa;
import static graphvk.GraphVk.paramsSearch;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
/**
 *
 * @author Smile
 */
public class Search {
   public static String urlApi = "https://api.vk.com/method/";
   static String methodUusersSearch = "users.search?";
   static String methodFriendsGet = "friends.get?";
   static String accessToken = "02862403d14bddcdf6817b0af0c517ba05e74389e2fab6fb7610a546a239df542aec647ea8d45dfc67f02";
   static String pathOne = "D:\\vkGraph\\gitVkGraph\\graph.csv";
   public static  ArrayList<String> userListSearch1 = new ArrayList<>();
   public ArrayList<String> zaprosSearch () throws IOException{
   String strUrl = "";
   String resultSeachJson;
   strUrl  =  urlApi + methodUusersSearch
           + paramsSearch + "&count=1000" + "&access_token=" + accessToken;
    File file = new File(pathOne); 
    //Если требуемого файла не существует.
    if(!file.exists()) {
       //Создаем его.
       file.createNewFile();
    }
    URL url = new URL(strUrl); // запрос к API VK UserSearch
    BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
    resultSeachJson = reader.readLine();
    reader.close();  
    JSONParser parser = new JSONParser(); // парсинг ответа Json 
       try {
           Object objS = parser.parse(resultSeachJson);
           JSONObject jsonSObject = (JSONObject) objS;
           JSONArray listResult = (JSONArray) jsonSObject.get("response"); 
           JSONObject jsonSObj;
           for (int i = 1; i < listResult.size() - 1; i++) {
               jsonSObj = (JSONObject) listResult.get(i);
               userListSearch1.add(""+ jsonSObj.get("uid"));
           } 
       } catch (Exception e) {
          System.out.println(" Search ошибка");
       }  
        return userListSearch1;
     }
   
   /**
   * метод получение друзей user
   * @param string userId
   *    Уникальный номер пользователя
   * @param string params
   *    Поля которые нужно дополнительнополучить
   */
public void getUserFriend(String userId, String params) throws IOException{
    String resultFriendJson = null;
    String strFriend = urlApi +
            methodFriendsGet
            + "user_id="
            +userId
            +params;
    URL urlFriend = new URL(strFriend);
    try (BufferedReader readerOne = new BufferedReader(new InputStreamReader(urlFriend.openStream()))) {
        resultFriendJson = readerOne.readLine();  
}
 catch(IOException ex){           
    System.out.println(ex.getMessage());
}
//парсинг ответа Json
JSONParser parser = new JSONParser();
   try {
       Object objF = parser.parse(resultFriendJson);
       JSONObject jsonFriendObject = (JSONObject) objF;
       JSONArray listResultF = (JSONArray) jsonFriendObject.get("response"); 
       if (listResultF == null)
       {
            try {
                 Thread.sleep(2000);                 
             } catch(InterruptedException ex) {
                 Thread.currentThread().interrupt();
             }
            getUserFriend(userId, params);
       }
   JSONObject jsonFriendObj;
   String f; //проход по users
   FileWriter fo = new FileWriter(pathOne, true);
try {	
    Integer countFriends = 0;
    Boolean flagZapis = false;
    for (int i = 0; i < listResultF.size(); i++) {
       flagZapis = false;
       jsonFriendObj = (JSONObject) listResultF.get(i);   
       for(Map.Entry<String, String> item : fieldsZaprosa.entrySet()) {
           f = "" + jsonFriendObj.get(item.getKey());
           if(item.getKey().equals("universities")) {
               if(!f.equals("null")){
               JSONArray arrUniversiti = (JSONArray) jsonFriendObj.get("universities"); 
                    if(!arrUniversiti.isEmpty()){
                     JSONObject jsonUniverObj;
                     for (int j = 0; j < arrUniversiti.size(); j++) {
                           jsonUniverObj = (JSONObject) arrUniversiti.get(j);
                           String idUniver;
                           idUniver = "" + jsonUniverObj.get("id");
                           if (!idUniver.equals(item.getValue())) { // если универ подходит, до создаем связь
                               flagZapis = true;
                           }
                           else {flagZapis = false; break;}
                         }
                     } else {flagZapis = true;}      
               } else {flagZapis = true;break;}
               continue;
           }
           if (flagZapis == true){break;}
           if(!f.equals(item.getValue())) {
               flagZapis = true; 
               break;
           }
     }
       if (flagZapis == false) {
           countFriends++;
           fo.write(userId+";"+jsonFriendObj.get("uid")+"\r\n");
           checkAndAddUser("" + jsonFriendObj.get("uid"));                  
       }
  }
if (countFriends == 0) { 
  fo.write(userId+"\r\n");
}
    }
         catch (IOException ex) {
                // handle exception
        }
   finally { // закрытие ресурсов обязательно в finally Оба вызова обязательно в отдельных try-catch 
            try {
                fo.flush();
                fo.close();
            } catch (IOException ex) {
            }		
    }
  } catch (Exception e) {
       System.out.println(userId+" ошибка");
   }   
}
   
/**
 * 
 * доавление в ощий массив id по которым потом будем делать запросы
 * @param string uidUser
 *  Уникальный айди пользователя
 */ 
public void checkAndAddUser(String uidUser){
    if(!userListSearch1.contains(uidUser)) {
        userListSearch1.add(uidUser);
    }
}  
}