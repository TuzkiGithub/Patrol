package tech.piis.framework.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import tech.piis.common.enums.OrgEnum;
import tech.piis.common.utils.DateUtils;
import tech.piis.common.utils.StringUtils;
import tech.piis.modules.core.domain.po.PiisDocumentPO;
import tech.piis.modules.core.domain.vo.UserBriefVO;
import tech.piis.modules.survey.domain.po.SurveyOptionPO;
import tech.piis.modules.survey.domain.po.SurveyQuestionPO;

import java.lang.reflect.Field;
import java.util.*;

import static tech.piis.common.constant.OrgConstants.*;

/**
 * Created with IntelliJ IDEA.
 * Package: tech.piis.common.utils
 * User: Tuzki
 * Date: 2020/9/29
 * Time: 16:50
 * Description:业务处理工具类
 */
@Slf4j
public class BizUtils {


    @SuppressWarnings("unchecked")
    public static <T> void setCreatedOperation(Class<T> clazz, T object) {
        List<Field> fieldList = new ArrayList<>();
        while (clazz != null) {
            fieldList.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
            clazz = (Class<T>) clazz.getSuperclass();
        }
        try {
            for (Field field : fieldList) {
                field.setAccessible(true);
                Object fieldName = field.getName();
                if (fieldName.equals("createdBy")) {
                    field.set(object, SecurityUtils.getUsername());
                }
                if (fieldName.equals("createBy")) {
                    field.set(object, SecurityUtils.getUsername());
                }
                if (fieldName.equals("createTime")) {
                    field.set(object, DateUtils.getNowDate());
                }
                if (fieldName.equals("createdTime")) {
                    field.set(object, DateUtils.getNowDate());
                }
            }
        } catch (Exception e) {
            log.error("###BizUtils### reflect is failed!");
            e.printStackTrace();
        }

    }


    @SuppressWarnings("unchecked")
    public static <T> void setCreatedTimeOperation(Class<T> clazz, T object) {
        List<Field> fieldList = new ArrayList<>();
        while (clazz != null) {
            fieldList.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
            clazz = (Class<T>) clazz.getSuperclass();
        }
        try {
            for (Field field : fieldList) {
                field.setAccessible(true);
                Object fieldName = field.getName();
                if (fieldName.equals("createdBy")) {
                    field.set(object, "sys");
                }
                if (fieldName.equals("createBy")) {
                    field.set(object, "sys");
                }
                if (fieldName.equals("createTime")) {
                    field.set(object, DateUtils.getNowDate());
                }
                if (fieldName.equals("createdTime")) {
                    field.set(object, DateUtils.getNowDate());
                }
            }
        } catch (Exception e) {
            log.error("###BizUtils### reflect is failed!");
            e.printStackTrace();
        }

    }

