package com.shanzhu.mapper.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shanzhu.entity.system.SysAttachment;
import com.shanzhu.model.system.vo.SysAttachmentVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysAttachmentMapper extends BaseMapper<SysAttachment> {

    List<String> queryDeletablePathByIds(@Param("ids") List<String> ids);

    SysAttachmentVO queryById(@Param("id") String id);

}
