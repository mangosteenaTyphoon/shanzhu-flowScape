package com.shanzhu.model.system.vo;

import com.shanzhu.entity.system.SysAttachment;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysAttachmentVO extends SysAttachment {

    // 上传用户昵称
    private String uploadName;
}
