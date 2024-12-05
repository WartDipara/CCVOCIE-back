package com.ela.ccvoice.common.common.domain.vo.response;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;

/**
 * 游标翻页返回结果
 *
 * @param
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CursorPageBaseResp<T> {
    @ApiModelProperty("游标")
    private String cursor;
    @ApiModelProperty("是否最后一页")
    private Boolean isLast=Boolean.FALSE;
    @ApiModelProperty("数据")
    private List<T> list;

    public static <T> CursorPageBaseResp<T> empty(){
        CursorPageBaseResp<T> cursorPageBaseResp = new CursorPageBaseResp<>();
        cursorPageBaseResp.setList(new ArrayList<>());
        cursorPageBaseResp.setIsLast(Boolean.TRUE);
        return cursorPageBaseResp;
    }
    public boolean isEmpty(){
        return CollectionUtil.isEmpty(list);
    }

    public static <T> CursorPageBaseResp<T> init(CursorPageBaseResp cursorPage, List<T> list){
        CursorPageBaseResp<T> cursorPageBaseResp = new CursorPageBaseResp<>();
        cursorPageBaseResp.setCursor(cursorPage.getCursor());
        cursorPageBaseResp.setIsLast(cursorPage.getIsLast());
        cursorPageBaseResp.setList(list);
        return cursorPageBaseResp;
    }
}
