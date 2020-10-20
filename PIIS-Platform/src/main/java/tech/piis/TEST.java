package tech.piis;

import org.springframework.util.CollectionUtils;
import tech.piis.modules.core.domain.vo.UserBriefVO;

import java.util.ArrayList;
import java.util.List;

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
//        String str = "0000000000|1800000000|1805000000";
//        str = str.replace("|", ",").replace("1805000000", "");
//        String[] strings = str.split(",");
//        Collections.reverse(Arrays.asList(strings));
//        String newStr = "";
//        for (String s : strings) {
//            newStr += s + ",";
//        }
//        System.out.println(newStr.substring(0,newStr.lastIndexOf(",")));
        String userIds = "18000462,18000453,18000444";
        String userNames = "张喆,姜天傲,杨栋楠";
        List<UserBriefVO> userBriefs = new ArrayList<>();
        userBriefs.add(new UserBriefVO().setUserId("18000462").setUserName("张喆"));
        userBriefs.add(new UserBriefVO().setUserId("18000453").setUserName("杨栋楠"));
        List<UserBriefVO> userBriefVOS = paramsCovert2List(userIds, userNames);
        List<String> strings = paramsCovert2String(userBriefs);
        for (UserBriefVO userBriefVO : userBriefVOS) {
            System.out.println(userBriefVO);
        }

        for (String string : strings) {
            System.out.println(string);
        }
    }


    /**
     * 将人员拼接字符串转为List
     *
     * @param userId
     * @param userName
     */
    protected static List<UserBriefVO> paramsCovert2List(String userId, String userName) {
        List<UserBriefVO> result = new ArrayList<>();
        if (!org.springframework.util.StringUtils.isEmpty(userId) && !org.springframework.util.StringUtils.isEmpty(userName)) {
            String[] userIdsArr = userId.split(",");
            String[] userNamesArr = userName.split(",");
            if (userNamesArr.length != 0 && userIdsArr.length != 0) {
                int length = userNamesArr.length;
                for (int i = 0; i < length; i++) {
                    UserBriefVO userBriefVO = new UserBriefVO();
                    userBriefVO.setUserName(userNamesArr[i]);
                    userBriefVO.setUserId(userIdsArr[i]);
                    result.add(userBriefVO);
                }
            }
        }
        return result;
    }

    /**
     * 将人员对象数组转为字符串  以，分割
     *
     * @param data
     */
    @SuppressWarnings("unchecked")
    protected static List<String> paramsCovert2String(List<UserBriefVO> data) {
        List<String> result = new ArrayList<>();
        StringBuilder userId = new StringBuilder();
        StringBuilder userName = new StringBuilder();
        if (!CollectionUtils.isEmpty(data)) {
            data.forEach(var -> {
                userId.append(var.getUserId()).append(",");
                userName.append(var.getUserName()).append(",");
            });
        }
        String userIdStr = userId.toString().substring(0, userId.toString().lastIndexOf(","));
        String userNameStr = userName.toString().substring(0, userName.toString().lastIndexOf(","));
        result.add(userIdStr);
        result.add(userNameStr);
        return result;
    }
}
