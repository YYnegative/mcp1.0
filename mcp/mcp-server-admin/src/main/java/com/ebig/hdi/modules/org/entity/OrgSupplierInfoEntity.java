package com.ebig.hdi.modules.org.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;

import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

/**
 * 供应商信息
 * 
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-04-23 09:27:38
 */
@Data
@TableName("hdi_org_supplier_info")
public class OrgSupplierInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId(value = "id",type = IdType.INPUT)
	private Long id;
	/**
	 * 父供应商id
	 */
	private Long parentId;
	/**
	 * 供应商编码
	 */
	private String supplierCode;
	/**
	 * 供应商名称
	 */
	@NotBlank(message="供应商名称不能为空")
	private String supplierName;
	/**
	 * 统一社会信用代码
	 */
	@NotBlank(message="统一社会信用代码不能为空")
	private String creditCode;
	/**
	 * 所在省编码
	 */
	private String provinceCode;
	/**
	 * 所在市编码
	 */
	private String cityCode;
	/**
	 * 所在区县编码
	 */
	private String areaCode;
	/**
	 * 供应商地址
	 */
	private String supplierAddress;
	/**
	 * 状态(0:停用;1:启用)
	 */
	@NotNull(message="请选择状态")
	private Integer status;
	/**
	 * 供应商性质(0:国营企业;1:民营企业;2:中外合资企业;3:股份制企业;4:个体企业)
	 */
	private Integer supplierNature;
	/**
	 * 法人代表
	 */
	private String corporate;
	/**
	 * 联系电话
	 */
	private String phone;
	/**
	 * 邮箱地址
	 */
	private String email;
	/**
	 * 传真
	 */
	private String fax;
	/**
	 * 是否集团机构(0:否;1:是)
	 */
	private Integer isGroup;
	/**
	 * 子机构数(集团供应商允许创建的子供应商数)
	 */
	private Integer childNumber;
	/**
	 * 所属机构
	 */
	private Long deptId;
	/**
	 * 创建人id
	 */
	private Long createId;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 修改人id
	 */
	private Long editId;
	/**
	 * 修改时间
	 */
	private Date editTime;
	/**
	 * ztree属性
	 */
	@TableField(exist=false)
	private Boolean open;
	
	/**
	 * 传给前端和id一样的属性
	 */
	@TableField(exist=false)
	private String value;

	@TableField(exist=false)
	private List<OrgSupplierInfoEntity> list;
	
	@TableField(exist=false)
	private String parentName;
	
	/**
	 * 是否默认排序，1：是
	 */
	@TableField(exist=false)
	private Integer isDefaultOrder;
	
	/**
	 * 分页时候的部门权限过滤
	 */
	@TableField(exist=false)
	private String fileterDept;
	/**
	 * 是否删除(-1:已删除;0:正常)
	 */
	@TableLogic
	private Integer delFlag; 

	/**
	 * 数据来源(0:系统录入;1:医院SPD)
	 */
	private Integer dataSource;
}
