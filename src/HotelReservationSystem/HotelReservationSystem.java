package HotelReservationSystem;


import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.Scanner;
import java.sql.Statement;
import java.sql.ResultSet;

public class HotelReservationSystem {
    public static final String url = "jdbc:mysql://127.0.0.1:3306/hotel";
    public static final String username = "root";
    public static final String password = "MySQL@@12";

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            Connection connection = DriverManager.getConnection(url,username,password);
            while(true){
                System.out.println();
                System.out.println("HOTEL MANAGEMENT SYSTEM");
                Scanner scanner = new Scanner(System.in);
                System.out.println("1. Reserve a room.");
                System.out.println("2. View Reservations.");
                System.out.println("3. Get room number.");
                System.out.println("4. Update reservations.");
                System.out.println("5. Delete Reservations.");
                System.out.println("0. Exit");
                System.out.print("Choose an option");
                int choice = scanner.nextInt();
                switch(choice){
                    case 0:exit(); scanner.close();return;
                    case 1:reserveRoom(connection,scanner);break;
                    case 2:viewReservations(connection);break;
                    case 3:getRoomNumber(connection,scanner);break;
                    case 4:updateReservation(connection,scanner);break;
                    case 5:deleteReservation(connection,scanner);break;
                    default:
                        System.out.println("INVALID CHOICE TRY AGAIN");
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void reserveRoom(Connection connection,Scanner scanner){
        try{
        System.out.print("Enter guest name: ");
        String guestName = scanner.next();
        scanner.nextLine();
        System.out.print("Enter room number: ");
        int roomNumber = scanner.nextInt();
        System.out.print("Enter contact number: ");
        String contactNumber = scanner.next();
        String sql = "INSERT INTO reservations (guest_name, room_number, contact_number) " +
                "VALUES ('" + guestName + "', " + roomNumber + ", '" + contactNumber + "')";
        try (Statement statement = connection.createStatement()) {
            int affectedRows = statement.executeUpdate(sql);

            if (affectedRows > 0) {
                System.out.println("Reservation successful!");
            } else {
                System.out.println("Reservation failed.");
            }
        }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void viewReservations(Connection connection){
        String query="select reservation_id,guest_name,room_number,contact_number,reservation_date from reservations";
        try(Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query)){
            System.out.println("CURRENT RESERVATIONS: ");
            while(resultSet.next()){
                int reservationid=resultSet.getInt("reservation_id");
                String guestname=resultSet.getString("guest_name");
                int roomnumber=resultSet.getInt("room_number");
                String contactnumber=resultSet.getString("contact_number");
                String reservationdate=resultSet.getTimestamp("reservation_date").toString();
                System.out.printf("|%14d |%13s |%12d |%11s |%10s \n",reservationid,guestname,roomnumber,contactnumber,reservationdate);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void getRoomNumber(Connection connection, Scanner scanner){
        try{
            System.out.print("ENTER RESERVATION ID: ");
            int reservationid=scanner.nextInt();
            System.out.print("Enter guest name: ");
            String guestname=scanner.next();
            String query="select room_number from reservations "+"where reservation_id = "+reservationid+" and guest_name = '"+guestname+"'";
            try(Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query)
            ){
                if(resultSet.next()){
                    int roomnumber=resultSet.getInt("room_number");
                    System.out.println("Room number for reservation id: "+reservationid+" and guest "+guestname+" is: "+roomnumber);

                }else{
                    System.out.println("RESERVATION NOT FOUND FOR GIVEN ID AND GUEST NAME.");
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public static void updateReservation(Connection connection, Scanner scanner){
        try {
            System.out.print("Enter reservation ID to update: ");
            int reservationId = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            if (!reservationExists(connection, reservationId)) {
                System.out.println("Reservation not found for the given ID.");
                return;
            }

            System.out.print("Enter new guest name: ");
            String newGuestName = scanner.nextLine();
            System.out.print("Enter new room number: ");
            int newRoomNumber = scanner.nextInt();
            System.out.print("Enter new contact number: ");
            String newContactNumber = scanner.next();

            String sql = "UPDATE reservations SET guest_name = '" + newGuestName + "', " +
                    "room_number = " + newRoomNumber + ", " +
                    "contact_number = '" + newContactNumber + "' " +
                    "WHERE reservation_id = " + reservationId;

            try (Statement statement = connection.createStatement()) {
                int affectedRows = statement.executeUpdate(sql);

                if (affectedRows > 0) {
                    System.out.println("Reservation updated successfully!");
                } else {
                    System.out.println("Reservation update failed.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static boolean reservationExists(Connection connection,int reservationId){
        try {
            String sql = "SELECT reservation_id FROM reservations WHERE reservation_id = " + reservationId;

            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {

                return resultSet.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static void deleteReservation(Connection connection,Scanner scanner){
        try {
            System.out.print("Enter reservation ID to delete: ");
            int reservationId = scanner.nextInt();

            if (!reservationExists(connection, reservationId)) {
                System.out.println("Reservation not found for the given ID.");
                return;
            }

            String sql = "DELETE FROM reservations WHERE reservation_id = " + reservationId;

            try (Statement statement = connection.createStatement()) {
                int affectedRows = statement.executeUpdate(sql);

                if (affectedRows > 0) {
                    System.out.println("Reservation deleted successfully!");
                } else {
                    System.out.println("Reservation deletion failed.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void exit() throws InterruptedException{
        System.out.print("EXITING SYSTEM");
        int i=5;
        while(i!=0){
            System.out.print(".");
            Thread.sleep(500);
            i--;
        }
        System.out.println();
        System.out.println("THANK YOU FOR USING HOTEL RESERVATION SYSTEM.");
    }
}
