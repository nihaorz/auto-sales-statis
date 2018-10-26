package com.nihaorz.autosalestatis.model;

/**
 * @author 王睿
 * @date 2018/10/26 14:05
 * 车辆销量数据对象
 */
public class CarScalesData {

    /**
     * 车辆名称
     */
    private String carName;

    /**
     * 销量数据时间
     */
    private String time;

    /**
     * 销量总数
     */
    private int count;

    private CarScalesData(){

    }

    public static class Builder {
        private String carName;
        private String time;
        private int count;

        public Builder carName(String carName){
            this.carName = carName;
            return this;
        }

        public Builder time(String time){
            this.time = time;
            return this;
        }

        public Builder count(int count){
            this.count = count;
            return this;
        }

        public CarScalesData build(){
            CarScalesData carScalesData = new CarScalesData();
            carScalesData.carName = this.carName;
            carScalesData.time = this.time;
            carScalesData.count = this.count;
            return carScalesData;
        }

    }

    public String getCarName() {
        return carName;
    }

    public String getTime() {
        return time;
    }

    public int getCount() {
        return count;
    }
}
