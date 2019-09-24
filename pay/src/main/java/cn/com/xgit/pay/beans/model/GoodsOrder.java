package cn.com.xgit.pay.beans.model;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@ToString
@Data
public class GoodsOrder implements Serializable {
    /**
     * 商品订单ID
     *
     * @mbggenerated
     */
    private String goodsOrderId;

    /**
     * 商品ID
     *
     * @mbggenerated
     */
    private String goodsId;

    /**
     * 商品名称
     *
     * @mbggenerated
     */
    private String goodsName;

    /**
     * 金额,单位分
     *
     * @mbggenerated
     */
    private Long amount;

    /**
     * 用户ID
     *
     * @mbggenerated
     */
    private String userId;

    /**
     * 订单状态,订单生成(0),支付成功(1),处理完成(2),处理失败(-1)
     *
     * @mbggenerated
     */
    private Byte status;

    /**
     * 支付订单号
     *
     * @mbggenerated
     */
    private String payOrderId;

    /**
     * 渠道ID
     *
     * @mbggenerated
     */
    private String channelId;

    /**
     * 支付渠道用户ID(微信openID或支付宝账号等第三方支付账号)
     *
     * @mbggenerated
     */
    private String channelUserId;

    /**
     * 创建时间
     *
     * @mbggenerated
     */
    private Date createTime;

    /**
     * 更新时间
     *
     * @mbggenerated
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;

}
