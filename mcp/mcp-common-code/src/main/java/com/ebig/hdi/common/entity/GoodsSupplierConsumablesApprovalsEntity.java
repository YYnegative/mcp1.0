package com.ebig.hdi.common.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 供应商耗材批准文号
 *
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-10 09:36:02
 */
@TableName("hdi_goods_supplier_consumables_approvals")
public class GoodsSupplierConsumablesApprovalsEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId
    private Long id;
    /**
     * 耗材id
     */
    private Long consumablesId;
    /**
     * 批准文号
     */
    private String approvals;
    /**
     * 状态(0:停用;1:启用)
     */
    private Integer status;
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
     * 设置：主键id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取：主键id
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置：耗材id
     */
    public void setConsumablesId(Long consumablesId) {
        this.consumablesId = consumablesId;
    }

    /**
     * 获取：耗材id
     */
    public Long getConsumablesId() {
        return consumablesId;
    }

    /**
     * 设置：批准文号
     */
    public void setApprovals(String approvals) {
        this.approvals = approvals;
    }

    /**
     * 获取：批准文号
     */
    public String getApprovals() {
        return approvals;
    }

    /**
     * 设置：状态(0:停用;1:启用)
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取：状态(0:停用;1:启用)
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置：创建人id
     */
    public void setCreateId(Long createId) {
        this.createId = createId;
    }

    /**
     * 获取：创建人id
     */
    public Long getCreateId() {
        return createId;
    }

    /**
     * 设置：创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取：创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置：修改人id
     */
    public void setEditId(Long editId) {
        this.editId = editId;
    }

    /**
     * 获取：修改人id
     */
    public Long getEditId() {
        return editId;
    }

    /**
     * 设置：修改时间
     */
    public void setEditTime(Date editTime) {
        this.editTime = editTime;
    }

    /**
     * 获取：修改时间
     */
    public Date getEditTime() {
        return editTime;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GoodsSupplierConsumablesApprovalsEntity) {
            GoodsSupplierConsumablesApprovalsEntity entity = (GoodsSupplierConsumablesApprovalsEntity) obj;
            if (this.getConsumablesId().equals(entity.getConsumablesId()) && this.getApprovals().equals(entity.getApprovals())) {
                return true;
            }
        }
        return false;
    }
    @Override
    public int hashCode() {
        int result = this.getConsumablesId().hashCode();
        result = 19 * result + this.getApprovals().hashCode();
        return result;

    }
}
