package com.artogether.common.business_member;

import com.artogether.common.business_perm.BusinessPerm;
import com.artogether.common.platform_msg.Platform_msg;
import com.artogether.event.event.Event;
import com.artogether.product.prd_coup.PrdCoup;
import com.artogether.product.product.Product;
import com.artogether.venue.venue.Venue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "business_member")
public class BusinessMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(nullable = false)
    @NotNull
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String addr;

    @Column(name = "tax_id", nullable = false)
    private String taxId;

    @Column(name = "start_date", updatable = false, insertable = false)
    @CreationTimestamp
    private Timestamp startDate;

    @Column(name = "approve_date")
    private Timestamp approveDate;


    private Byte status;


    //以下為hibernate一對多設定

    /*    Product    */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "businessMember", cascade = CascadeType.ALL)
    private Set<Product> products;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "businessMember", cascade = CascadeType.ALL)
    private Set<PrdCoup> prdCoup;


    /*    Venue    */

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "businessMember", cascade = CascadeType.ALL)
    private Set<Venue> venues;


    /*    Event    */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "businessMember", cascade = CascadeType.ALL)
    private Set<Event> events;


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "businessMember", cascade = CascadeType.ALL)
    private Set<Platform_msg> platformMsgs;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "businessMember", cascade = CascadeType.ALL)
    private Set<BusinessPerm> businessPerms;


    public int hashCode() {
        return Objects.hash(this.id);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj != null && this.getClass() == obj.getClass()) {
            BusinessMember that = (BusinessMember)obj;
            return Objects.equals(this.id, that.id);
        } else {
            return false;
        }
    }
}
