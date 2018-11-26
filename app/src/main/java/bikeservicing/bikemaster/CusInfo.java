package bikeservicing.bikemaster;

class CusInfo {

    public String name, address, phoneNo, dateToSet, timeSlot;

    CusInfo() {

    }

    CusInfo(String name, String address, String phoneNo, String dateToSet, String timeSlot) {
        this.name = name;
        this.address = address;

        this.phoneNo = phoneNo;
        this.dateToSet = dateToSet;
        this.timeSlot = timeSlot;
    }

    public String getDateToSet() {
        return dateToSet;
    }

    public void setDateToSet(String dateToSet) {
        this.dateToSet = dateToSet;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }
}
