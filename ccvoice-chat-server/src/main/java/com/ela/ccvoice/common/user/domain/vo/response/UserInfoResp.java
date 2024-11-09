package com.ela.ccvoice.common.user.domain.vo.response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@Api("用户详情")
public class UserInfoResp {
    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("昵称")
    private String name;
    @ApiModelProperty("头像")
    private String avatar;
    @ApiModelProperty("性别 1male 2female")
    private Integer sex;
    @ApiModelProperty("剩余改名次数")
    private Integer modifyNameChange;
}
