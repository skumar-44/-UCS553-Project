package model;

public class Host extends Person {

    private String organization;
    private String contactNumber;

    public Host(int id, String name, String email,
            String organization, String contactNumber) {

        super(id, name, email);
        this.organization = organization;
        this.contactNumber = contactNumber;
    }

    public String getOrganization() {
        return organization;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    @Override
    public String getRole() {
        return "Host";
    }
}
