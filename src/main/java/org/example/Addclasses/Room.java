package org.example.Addclasses;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Entity(name = "room")
public class Room {
    @Id
    private int id;
    @Column(name="name")
    private String name;
    @Column(name="maxmemb")
    private int maxmemb;

    public Room(int id, String name, int maxmemb) {
        this.id = id;
        this.name = name;
        this.maxmemb = maxmemb;
    }

    public Room(String name, int maxmemb) {
        this.name = name;
        this.maxmemb = maxmemb;
    }

    public Room() {

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

    public int getMaxmemb() {
        return maxmemb;
    }

    public void setMaxmemb(int maxmemb) {
        this.maxmemb = maxmemb;
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", maxmemb=" + maxmemb +
                '}';
    }
}
