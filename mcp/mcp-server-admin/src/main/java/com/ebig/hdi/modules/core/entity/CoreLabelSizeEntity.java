package com.ebig.hdi.modules.core.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author luorx
 * @email 492699405@qq.com
 * @date 2020-01-08 10:08:48
 */
@TableName("hdi_core_label_size")
@Data
public class CoreLabelSizeEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long id;
	/**
	 * 用户id
	 */
	private Long userId;
	/**
	 * 部门id
	 */
	private Integer deptId;
	/**
	 * 属性类别(0:标签;1:批次码)
	 */
	private Integer typeId;
	/**
	 * 二维码宽
	 */
	private Integer qrcodeWidth;
	/**
	 * 二维码高
	 */
	private Integer qrcodeHeight;
	/**
	 * pdf宽
	 */
	private Double pdfWidth;
	/**
	 * pdf高
	 */
	private Double pdfHeight;
	/**
	 * 创建人id
	 */
	private Long createId;
	/**
	 * 创建人名称
	 */
	private String createName;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 修改人id
	 */
	private Long editId;
	/**
	 * 修改人名称
	 */
	private String editName;
	/**
	 * 修改时间
	 */
	private Date editTime;


}
