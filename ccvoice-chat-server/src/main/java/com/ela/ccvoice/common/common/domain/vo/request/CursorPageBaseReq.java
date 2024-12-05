package com.ela.ccvoice.common.common.domain.vo.request;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@ApiModel("游标翻页请求")
@AllArgsConstructor
@NoArgsConstructor
public class CursorPageBaseReq {
    @ApiModelProperty("游标,最初null，之后不为null")
    private String cursor;
    @ApiModelProperty("页面大小")
    @Max(100)
    @Min(0)
    private Integer pageSize = 10;

    public Page plusPage() {
        return new Page(1, this.pageSize, false);
    }

    @JsonIgnore
    public Boolean isFirstPage() {
        return StringUtils.isEmpty(cursor);
    }
}
