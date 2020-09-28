package tech.piis;

import tech.piis.modules.core.domain.po.InspectionGroupMemberPO;

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
        String str = "piis.file.com:81/2020/09/28/集团本部-云资源配置清单.xls";
//        System.out.println(str.substring(str.indexOf("\\/"),str.length() - 1));
        System.out.println(str.replace("piis.file.com:81","E:/file/upload"));
    }
}
