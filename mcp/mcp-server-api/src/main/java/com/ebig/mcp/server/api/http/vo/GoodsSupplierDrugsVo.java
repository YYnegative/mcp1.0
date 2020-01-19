package com.ebig.mcp.server.api.http.vo;

import lombok.Data;
import org.springframework.jdbc.core.RowMapper;

import javax.validation.constraints.NotNull;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @description:
 * @author: wenchao
 * @time: 2019-10-16 9:47
 */
@Data
public class GoodsSupplierDrugsVo extends GoodsSupplierVo implements RowMapper {
    /**
     * 药品编码
     */
    private String drugsCode;
    /**
     * 药品名称
     */
    @NotNull
    private String drugsName;

    @Override
    public GoodsSupplierDrugsVo mapRow(ResultSet rs, int rowNum) throws SQLException {
        GoodsSupplierDrugsVo vo = new GoodsSupplierDrugsVo();
        vo.setId(rs.getLong("id"));
        vo.setDrugsName(rs.getString("drugs_name"));
        vo.setSupplierCode(rs.getString("supplier_code"));
        return vo;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GoodsSupplierDrugsVo) {
            GoodsSupplierDrugsVo entity= (GoodsSupplierDrugsVo) obj;
            if(this.getDrugsName().equals(entity.getDrugsName())
                    && this.getSupplierCode().equals(entity.getSupplierCode())
                    && this.getFactoryName().equals(entity.getFactoryName())){
                return true;
            }
        }
        return false;
    }
    @Override
    public int hashCode() {
        int result = this.getSupplierCode().hashCode();
        result = 19 * result + this.getDrugsName().hashCode();
        return result;

    }
}
