package com.syshlang.smm.pojo;

/**
 * Created by sunys on 2017/7/1 13:14.
 * Description:枚举类型的使用
 */
public enum Querylist {

    Author("name","sunys"),
    Time("date","2017年7月1日 13:23:55");
    public final String key;
    public final String value;
    Querylist(String key,String value){
        this.key =key;
        this.value=value;
    }

    public String getValueByKey(String key){
        String type="";
        if (key != null && !key.isEmpty()){
            if (key.equals(Author.key)){
                type = Author.value;
            }else if (key.equals(Time.key)){
                type = Time.value;
            }else {
                type ="没有找到！";
            }
        }

        return null;
    }
}
