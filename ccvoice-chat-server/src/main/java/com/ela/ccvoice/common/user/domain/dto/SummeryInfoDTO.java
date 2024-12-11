package com.ela.ccvoice.common.user.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 修改用户名
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SummeryInfoDTO {
    @ApiModelProperty(value = "用户id")
    private Long uid;
    @ApiModelProperty(value = "是否需要刷新")
    private Boolean needRefresh = Boolean.TRUE;
    @ApiModelProperty(value = "用户邮箱")
    private String email;
    @ApiModelProperty(value = "用户昵称")
    private String name;
    @ApiModelProperty(value = "用户头像")
    private String avatar;
    @ApiModelProperty(value = "归属地")
    private String locPlace;


    /**
     * 跳过更新
     * @param uid
     * @return
     */
    public static SummeryInfoDTO skip(Long uid) {
        SummeryInfoDTO dto = new SummeryInfoDTO();
        dto.setUid(uid);
        dto.setNeedRefresh(Boolean.FALSE);
        return dto;
    }
}
