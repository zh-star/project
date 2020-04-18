package entity;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: GAOBO
 * Date: 2020-02-12
 * Time: 9:06
 */
@Data
public class Goods {
    private Integer id;
    private String name;
    private String introduce;
    private Integer stock;
    private String unit;//单位
    private Integer price;//存入数据库为整数
    private Integer discount;//88  0.88

    private Integer buyGoodsNum;//记录需要购买当前商品的数量

    public double getPrice() {
        return price * 1.0 / 100;
    }
    public int getPriceInt() {
        return price;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public Integer getBuyGoodsNum() {
        return buyGoodsNum;
    }

    public void setBuyGoodsNum(Integer buyGoodsNum) {
        this.buyGoodsNum = buyGoodsNum;
    }
}
