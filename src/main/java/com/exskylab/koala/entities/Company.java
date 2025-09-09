package com.exskylab.koala.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "companies")
public class Company extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "logo_id", referencedColumnName = "id")
    private Image logo;

    @Column(name = "website")
    private String website;

    @Column(name = "description")
    private String description;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    @Column(name = "tax_number")
    private String taxNumber;

    @Enumerated(EnumType.STRING)
    private CompanyType type;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CompanyContact> contacts = new ArrayList<>();

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CompanyContactInvitation> contactInvitations = new ArrayList<>();

    @Column(name = "is_approved")
    private boolean isApproved;

    @Column(name = "iyzico_submerchant_key")
    private String iyzicoSubmerchantKey;

    @Column(name = "iyzico_submerchant_external_id")
    private String iyzicoSubmerchantExternalId;

    public void addContact(CompanyContact contact){
        this.contacts.add(contact);
        contact.setCompany(this);
    }

    public void removeContact(CompanyContact contact){
        this.contacts.remove(contact);
        contact.setCompany(null);
    }


}
