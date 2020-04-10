package Model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CarManagement {

    public void display(Connection con) {
        try {
            Statement statement = con.createStatement();

            String query = "SELECT * " +
                    "FROM cars c, brands b " +
                    "WHERE b.id = c.brand_id";

            ResultSet rs = statement.executeQuery(query);

            System.out.printf("| %-7s| %-15s| %-15s| %-23s| %-10s| %-15s| %-15s| %-15s| %-15s| %-9s| %-15s| %-12s| %-13s| %-11s|\n", "Car_ID",
                    "BRAND", "MODEL", "ENGINE CAPACITY", "HP", "FUEL TYPE", "ODOMETER", "AUTOMATIC", "PRICE/DAY", "NR_SEATS",
                    "SEATS MATERIAL", "CRUISE CTRL", "PLATE", "REG DATE");
            System.out.println("**************************************************************************************************************" +
                    "*************************************************************************************************************");
            while(rs.next()) {
                System.out.printf("| %-7s| %-15s| %-15s| %-23s| %-10s| %-15s| %-15s| %-15s| %-15s| %-9s| %-15s| %-12s| %-13s| %-11s|\n",
                        rs.getString("c.id"),rs.getString("b.name"), rs.getString("b.model"), rs.getString("c.litre_engine"),
                        rs.getString("c.hp"), rs.getString("c.fuel_type"), rs.getString("c.odometer"),
                        rs.getString("c.automatic_gear"), rs.getString("c.price_per_day"), rs.getString("c.number_seats"),
                        rs.getString("c.seats_material"), rs.getString("c.cruise_control"), rs.getString("c.plate"),
                        rs.getString("c.registration_date"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
