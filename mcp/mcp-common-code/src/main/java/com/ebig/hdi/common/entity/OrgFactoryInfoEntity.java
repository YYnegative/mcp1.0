package com.ebig.hdi.common.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import org.springframework.jdbc.core.RowMapper;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * 厂商信息
 * 
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-04-23 09:27:40
 */
@Data
@TableName("hdi_org_factory_info")
public class OrgFactoryInfoEntity implements RowMapper, Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	//@TableId
	//主键手动输入(取消主键自增)
	@TableId(value = "id",type = IdType.INPUT)
	private Long id;
	/**
	 * 厂商编码
	 */
	private String factoryCode;
	/**
	 * 厂商名称
	 */
	@NotBlank(message="厂商名称不能为空")
	private String factoryName;
	/**
	 * 统一社会信用代码
	 */
	@NotBlank(message="统一社会信用代码不能为空")
	private String creditCode;
	/**
	 * 所在国家编码
	 */
	private String countryCode;
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
	 * 厂商地址
	 */
	private String factoryAddress;
	/**
	 * 状态(-1:待审批;0:停用;1:启用)
	 */
	@NotNull(message="请选择状态")
	private Integer status;
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


	@Override
	public boolean equals(Object obj) {
		if (obj instanceof OrgFactoryInfoEntity) {
			OrgFactoryInfoEntity entity= (OrgFactoryInfoEntity) obj;
			if(this.getFactoryName().equals(entity.getFactoryName())){
				return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		OrgFactoryInfoEntity vo = new OrgFactoryInfoEntity();
		vo.setId(rs.getLong("id"));
		vo.setFactoryName(rs.getString("factory_name"));
		return vo;
	}
}
