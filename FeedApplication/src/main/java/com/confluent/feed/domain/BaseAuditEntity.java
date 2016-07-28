package com.confluent.feed.domain;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import javax.persistence.*;
import java.io.Serializable;

@MappedSuperclass
public abstract class BaseAuditEntity implements Serializable{

    @Column(name = "CREATED_D")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime createdDate;

    @Column(name = "MODIFIED_D")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime modifiedDate;

    @Column(name = "VERSION")
    @Version
    private Long version;

    @PreUpdate
    @PrePersist
    public void setAuditDates() {
        modifiedDate = new DateTime().withZone(DateTimeZone.UTC);
        if (createdDate == null) {
            createdDate = modifiedDate;
        }
    }
}
