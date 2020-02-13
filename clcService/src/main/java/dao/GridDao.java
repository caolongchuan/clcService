package dao;

import entiry.Grid;
import entiry.Merchandise;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * 控制柜子数据库
 */
public class GridDao {

    private String mGrid_Name;

    public GridDao(String name) {
        mGrid_Name = name;
    }

    public ArrayList<Grid> getAll(Connection con) throws SQLException {
        ArrayList<Grid> result = new ArrayList<Grid>();
        String sql = "select * from " + mGrid_Name;
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Grid resultGrid = new Grid();
            resultGrid.setGrid_number(rs.getInt("grid_number"));
            resultGrid.setMerchandise_id(rs.getInt("merchandise_id"));
            resultGrid.setGrid_status(rs.getInt("grid_status"));
            result.add(resultGrid);
        }
        if (result.size() > 0) {
            return result;
        }
        return null;
    }

    /**
     * 设置制定柜子状态
     *
     * @param con
     * @param grid_no
     * @param status
     */
    public void setGridStatus(Connection con, int grid_no, int status) {
        String sql = "";
        if (con != null && mGrid_Name != null) {
            sql = "UPDATE " + mGrid_Name + " SET grid_status = " + status + " WHERE grid_number = " + grid_no;
            PreparedStatement ps = null;
            try {
                ps = con.prepareStatement(sql);
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 修改制定柜号的商品id
     *
     * @param con
     * @param grid_no
     * @param mer_no
     */
    public void setGridMerNo(Connection con, int grid_no, int mer_no) {
        String sql;
        if (con != null && mGrid_Name != null) {
            sql = "UPDATE " + mGrid_Name + " SET merchandise_id = " + mer_no + " WHERE grid_number = " + grid_no;
            PreparedStatement ps = null;
            try {
                ps = con.prepareStatement(sql);
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public int getGridMerNoByGridNo(Connection con, int grid_number) {
        String sql;
        if (con != null) {
            sql = "select * from " + mGrid_Name + " where grid_number=?";
            PreparedStatement ps = null;
            try {
                ps = con.prepareStatement(sql);
                ps.setInt(1, grid_number);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getInt("merchandise_id");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

}
