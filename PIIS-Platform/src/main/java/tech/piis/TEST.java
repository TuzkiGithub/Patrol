package tech.piis;

import java.util.Arrays;
import java.util.Collections;

/**
 * Created with IntelliJ IDEA.
 * Package: PACKAGE_NAME
 * User: Tuzki
 * Date: 2020/9/24
 * Time: 8:34
 * Description:
 */
public class TEST {
    public static void main(String[] args) {
//        InspectionGroupMemberPO groupMemberPO1 = new InspectionGroupMemberPO();
//        InspectionGroupMemberPO groupMemberPO2 = new InspectionGroupMemberPO();
//
//        groupMemberPO1.setGroupMemberId(111L);
//        groupMemberPO2.setGroupMemberId(111L);
//        System.out.println(groupMemberPO1 == groupMemberPO2);
//        System.out.println(groupMemberPO1.hashCode() == groupMemberPO2.hashCode());
//        System.out.println(groupMemberPO1.equals(groupMemberPO2));
//        double d;
//        System.out.println(d);
        String str = "0000000000|1800000000|1805000000";
        str = str.replace("|", ",").replace("1805000000", "");
        String[] strings = str.split(",");
        Collections.reverse(Arrays.asList(strings));
        String newStr = "";
        for (String s : strings) {
            newStr += s + ",";
        }
        System.out.println(newStr.substring(0,newStr.lastIndexOf(",")));
    }
}
