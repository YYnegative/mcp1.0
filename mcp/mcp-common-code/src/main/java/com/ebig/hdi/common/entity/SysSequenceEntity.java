package com.ebig.hdi.common.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import org.springframework.jdbc.core.RowMapper;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 系统序列
 * 
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-04-24 13:06:34
 */
@Data
@TableName("sys_sequence")
public class SysSequenceEntity  implements RowMapper, Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId
	private Long id;
	/**
	 * 序列编码
	 */
	private String seqCode;
	/**
	 * 序列名称
	 */
	private String seqName;
	/**
	 * 序列前缀
	 */
	private String seqPrefix;
	/**
	 * 格式化长度
	 */
	private Integer formatLength;
	/**
	 * 当前值
	 */
	private Long currentVal;

	@Override
	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		SysSequenceEntity vo = new SysSequenceEntity();
		vo.setId(rs.getLong("id"));
		vo.setSeqCode(rs.getString("seq_code"));
		vo.setSeqName(rs.getString("seq_name"));
		vo.setSeqPrefix(rs.getString("seq_prefix"));
		vo.setFormatLength(rs.getInt("format_length"));
		vo.setCurrentVal(rs.getLong("current_val"));
		return vo;
	}
}