    @SuppressWarnings("unchecked")
    public static <T> void setUpdatedOperation(Class<T> clazz, T object) {
        List<Field> fieldList = new ArrayList<>();
        while (clazz != null) {
            fieldList.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
            clazz = (Class<T>) clazz.getSuperclass();
        }
        try {
            for (Field field : fieldList) {
                field.setAccessible(true);
                Object fieldName = field.getName();
                if (fieldName.equals("updatedBy")) {
                    field.set(object, SecurityUtils.getUsername());
                }
                if (fieldName.equals("updatedTime")) {
                    field.set(object, DateUtils.getNowDate());
                }
                if (fieldName.equals("updateBy")) {
                    field.set(object, SecurityUtils.getUsername());
                }
                if (fieldName.equals("updateTime")) {
                    field.set(object, DateUtils.getNowDate());
                }
            }
        } catch (Exception e) {
            log.error("###BizUtils### reflect is failed!");
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> void setUpdatedTimeOperation(Class<T> clazz, T object) {
        List<Field> fieldList = new ArrayList<>();
        while (clazz != null) {
            fieldList.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
            clazz = (Class<T>) clazz.getSuperclass();
        }
        try {
            for (Field field : fieldList) {
                field.setAccessible(true);
                Object fieldName = field.getName();
                if (fieldName.equals("updatedBy")) {
                    field.set(object, "sys");
                }
                if (fieldName.equals("updatedTime")) {
                    field.set(object, DateUtils.getNowDate());
                }
                if (fieldName.equals("updateBy")) {
                    field.set(object, "sys");
                }
                if (fieldName.equals("updateTime")) {
                    field.set(object, DateUtils.getNowDate());
                }
            }
        } catch (Exception e) {
            log.error("###BizUtils### reflect is failed!");
            e.printStackTrace();
        }
    }

    /**
     * 根据公司简称，获取对应ID
     *
     * @return
     */
    public static String getOrgIdByName(String orgName) {
        switch (orgName) {
            case ALL:
                return OrgEnum.ALL.getOrgId();
            case EB_GROUP:
                return OrgEnum.EB_GROUP.getOrgId();
            case EB_BANK:
                return OrgEnum.EB_BANK.getOrgId();
            case EB_BOND:
                return OrgEnum.EB_BOND.getOrgId();
            case EB_INSURANCE:
                return OrgEnum.EB_INSURANCE.getOrgId();
            case EB_TRUST:
                return OrgEnum.EB_TRUST.getOrgId();
            case EB_FINANCIAL:
                return OrgEnum.EB_FINANCIAL.getOrgId();
            case EB_INDUSTRY:
                return OrgEnum.EB_INDUSTRY.getOrgId();
            case EB_HK:
                return OrgEnum.EB_HK.getOrgId();
            case EB_HOSTEL:
                return OrgEnum.EB_HOSTEL.getOrgId();
            case EB_HEALTH:
                return OrgEnum.EB_HEALTH.getOrgId();
            case EB_TRAVEL:
                return OrgEnum.EB_TRAVEL.getOrgId();
            case EB_MEDICINE:
                return OrgEnum.EB_MEDICINE.getOrgId();
            case EB_METAL:
                return OrgEnum.EB_METAL.getOrgId();
            case EB_TECH:
                return OrgEnum.EB_TECH.getOrgId();
        }
        return null;
    }

    /**
     * 处理选项中的A/B/C/D
     *
     * @return
     */
    public static void filterOptionName(List<SurveyOptionPO> optionList) {
        if (!CollectionUtils.isEmpty(optionList)) {
            optionList.forEach(option -> {
                String optionName = option.getOptionName();
                if (!StringUtils.isEmpty(optionName)) {
                    optionName = optionName.substring(optionName.indexOf("、") + 1, optionName.length());
                    option.setOptionName(optionName);
                }
            });
        }
    }

    /**
     * 为选项添加A/B/C/D
     */
    public static void addOptionName(List<SurveyQuestionPO> questionList) {
        if (!CollectionUtils.isEmpty(questionList)) {
            questionList.forEach(question -> {
                List<SurveyOptionPO> optionList = question.getOptionList();
                if (!CollectionUtils.isEmpty(optionList)) {
                    for (int i = 0; i < optionList.size(); i++) {
                        String optionName = optionList.get(i).getOptionName();
                        if (!StringUtils.isEmpty(optionName)) {
                            String prefix = "";
                            switch (i) {
                                case 0:
                                    prefix = "A、";
                                    break;
                                case 1:
                                    prefix = "B、";
                                    break;
                                case 2:
                                    prefix = "C、";
                                    break;
                                case 3:
                                    prefix = "D、";
                                    break;
                                case 4:
                                    prefix = "E、";
                                    break;
                                case 5:
                                    prefix = "F、";
                                    break;
                                case 6:
                                    prefix = "G、";
                                    break;
                                case 7:
                                    prefix = "H、";
                                    break;
                                case 8:
                                    prefix = "I、";
                                    break;
                                case 9:
                                    prefix = "J、";
                                    break;
                                case 10:
                                    prefix = "K、";
                                    break;
                                case 11:
                                    prefix = "L、";
                                    break;
                                case 12:
                                    prefix = "M、";
                                    break;
                                case 13:
                                    prefix = "N、";
                                    break;
                                case 14:
                                    prefix = "O、";
                                    break;
                                case 15:
                                    prefix = "P、";
                                    break;
                                case 16:
                                    prefix = "Q、";
                                    break;
                                case 17:
                                    prefix = "R、";
                                    break;
                                case 18:
                                    prefix = "S、";
                                    break;
                                case 19:
                                    prefix = "T、";
                                    break;
                                case 20:
                                    prefix = "U、";
                                    break;
                                case 21:
                                    prefix = "V、";
                                    break;
                                case 22:
                                    prefix = "W、";
                                    break;
                                case 23:
                                    prefix = "X、";
                                    break;
                                case 24:
                                    prefix = "Y、";
                                    break;
                                case 25:
                                    prefix = "Z、";
                                    break;
                            }
                            optionList.get(i).setOptionName(prefix + optionName);
                        }
                    }
                }
            });
        }
    }

    /**
     * 实际文件字典 《 - 》 临时文件字典
     * key -> value
     * @param documents
     */
    public static void convertFileDict(List<PiisDocumentPO> documents, Map<Long, Long> mapping) {
        if (!CollectionUtils.isEmpty(documents)) {
            documents.forEach(document -> {
                Long DictId = document.getFileDictId();
                document.setFileDictId(mapping.get(DictId));
            });
        }
    }

    /**
     * 将人员拼接字符串转为List
     *
     * @param userId
     * @param userName
     */
    public static List<UserBriefVO> paramsCovert2List(String userId, String userName) {
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
    public static List<String> paramsCovert2String(List<UserBriefVO> data) {
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
