package tech.piis.modules.core.domain.dto;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.beans.IntrospectionException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Package: tech.piis.modules.core.domain.dto
 * User: Tuzki
 * Date: 2020/9/27
 * Time: 14:21
 * Description:新增计划返回DTO
 */

@Data
@Accessors(chain = true)
public class InspectionPlanSaveDTO {

    /**
     * 新增后影响的行数
     */
    private int row;

    /**
     * 文件名集合
     */
    private List<String> fileNameList;
}
