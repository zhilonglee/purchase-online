package com.zhilong.springcloud.entity;

import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "Event")
@Table(name = "event")
public class Event extends BaseEntity implements Serializable {

    @Type(type = "json")
    @Column(columnDefinition = "json")
    private Location location;

    @Type(type = "json")
    @Column(columnDefinition = "json")
    private List<Location> alternativeLocations = new ArrayList<Location>();

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<Location> getAlternativeLocations() {
        return alternativeLocations;
    }

    public void setAlternativeLocations(List<Location> alternativeLocations) {
        this.alternativeLocations = alternativeLocations;
    }
}
