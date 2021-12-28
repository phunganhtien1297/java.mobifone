package model;

public class KhachHang {
    String id, firstname, lastname, room, valid;

    public KhachHang(String id, String firstname, String lastname, String room, String valid) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.room = room;
        this.valid = valid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid;
    }
    
    
}
