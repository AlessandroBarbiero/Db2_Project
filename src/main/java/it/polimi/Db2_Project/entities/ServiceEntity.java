package it.polimi.Db2_Project.entities;

import it.polimi.Db2_Project.exceptions.WrongConfigurationException;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "service", schema = "db2_database")
public class ServiceEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable=false)
    private int id;

    @Column(name = "type", nullable=false)
    private ServiceType type;

//------------mobile phone---------------
    @Column(name = "numberOfSms", nullable=true)
    private int numberOfSms;

    @Column(name = "numberOfMinutes", nullable=true)
    private int numberOfMinutes;

    @Column(name = "extraMinutesFee", nullable=true)
    private float extraMinutesFee;

    @Column(name = "extraSmsFee", nullable=true)
    private float extraSmsFee;

//-------------internet-----------------
    @Column(name = "numberOfGb", nullable=true)
    private int numberOfGb;

    @Column(name = "extraGbFee", nullable=true)
    private float extraGbFee;

//---------------------------------------

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable (name="service_composition",
            joinColumns = @JoinColumn(name="serviceId"),
            inverseJoinColumns= @JoinColumn (name="servicePackageId"))
    private List<ServicePackageEntity> servicePackages;


    public ServiceEntity() {
    }

    /**
     * builds the service passing the type and relative attributes
     * @throws WrongConfigurationException if you try to assign a value to an attribute that need to be null for the given type
     * @param type the type of the service
     * @param numberOfSms put null if not MOBILE_PHONE
     * @param numberOfMinutes put null if not MOBILE_PHONE
     * @param extraMinutesFee put null if not MOBILE_PHONE
     * @param extraSmsFee put null if not MOBILE_PHONE
     * @param numberOfGb put null if not FIXED_INTERNET or MOBILE_INTERNET
     * @param extraGbFee put null if not FIXED_INTERNET or MOBILE_INTERNET
     */
    public ServiceEntity(ServiceType type, Integer numberOfSms, Integer numberOfMinutes, Float extraMinutesFee, Float extraSmsFee, Integer numberOfGb, Float extraGbFee) throws WrongConfigurationException {
        this.type = type;
        switch (type) {
            case FIXED_PHONE:
                if (numberOfSms != null || numberOfMinutes != null || extraMinutesFee != null || extraSmsFee != null || numberOfGb != null || extraGbFee != null)
                    throw new WrongConfigurationException();
                break;
            case MOBILE_PHONE:
                if (numberOfSms == null || numberOfMinutes == null || extraMinutesFee == null || extraSmsFee == null || numberOfGb != null || extraGbFee != null)
                    throw new WrongConfigurationException();
                this.numberOfSms = numberOfSms;
                this.numberOfMinutes = numberOfMinutes;
                this.extraMinutesFee = extraMinutesFee;
                this.extraSmsFee = extraSmsFee;
                break;
            case FIXED_INTERNET:
            case MOBILE_INTERNET:
                if (numberOfSms != null || numberOfMinutes != null || extraMinutesFee != null || extraSmsFee != null || numberOfGb == null || extraGbFee == null)
                    throw new WrongConfigurationException();
                this.numberOfGb = numberOfGb;
                this.extraGbFee = extraGbFee;
                break;
        }
    }

//%%%%%%%%%%%%%%%%%%% GETTERS %%%%%%%%%%%%%%%%%%%%

    public int getId() {
        return id;
    }

    public ServiceType getType() {
        return type;
    }

    public int getNumberOfSms() {
        return numberOfSms;
    }

    public int getNumberOfMinutes() {
        return numberOfMinutes;
    }

    public float getExtraMinutesFee() {
        return extraMinutesFee;
    }

    public float getExtraSmsFee() {
        return extraSmsFee;
    }

    public int getNumberOfGb() {
        return numberOfGb;
    }

    public float getExtraGbFee() {
        return extraGbFee;
    }

    public List<ServicePackageEntity> getServicePackages() {
        return servicePackages;
    }

//%%%%%%%%%%%%%%%%%%% SETTERS %%%%%%%%%%%%%%%%%%%%


    public void setType(ServiceType type) {
        this.type = type;
    }

    public void setNumberOfSms(int numberOfSms) {
        if(type.equals(ServiceType.MOBILE_PHONE))
            this.numberOfSms = numberOfSms;
    }

    public void setNumberOfMinutes(int numberOfMinutes) {
        if(type.equals(ServiceType.MOBILE_PHONE))
            this.numberOfMinutes = numberOfMinutes;
    }

    public void setExtraMinutesFee(float extraMinutesFee) {
        if(type.equals(ServiceType.MOBILE_PHONE))
            this.extraMinutesFee = extraMinutesFee;
    }

    public void setExtraSmsFee(float extraSmsFee) {
        if(type.equals(ServiceType.MOBILE_PHONE))
            this.extraSmsFee = extraSmsFee;
    }

    public void setNumberOfGb(int numberOfGb) {
        if(type.equals(ServiceType.MOBILE_INTERNET) || type.equals(ServiceType.FIXED_INTERNET))
            this.numberOfGb = numberOfGb;
    }

    public void setExtraGbFee(float extraGbFee) {
        if(type.equals(ServiceType.MOBILE_INTERNET) || type.equals(ServiceType.FIXED_INTERNET))
            this.extraGbFee = extraGbFee;
    }

}
