package com.ela.ccvoice.common.user.domain.vo.request.userRelate;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ModifyNameReq {
    @NotNull
    @Length(min = 1,max = 10, message = "用户名最大上限10，不可过长")
    @ApiModelProperty("用户名")
    private String name;
}
