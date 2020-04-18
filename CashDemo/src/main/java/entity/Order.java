package entity;

import common.OrderStatus;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: GAOBO
 * Date: 2020-02-13
 * Time: 9:52
 */
@Data
public class Order {
    private String id;
    private Integer account_id;
    private String account_name;
    private String create_time;
    private String finish_time;
    private Integer actual_amount;
    private Integer total_money;
    private OrderStatus order_status;

    public OrderStatus getOrder_statusDesc() {
        return order_status;
    }
    //为了浏览订单使用
    public String getOrder_status() {
        return order_status.getDesc();
    }

    //订单项的内容也需要存储到当前订单内
    public List<OrderItem> orderItemList = new ArrayList<>();

    public double getTotal_money() {
        return total_money * 1.0 / 100;
    }
    public int getTotalMoneyInt() {
        return total_money;
    }
    //浏览订单会用到
    public double getActual_amount() {
        return actual_amount * 1.0/100;
    }
    public int getActualAmountInt() {
        return actual_amount;
    }

    //优惠
    public double getDiscount() {
        //return (this.getTotal_money() - this.getActual_amount());
        return (this.getTotalMoneyInt() - this.getActualAmountInt())*1.00 / 100;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getAccount_id() {
        return account_id;
    }

    public void setAccount_id(Integer account_id) {
        this.account_id = account_id;
    }

    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getFinish_time() {
        return finish_time;
    }

    public void setFinish_time(String finish_time) {
        this.finish_time = finish_time;
    }

    public void setActual_amount(Integer actual_amount) {
        this.actual_amount = actual_amount;
    }

    public void setTotal_money(Integer total_money) {
        this.total_money = total_money;
    }

    public void setOrder_status(OrderStatus order_status) {
        this.order_status = order_status;
    }

    public List<OrderItem> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }
}
