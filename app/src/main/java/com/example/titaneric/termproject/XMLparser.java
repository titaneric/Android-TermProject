package com.example.titaneric.termproject;

/**
 * Created by TitanEric on 12/25/2016.
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

        import javax.xml.parsers.DocumentBuilderFactory;
        import javax.xml.parsers.DocumentBuilder;
        import org.w3c.dom.Document;
        import org.w3c.dom.NodeList;
        import org.w3c.dom.Node;
        import org.w3c.dom.Element;
        import java.net.*;



/**
 *
 * @author TitanEric
 */
public class XMLparser {
    private WeatherData[] WDArray;

    public XMLparser() {
        // TODO code application logic here
        String mykey = "CWB-973DBD86-AE7F-4B31-A93B-487CE21DD7A9";
        String dataIndex = "F-C0032-001";
        String str = String.format("http://opendata.cwb.gov.tw/opendataapi?dataid=%s&authorizationkey=%s", dataIndex, mykey);

        try{
            URL url = new URL(str);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();


            DocumentBuilderFactory dbFactory
                    = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(connection.getInputStream());
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("locationName");
            NodeList wList = doc.getElementsByTagName("location");
            int size = doc.getElementsByTagName("parameterName").getLength() / 3;
            this.WDArray = new WeatherData[size];
            for(int i=0;i<size;i++){
                this.WDArray[i] = new WeatherData();
            }
            int index = 0;
            for(int i=0;i<nList.getLength();i++){
                Node location = nList.item(i);
                String loc = location.getTextContent();
                Element locElement = (Element)wList.item(i);
                NodeList paraList = locElement.getElementsByTagName("parameterName");
                String weather = "", maxT = "", minT = "", comfortIndex = "", dropPercent = "", timeRange = "";

                //System.out.println(location.getTextContent() + "-" + paraList.getLength());
                for(int k=0;k<paraList.getLength();k++){
                    /*
                    //today night
                    if(k%3 == 0){

                        timeRange = "今晚明晨";
                        //System.out.println(timeRange);
                        //the weather
                        if((k-k%3)/3 == 0){
                            weather = paraList.item(k).getTextContent();
                            //System.out.println("Weather " + weather);
                        }
                        //the max temperature
                        else if((k-k%3)/3 == 1){
                            maxT = paraList.item(k).getTextContent();
                            //System.out.println("max " + maxT);
                        }
                        //the min temperature
                        else if((k-k%3)/3 == 2){
                            minT = paraList.item(k).getTextContent();
                            //System.out.println("min " + minT);
                        }
                        //the comfort index
                        else if((k-k%3)/3 == 3){
                            comfortIndex = paraList.item(k).getTextContent();
                            //System.out.println("comfortIndex " + comfortIndex);
                        }
                        //the drop percent
                        else if((k-k%3)/3 == 4){
                            dropPercent = paraList.item(k).getTextContent();
                            //System.out.println("dropPercent " + dropPercent);
                        }
                        //WDArray[index] = new WeatherData(location.getTextContent(), weather, timeRange, maxT, minT, comfortIndex, dropPercent);
                        //index++;
                    }
                    */
                    //day
                    if(k%3 == 1){


                        timeRange = "明日白天";
                        if((k-k%3)/3 == 0){
                            weather = paraList.item(k).getTextContent();
                        }
                        //the max temperature
                        else if((k-k%3)/3 == 1){
                            maxT = paraList.item(k).getTextContent();
                        }
                        //the min temperature
                        else if((k-k%3)/3 == 2){
                            minT = paraList.item(k).getTextContent();
                        }
                        //the comfort index
                        else if((k-k%3)/3 == 3){
                            comfortIndex = paraList.item(k).getTextContent();
                        }
                        //the drop percent
                        else if((k-k%3)/3== 4){
                            dropPercent = paraList.item(k).getTextContent();
                            this.WDArray[index] = new WeatherData(loc, weather, timeRange, maxT, minT, comfortIndex, dropPercent);
                            index++;
                        }

                    }
                    /*
                    //tomorrow night
                    if(k %3 == 2){


                        timeRange = "明日晚上";
                        if((k-k%3)/3 == 0){
                            weather = paraList.item(k).getTextContent();
                        }
                        //the max temperature
                        else if((k-k%3)/3 == 1){
                            maxT = paraList.item(k).getTextContent();
                        }
                        //the min temperature
                        else if((k-k%3)/3 == 2){
                            minT = paraList.item(k).getTextContent();
                        }
                        //the comfort index
                        else if((k-k%3)/3 == 3){
                            comfortIndex = paraList.item(k).getTextContent();
                        }
                        //the drop percent
                        else if((k-k%3)/3 == 4){
                            dropPercent = paraList.item(k).getTextContent();
                        }
                        //WDArray[index] = new WeatherData(location.getTextContent(), weather, timeRange, maxT, minT, comfortIndex, dropPercent);
                        //index++;
                    }
                    */
                }

            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public WeatherData[] getWeatherDataArray(){
        return this.WDArray;
    }

}



