package com.nihaorz.autosalestatis.action;

import com.nihaorz.autosalestatis.model.CarScalesData;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 王睿
 * @date 2018/10/26 13:54
 */
@RequestMapping("/")
@Controller
public class IndexAction {

    /**
     * 太平洋汽车数据接口URL,例如:https://price.pcauto.com.cn/top/sales/s1-t1-y2018-m9.html
     */
    private static final String JIAOCHE_URL_PREFIX = "https://price.pcauto.com.cn/top/sales/s1-t1-y%s-m%s.html";

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    /**
     * 获取车型销售数据
     *
     * @param nameList 车型名称集合
     * @param start    开始时间，yyyy-m格式，例如2018-2
     * @param end      结束时间，yyyy-m格式，例如2018-9
     * @return
     */
    @ResponseBody
    @RequestMapping("/getSalesData")
    public List<CarScalesData> getSalesData(
            @RequestParam("name") List<String> nameList,
            @RequestParam("start") String start,
            @RequestParam("end") String end) {
        List<CarScalesData> list = new ArrayList<>();
        String splitStr = "-";
        int yearStart = Integer.parseInt(start.split(splitStr)[0]);
        int monthStart = Integer.parseInt(start.split(splitStr)[1]);
        int yearEnd = Integer.parseInt(end.split(splitStr)[0]);
        int monthEnd = Integer.parseInt(end.split(splitStr)[1]);
        for (int year = yearStart; year <= yearEnd; year++) {
            for (int month = 1; month <= 12; month++) {
                boolean isQuery = false;
                if (yearStart == yearEnd) {
                    if (month >= monthStart && month <= monthEnd) {
                        isQuery = true;
                    }
                } else {
                    if (year == yearStart) {
                        if (month >= monthStart) {
                            isQuery = true;
                        }
                    } else if (year == yearEnd) {
                        if (month <= monthEnd) {
                            isQuery = true;
                        }
                    } else {
                        isQuery = true;
                    }
                }
                if (isQuery) {
                    String url = String.format(JIAOCHE_URL_PREFIX, year, month);
                    try {
                        Document doc = Jsoup.connect(url).get();
                        Element element = doc.select(".table-sl").first();
                        Elements elements = element.select("tr");
                        for (String name : nameList) {
                            int salesNum = 0;
                            String[] nameArr = name.split("\\|");
                            for (Element tr : elements) {
                                /**
                                 * 车型
                                 */
                                String carName = tr.select(".brand").text();
                                for (String s : nameArr) {
                                    if (carName.equalsIgnoreCase(s)) {
                                        /**
                                         * 销量
                                         */
                                        salesNum += Integer.parseInt(tr.select(".salesNum").text());
                                    }
                                }
                            }
                            CarScalesData carScalesData = new CarScalesData.Builder().carName(name).count(salesNum).time(year + splitStr + month).build();
                            list.add(carScalesData);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
        return list;
    }


}
