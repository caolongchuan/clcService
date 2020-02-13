package entiry;

import java.util.Date;

public class Employee {
    private int id;
    private String name;
    private String password;
    private int gender;
    private String email;
    private Date birthday;



    public Employee() {
        super();
    }
    public Employee(int id, String name, String password, int gender,
                    String email, Date birthday) {
        super();
        this.id = id;
        this.name = name;
        this.password = password;
        this.gender = gender;
        this.email = email;
        this.birthday = birthday;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public int getGender() {
        return gender;
    }
    public void setGender(int gender) {
        this.gender = gender;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public Date getBirthday() {
        return birthday;
    }
    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

}
