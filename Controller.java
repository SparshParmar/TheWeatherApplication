package sample;

import javafx.fxml.FXML;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.time.chrono.ChronoLocalDate;
import java.util.Date;




public  class Controller {

    private static final String CONNECTION_STRING = "jdbc:sqlite:D:\\ProgrammingParadigms\\theWeatherData.db" ;
    public String Summary;
    public String PrecipType;
    public int Temperature;
    public int FeelsLike;
    public int Humidity;
    public int WindSpeed;
    public int WindBearing;
    public int Visibility;
    public int LoudCover;
    public float Pressure;
    public String DailySummary;

    public static Connection conn;
    public static boolean open() {
        try {
            conn = DriverManager.getConnection(CONNECTION_STRING);
            System.out.println("Opened successfully :");
            return true;
        } catch (SQLException e) {
            System.out.println("Could not open: " + e.getMessage());
            return false;
        }
    }

    public void close() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println("Cloudnt close : " + e.getMessage());
        }
    }


    LocalDate d1 = LocalDate.parse("2006-04-01");
    LocalDate d2 = LocalDate.parse("2017-11-27");
    @FXML
    private ComboBox hour;
    @FXML
    private TextArea  theResult;
    @FXML
    private DatePicker datePicker;
    @FXML
    public void processResult() {
        theResult.setText(null);
        LocalDate theDate = datePicker.getValue();
        if(theDate==(null)||theDate.isBefore(d1) || theDate.isAfter(d2) || hour.getValue()==null){
            theResult.setText("The Date is Out of Range or hour not entered. Please enter the Date in Between 2006-04-01 and 2017-11-27 :");
        }
        else{
            theResult.setText(null);
            StringBuilder sb = new StringBuilder("SELECT * FROM History WHERE FormattedDate=\"");
            sb.append(theDate);
            sb.append(" "+ hour.getValue()+":00.000 +0100\"");


            if(!(Controller.open())){
                System.out.println("Cant open the dataSource :");
                return;
            }
            else{
                System.out.println("DataSource opened successfully :");
            }
            System.out.println(sb.toString());
            try (Statement statement = conn.createStatement();
                 ResultSet results = statement.executeQuery(sb.toString());) {
//
                Summary=results.getString(2);
                PrecipType=results.getString(3);
                Temperature=results.getInt(4);
                FeelsLike=results.getInt(5);
                Humidity=results.getInt(6);
                WindSpeed=results.getInt(7);
                WindBearing=results.getInt(8);
                Visibility=results.getInt(9);
                LoudCover=results.getInt(10);
                Pressure=results.getInt(11);
                DailySummary=results.getString(12);

                theResult.setText(null);
                theResult.setStyle("-fx-background-color: green");
                theResult.setFont(Font.font("Quillian", FontWeight.BOLD, 25));
                theResult.appendText("\nThe Weather ForeCast \n");
               // theResult.setFont(Font.font("Verdana", FontWeight.BOLD, 5));
                theResult.appendText("Summary : "+Summary +"\n");
                theResult.appendText("Precipitation : "+PrecipType +"\n");
                theResult.appendText("Temperature : "+Temperature +" C\n");
                theResult.appendText("FeelsLike : "+FeelsLike +" C\n");
                theResult.appendText("Humidity : "+Humidity +" \n");
                theResult.appendText("WindSpeed : "+WindSpeed +" Km/hour\n");
                theResult.appendText("WindBearing : "+WindBearing +" Degrees\n");
                theResult.appendText("Visibility : "+Visibility +" Km\n");
                theResult.appendText("LoudCover : "+LoudCover +"\n");
                theResult.appendText("Pressure : "+  (float)Pressure +" Milli-bar\n");
                theResult.appendText("DailySummary : "+DailySummary +"\n");
//                System.out.println(results.getString(1) + " "+ results.getString(2));
                DailySummary = "Sparsh";

               } catch (SQLException e) {
                System.out.println("Query Failed : "+ e.getMessage());
                theResult.setText("Unable to find the data for this date in the database. Please press clear and find the another date :");
            }

        }
    }

    public void clear(){
        datePicker.setValue(null);
        theResult.setText(null);
        hour.setValue(null);
    }
}
