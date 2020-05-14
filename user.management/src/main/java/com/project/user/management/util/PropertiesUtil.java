package com.project.user.management.util;


import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@PropertySource(value = "classpath:application.properties")
public class PropertiesUtil {

    public static String getPropertyValue(Environment env, String key) {
        String rs = "";

        try {
            rs = env.getProperty(key);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rs;
    }

    public static Integer getPropertyValueInt(Environment env, String key) {
        Integer rs = -1;

        try {
            rs = Integer.parseInt(env.getProperty(key));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rs;
    }

    public static Integer getRoleSysAdminID(Environment env) {
        Integer roleId = -1;

        try {
            roleId = Integer.parseInt(env.getProperty("cons.role-sysadmin-id"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return roleId;
    }

}
