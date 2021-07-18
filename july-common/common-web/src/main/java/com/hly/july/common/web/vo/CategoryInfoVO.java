package com.hly.july.common.web.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.hly.july.common.core.validation.group.NewCategoryGroup;
import com.hly.july.common.core.validation.group.NewSeriesGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @ClassName CategoryInfoVO
 * @Description
 * @Author Linyuan Hou
 * @Date 2021/7/17 15:18
 * @Version 1.0.0
 **/
@Data
public class CategoryInfoVO {

    private String mainCategoryId;

    private String subCategoryId;

    @Size(max=10, message="主类别名长度最大10位",groups = {NewCategoryGroup.class})
    @Pattern(groups = {NewCategoryGroup.class},regexp = "(^\\s?)|(^([a-zA-Z0-9_\\u4e00-\\u9fa5\\.-]+){1,10}$)"
            ,message = "主类别名必须是字母或者汉字组成(最大10位)")
    private String mainCategoryName;

    @Size(max=10, message="子类别名长度最大10位",groups = {NewCategoryGroup.class})
    @Pattern(groups = {NewCategoryGroup.class},regexp = "(^\\s?)|(^([a-zA-Z0-9_\\u4e00-\\u9fa5\\.-]+){1,10}$)"
            ,message = "子类别名必须是字母或者汉字组成(最大10位)")
    private String subCategoryName;

    private Integer type;

    private String typeStr;
}
