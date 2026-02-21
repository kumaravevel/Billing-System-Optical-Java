package com.BillSystem.Appticals.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Lens {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	   
    @Column(name = "lens_code", unique = true)
    private String lensCode;
    
    @Column(name = "lens_name")
    private String lensName;
    
    @Column(name = "lens_type")
    private String lensType; // SINGLE_VISION, BIFOCAL, PROGRESSIVE
    
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLensCode() {
		return lensCode;
	}

	public void setLensCode(String lensCode) {
		this.lensCode = lensCode;
	}

	public String getLensName() {
		return lensName;
	}

	public void setLensName(String lensName) {
		this.lensName = lensName;
	}

	public String getLensType() {
		return lensType;
	}

	public void setLensType(String lensType) {
		this.lensType = lensType;
	}

	public String getLensMaterial() {
		return lensMaterial;
	}

	public void setLensMaterial(String lensMaterial) {
		this.lensMaterial = lensMaterial;
	}

	public BigDecimal getLensPrice() {
		return lensPrice;
	}

	public void setLensPrice(BigDecimal lensPrice) {
		this.lensPrice = lensPrice;
	}

	public BigDecimal getCoatingPrice() {
		return coatingPrice;
	}

	public void setCoatingPrice(BigDecimal coatingPrice) {
		this.coatingPrice = coatingPrice;
	}

	public BigDecimal getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(BigDecimal taxRate) {
		this.taxRate = taxRate;
	}

	public Boolean getIsAvailable() {
		return isAvailable;
	}

	public void setIsAvailable(Boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	@Column(name = "lens_material")
    private String lensMaterial; 
    
    @Column(name = "lens_price")
    private BigDecimal lensPrice;
    
    @Column(name = "coating_price")
    private BigDecimal coatingPrice;
    
    @Column(name = "tax_rate")
    private BigDecimal taxRate;
    
    @Column(name = "is_available")
    private Boolean isAvailable;
    
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

	/**
	 * @param id
	 * @param lensCode
	 * @param lensName
	 * @param lensType
	 * @param lensMaterial
	 * @param lensPrice
	 * @param coatingPrice
	 * @param taxRate
	 * @param isAvailable
	 * @param createdAt
	 */
	public Lens(Long id, String lensCode, String lensName, String lensType, String lensMaterial, BigDecimal lensPrice,
			BigDecimal coatingPrice, BigDecimal taxRate, Boolean isAvailable, LocalDateTime createdAt) {
		super();
		this.id = id;
		this.lensCode = lensCode;
		this.lensName = lensName;
		this.lensType = lensType;
		this.lensMaterial = lensMaterial;
		this.lensPrice = lensPrice;
		this.coatingPrice = coatingPrice;
		this.taxRate = taxRate;
		this.isAvailable = isAvailable;
		this.createdAt = createdAt;
	}

}
