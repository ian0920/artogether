package com.artogether.event.my_evt_coup;

import com.artogether.common.member.Member;
import com.artogether.event.evt_coup.EvtCoup;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "my_evt_coup")
@IdClass(MyEvtCoup.Composite.class)
public class MyEvtCoup {

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member member;

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "evt_coup_id", referencedColumnName = "id")
    private EvtCoup evtCoup;


    private Byte status;

    @Column(name = "receive_date")
    private Timestamp receiveDate;

    @Column(name = "use_date")
    private Timestamp useDate;


    public Composite getComposite() {
        return new Composite(member.getId(), evtCoup.getId());
    }

    public void setComposite(Composite composite) {
        this.member.setId(composite.member);
        this.evtCoup.setId(composite.evtCoup);
    }

    /*
        需要宣告一個有包含複合主鍵屬性的類別，並一定要實作 java.io.Serializable 介面
        一定要有無參數建構子
        一定要 override 此類別的 hashCode() 與 equals() 方法
     */

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class Composite implements Serializable {
        private static final long serialVersionUID = 1L;


        private Integer member;
        private Integer evtCoup;


    }


}

