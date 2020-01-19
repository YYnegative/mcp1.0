package com.ebig.hdi.modules.org.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 医院信息
 * 
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-04-23 09:27:40
 */
@Data
@TableName("hdi_org_hospital_info")
public class OrgHospitalInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId
	private Long id;
	/**
	 * 父医院id,顶级医院为0
	 */
	private Long parentId;
	/**
	 * 医院编码
	 */
	private String hospitalCode;
	/**
	 * 医院级别(0:三级特等;1:三级甲等;2:三级乙等;3:三级丙等;4:二级甲等;5:二级乙等;6:二级丙等;7:一级甲等;8:一级乙等;9:一级丙等)
	 */
	@NotNull(message="请选择医院级别")
	private Integer hospitalGrade;
	/**
	 * 医院名称
	 */
	@NotBlank(message="医院名称不能为空")
	private String hospitalName;
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
	 * 状态(0:停用;1:启用)
	 */
	@NotNull(message="请选择状态")
	private Integer status;
	/**
	 * 医院地址
	 */
	private String hospitalAddress;
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
	 * 是否集团机构(0:否;1:是)
	 */
	private Integer isGroup;
	/**
	 * 子机构数(集团医院允许创建的子医院数)
	 */
	private Integer childNumber;
	/**
	 * 传真
	 */
	private String fax;
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
	 * 是否删除(-1:已删除;0:正常)
	 */
	@TableLogic
	private Integer delFlag; 
	/**
	 * 数据来源(0:系统录入;1:医院SPD)
	 */
	private Integer dataSource; 
}
