package tech.piis.modules.managment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tech.piis.common.exception.BaseException;
import tech.piis.common.utils.DateUtils;
import tech.piis.common.utils.IdUtils;
import tech.piis.modules.core.domain.po.PiisDocumentPO;
import tech.piis.modules.core.mapper.PiisDocumentMapper;
import tech.piis.modules.managment.domain.po.FullPartOrgPO;
import tech.piis.modules.managment.mapper.FullPartOrgMapper;
import tech.piis.modules.managment.service.IFullPartOrgService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * ClassName : IFullPartOrgService
 * Package : tech.piis.modules.managment.service.impl
 * Description :
 * 专兼职管理 业务层处理
 *
 * @author : chenhui@xvco.com
 */
@Slf4j
@Transactional
@Service
public class FullPartOrgServiceImpl implements IFullPartOrgService {

    @Autowired
    private FullPartOrgMapper fullPartOrgMapper;
    @Autowired
    private PiisDocumentMapper documentMapper;
    @Value("${piis.profile}")
    private String baseFileUrl;
    @Value("${piis.serverAddr}")
    private String serverAddr;

    /**
     * 新增专兼职管理
     *
     * @param fullPartOrgPO
     * @return
     */
    @Override
    public int save(FullPartOrgPO fullPartOrgPO) throws BaseException {
        if (null != fullPartOrgPO) {
            String fullPartId = DateUtils.dateTime() + IdUtils.simpleUUID().substring(0, 6);
            fullPartOrgPO.setFullPartId(fullPartId);
            List<PiisDocumentPO> inspectionPointDocs = fullPartOrgPO.getInspectionPointDocs();
            bundledFile(inspectionPointDocs, "poi" + fullPartId);

            List<PiisDocumentPO> inspectionSystemDocs = fullPartOrgPO.getInspectionSystemDocs();
            bundledFile(inspectionSystemDocs, "sys" + fullPartId);
        }
        return fullPartOrgMapper.insert(fullPartOrgPO);
    }

    /**
     * 修改专兼职管理
     *
     * @param fullPartOrgPO
     * @return
     */
    @Override
    public int update(FullPartOrgPO fullPartOrgPO) throws BaseException {
        String fullPartId = fullPartOrgPO.getFullPartId();
        QueryWrapper<PiisDocumentPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("object_id", fullPartId);
        List<PiisDocumentPO> piisDocumentPOS = documentMapper.selectList(queryWrapper);
        unbundledFile(piisDocumentPOS);

        List<PiisDocumentPO> inspectionPointDocs = fullPartOrgPO.getInspectionPointDocs();
        bundledFile(inspectionPointDocs, "poi" + fullPartId);

        List<PiisDocumentPO> inspectionSystemDocs = fullPartOrgPO.getInspectionSystemDocs();
        bundledFile(inspectionSystemDocs, "sys" + fullPartId);
        return fullPartOrgMapper.updateById(fullPartOrgPO);
    }


    /**
     * 删除专兼职管理
     *
     * @param fullPartOrgIds
     * @return
     */
    @Override
    public int deleteByFullPartOrgId(String[] fullPartOrgIds) throws BaseException {
        List<String> list = Arrays.asList(fullPartOrgIds);
        if (!CollectionUtils.isEmpty(list)) {
            list.forEach(fullPartId -> {
                QueryWrapper<PiisDocumentPO> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("object_id", fullPartId);
                List<PiisDocumentPO> piisDocumentPOS = documentMapper.selectList(queryWrapper);
                unbundledFile(piisDocumentPOS);
            });
        }

        return fullPartOrgMapper.deleteBatchIds(list);
    }

    /**
     * 专兼职管理列表
     *
     * @param orgName
     * @return
     */
    @Override
    public List<FullPartOrgPO> selectByWholeName(String orgName) throws BaseException {
        QueryWrapper<FullPartOrgPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(orgName), "ORG_NAME", orgName);
        List<FullPartOrgPO> fullPartOrgList = fullPartOrgMapper.selectList(queryWrapper);
        if (!CollectionUtils.isEmpty(fullPartOrgList)) {
            fullPartOrgList.forEach(fullPartOrg -> {
                String fullPartId = fullPartOrg.getFullPartId();
                QueryWrapper<PiisDocumentPO> query = new QueryWrapper<>();
                queryWrapper.eq("object_id", fullPartId);
                List<PiisDocumentPO> piisDocumentPOS = documentMapper.selectList(query);
                if (!CollectionUtils.isEmpty(piisDocumentPOS)) {
                    List<PiisDocumentPO> inspectionSystemDocs = new ArrayList<>();
                    List<PiisDocumentPO> inspectionPointDocs = new ArrayList<>();
                    piisDocumentPOS.forEach(piisDocumentPO -> {
                        if (piisDocumentPO.getObjectId().contains("sys")) {
                            inspectionSystemDocs.add(piisDocumentPO);
                        } else if (piisDocumentPO.getObjectId().contains("poi")) {
                            inspectionPointDocs.add(piisDocumentPO);
                        }
                    });
                    fullPartOrg.setInspectionSystemDocs(inspectionSystemDocs);
                    fullPartOrg.setInspectionPointDocs(inspectionPointDocs);
                }
            });
        }
        return fullPartOrgList;
    }

    private void unbundledFile(List<PiisDocumentPO> documents) {
        if (!CollectionUtils.isEmpty(documents)) {
            documents.forEach(piisDocumentPO -> {
                piisDocumentPO.setObjectId("");
                documentMapper.updateById(piisDocumentPO);
            });
        }
    }

    private void bundledFile(List<PiisDocumentPO> documents, String objectId) {
        if (!CollectionUtils.isEmpty(documents)) {
            documents.forEach(piisDocumentPO -> {
                piisDocumentPO.setObjectId(objectId);
                documentMapper.updateById(piisDocumentPO);
            });
        }
    }
}
