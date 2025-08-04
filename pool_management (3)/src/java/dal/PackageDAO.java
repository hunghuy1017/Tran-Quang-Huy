/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.util.List;
import java.util.Stack;
import java.util.Vector;
import models.Packages;
import models.PackagesEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 *
 * @author LENOVO
 */
public class PackageDAO extends DBContext {

    public Vector<Packages> getAllPackage(String sql) {
        Vector<Packages> list = new Vector<>();
        PreparedStatement ptm;
        try {
            ptm = connection.prepareStatement(sql);
            ResultSet rs = ptm.executeQuery();
            while (rs.next()) {
                Packages pack = new Packages(rs.getInt(1),
                        rs.getString(2),
                        rs.getInt(3),
                        rs.getDouble(4),
                        rs.getString(5),
                        rs.getBoolean(6),
                        rs.getDate(7));
                list.add(pack);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return list;
    }

    public Packages getPackageById(String id) {
        Packages pack = null;
        String sql = "SELECT * FROM Package WHERE packageID = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                pack = new Packages(rs.getInt(1),
                        rs.getString(2),
                        rs.getInt(3),
                        rs.getDouble(4),
                        rs.getString(5),
                        rs.getBoolean(6),
                        rs.getDate(7));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pack;
    }
    public Packages getPackageByID1(int id) {
        Packages pack = null;
        String sql = "SELECT * FROM Package WHERE packageID = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                pack = new Packages(rs.getInt(1),
                        rs.getString(2),
                        rs.getInt(3),
                        rs.getDouble(4),
                        rs.getString(5),
                        rs.getBoolean(6),
                        rs.getDate(7));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pack;
    }

    public Vector<PackagesEvent> getAllPackageEvent() {
        String sql = "SELECT\n"
                + "p.PackageID,\n"
                + "    E.Title AS EventTitle,\n"
                + "    P.PackageName,\n"
                + "    P.Description AS PackageDescription,\n"
                + "    P.Price,\n"
                + "    PP.AvailabilityStatus\n"
                + "FROM\n"
                + "    Events AS E\n"
                + "JOIN\n"
                + "    PoolPackages AS PP ON E.EventID = PP.EventID\n"
                + "JOIN\n"
                + "    Package AS P ON PP.PackageID = P.PackageID\n"
                + "ORDER BY\n"
                + "    E.EventDate ASC, E.Title ASC, P.PackageName ASC;";
        Vector<PackagesEvent> list = new Vector<>();
        PreparedStatement ptm ;
        ResultSet rs;
        try{
        ptm = connection.prepareStatement(sql);
        rs = ptm.executeQuery();
        while(rs.next()){
        PackagesEvent pack = new PackagesEvent(rs.getInt(1),
                rs.getString(2),
                rs.getString(3),
                rs.getString(4),
                rs.getDouble(5),
                rs.getBoolean(6));
        list.add(pack);
        }
        }catch(SQLException e){
            System.out.println(e);
        }
        return list;
    }
public PackagesEvent getPackageEventById(String id) {
        PackagesEvent packEvent = null;
        String sql = "SELECT\n"
                + "    P.PackageID,\n"
                + "    E.Title AS EventTitle,\n"
                + "    P.PackageName,\n"
                + "    P.Description AS PackageDescription,\n"
                + "    P.Price,\n"
                + "    PP.AvailabilityStatus\n"
                + "FROM\n"
                + "    Events AS E\n"
                + "JOIN\n"
                + "    PoolPackages AS PP ON E.EventID = PP.EventID\n"
                + "JOIN\n"
                + "    Package AS P ON PP.PackageID = P.PackageID\n"
                + "WHERE P.PackageID = ?;"; // Thêm điều kiện WHERE
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    packEvent = new PackagesEvent(
                        rs.getInt("PackageID"),
                        rs.getString("EventTitle"),
                        rs.getString("PackageName"),
                        rs.getString("PackageDescription"),
                        rs.getDouble("Price"),
                        rs.getBoolean("AvailabilityStatus")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi trong getPackageEventById: " + e.getMessage());
            e.printStackTrace();
        }
        return packEvent;
    }
    public static void main(String[] args) {
        PackageDAO dao = new PackageDAO();
//        String sql = "select * from Package";
        Vector<PackagesEvent> list = dao.getAllPackageEvent();
        for(PackagesEvent a : list){
           System.out.println(a);
        }
        Packages p = dao.getPackageById("1");
        System.out.println(p);
PackagesEvent p1 = dao.getPackageEventById("7");
        System.out.println(p1);
    }
}
