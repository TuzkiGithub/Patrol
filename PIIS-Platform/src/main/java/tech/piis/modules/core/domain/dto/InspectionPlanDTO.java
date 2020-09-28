package tech.piis.modules.core.domain.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.web.multipart.MultipartFile;
import tech.piis.modules.core.domain.po.InspectionPlanPO;

import javax.validation.Valid;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Package: tech.piis.modules.core.domain.dto
 * User: Tuzki
 * Date: 2020/9/27
 * Time: 9:36
 * Description:巡视计划DTO
 */
@Data
@ToString(callSuper = true)
@Accessors(chain = true)
public class InspectionPlanDTO {

    @Valid
    private InspectionPlanPO inspectionPlan;

    private List<PlanFileDTO> files;
}
