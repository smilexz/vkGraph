/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphvk;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * 
 */
public class GraphVk {
    public static String paramsSearch;
    public static  Map<String, String> fieldsZaprosa;
   /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
public static void main(String[] args) throws IOException {
   fieldsZaprosa = new HashMap<>();
    String resultScan, fields;
    paramsSearch = "";
    fields = "&fields=";
    Scanner filter = new Scanner(System.in, "cp1251");
    System.out.println("Ввод фильтров, если фильтр не нужен нажмите enter");
    System.out.print("Введите Страну:");
    resultScan = filter.nextLine();
    if(!resultScan.isEmpty()) {
        fieldsZaprosa.put("country", resultScan);
        paramsSearch = "&country=" + resultScan;
        fields += "country,";
    }
    System.out.print("Введите Город:");
    resultScan = filter.nextLine();
    if(!resultScan.isEmpty()) {
        fieldsZaprosa.put("city", resultScan);
        paramsSearch += "&city=" + resultScan;
        fields += "city,";
    }
    System.out.print("Введите пол:");
    resultScan = filter.nextLine();
    if(!resultScan.isEmpty()) {
        fieldsZaprosa.put("sex", resultScan);
        paramsSearch += "&sex=" + resultScan;
        fields += "sex,";
    }
    System.out.print("Введите Вуз:");
    resultScan = filter.nextLine();
    if(!resultScan.isEmpty()) {
        fieldsZaprosa.put("universities", resultScan);
        paramsSearch += "&university=" + resultScan;
        fields += "universities";
    }
    ArrayList<String> userList = new ArrayList<>();
    ArrayList<String> list = new ArrayList<>();
    Search str = new Search();
    userList = str.zaprosSearch();
    Integer userListCout = 0;
    while(true){
        for (int i = userListCout; i < userList.size(); i++) {
            list.add(userList.get(i));
        }
    userListCout = userList.size();
    if( list.isEmpty()) {
        break;
    }
    for(int j=0; j<list.size();j++) {
        str.getUserFriend(list.get(j), fields);
   }
    list.clear();
    }
    System.out.println("Вершин в графе: " +userList.size());
}   
}
