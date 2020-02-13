package result_json;

import entiry.Merchandise;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 服务器向浏览器发送的数据结果 商品列表
 */
public class Mer_Det_Result {

    private String status;
    private int amount;
    private ArrayList<Integer> grid_status;
    private ArrayList<Merchandise> merchandises;

    public Mer_Det_Result(String status, int amount, ArrayList<Integer> grid_status, ArrayList<Merchandise> merchandises) {
        this.status = status;
        this.amount = amount;
        this.grid_status = grid_status;
        this.merchandises = merchandises;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public ArrayList<Merchandise> getMerchandise() {
        return merchandises;
    }

    public void setMerchandise(ArrayList<Merchandise> merchandises) {
        this.merchandises = merchandises;
    }

    public ArrayList<Integer> getGrid_status() {
        return grid_status;
    }

    public void setGrid_status(ArrayList<Integer> grid_status) {
        this.grid_status = grid_status;
    }

    public String toJsonString() {
        JSONObject root = new JSONObject();
        String jsonString = "";
        try {
            root.put("status", this.status);
            root.put("amount", this.amount);
            if (null != merchandises && null != grid_status) {
                JSONArray data = new JSONArray();
                for (int i = 0; i < merchandises.size(); i++) {
                    JSONObject subitem = new JSONObject();
                    subitem.put("id", merchandises.get(i).getId());
                    subitem.put("name", merchandises.get(i).getName());
                    subitem.put("price", merchandises.get(i).getPrice());
                    subitem.put("img", merchandises.get(i).getImg());
                    subitem.put("gridstatus", grid_status.get(i));

                    data.put(subitem);
                }
                root.put("merchandises", data);
            }
            //发送
            jsonString = root.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            jsonString = e.toString();
        }
        return jsonString;
    }

}
