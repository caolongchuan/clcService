package entiry;

/**
 * 所有卖出去的商品
 */
public class out_trade_no_table {
    public String out_trade_no;//单号
    public long create_time;//创建时间
    public int lottery_status;//中奖状态 0是未抽奖 1-10是抽中的奖值 11-20是已经领过奖
    public String user_openid;//用户的openid
    public int total_fee;//金额数
}
