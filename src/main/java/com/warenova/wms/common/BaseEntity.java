package com.warenova.wms.common;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;

// ================================================
// BASE ENTITY
// ================================================
// PURPOSE:
// This class is the PARENT of ALL entities
// in WareNova WMS system.
//
// Every database table will have these 4 columns:
// 1. created_at  → when was this record created
// 2. updated_at  → when was this record last changed
// 3. created_by  → who created this record
// 4. updated_by  → who last changed this record
//
// HOW IT WORKS:
// @MappedSuperclass → tells JPA this is a
//   parent class, not a separate table
//
// @EntityListeners → tells Spring to watch
//   this entity and auto fill audit fields
//
// @EnableJpaAuditing in AuditConfig.java
//   activates this whole mechanism
//
// USAGE:
// public class Item extends BaseEntity { }
// public class Location extends BaseEntity { }
// All entities just extend this class ✅
// ================================================

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    // ================================================
    // CREATED AT
    // ================================================
    // Automatically set when record is first saved
    // updatable = false → Spring never changes this
    // after first insert
    // Example: 2024-01-15 10:30:00
    // ================================================
    @CreatedDate
    @Column(
            name = "created_at",
            nullable = false,
            updatable = false   // once set, never changes
    )
    private LocalDateTime createdAt;

    // ================================================
    // UPDATED AT
    // ================================================
    // Automatically updated every time record is saved
    // First insert → same as created_at
    // Any update → changes to current timestamp
    // Example: 2024-01-20 15:45:00
    // ================================================
    @LastModifiedDate
    @Column(
            name = "updated_at",
            nullable = false
    )
    private LocalDateTime updatedAt;

    // ================================================
    // CREATED BY
    // ================================================
    // Automatically set to logged in username
    // when record is first created
    // updatable = false → never changes after insert
    // Comes from AuditConfig → AuditorAware bean
    // Example: "admin" or "john.doe"
    // ================================================
    @CreatedBy
    @Column(
            name = "created_by",
            updatable = false   // once set, never changes
    )
    private String createdBy;

    // ================================================
    // UPDATED BY
    // ================================================
    // Automatically set to logged in username
    // every time record is saved or updated
    // Example: "supervisor" or "warehouse.manager"
    // ================================================
    @LastModifiedBy
    @Column(name = "updated_by")
    private String updatedBy;
}