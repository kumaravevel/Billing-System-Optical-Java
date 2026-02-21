package com.BillSystem.Appticals.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "frame")
public class Frame {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

    @Column(name = "frame_code", unique = true)
    private String frameCode;
    
    @Column(name = "frame_name")
    private String frameName;
    
    @Column(name = "frame_type")
    private String frameType; 
    
    @Column(name = "frame_size")
    private String frameSize; 
    
    @Column(name = "frame_price")
    private BigDecimal framePrice;
    
    @Column(name = "glass_price")
    private BigDecimal glassPrice;
    
    @Column(name = "fitting_charges")
    private BigDecimal fittingCharges;
    
    /**
	 * @param id
	 * @param frameCode
	 * @param frameName
	 * @param frameType
	 * @param frameSize
	 * @param framePrice
	 * @param glassPrice
	 * @param fittingCharges
	 * @param taxRate
	 * @param isAvailable
	 * @param createdAt
	 */
	public Frame(Long id, String frameCode, String frameName, String frameType, String frameSize, BigDecimal framePrice,
			BigDecimal glassPrice, BigDecimal fittingCharges, BigDecimal taxRate, Boolean isAvailable,
			LocalDateTime createdAt) {
		super();
		this.id = id;
		this.frameCode = frameCode;
		this.frameName = frameName;
		this.frameType = frameType;
		this.frameSize = frameSize;
		this.framePrice = framePrice;
		this.glassPrice = glassPrice;
		this.fittingCharges = fittingCharges;
		this.taxRate = taxRate;
		this.isAvailable = isAvailable;
		this.createdAt = createdAt;
	}

	@Column(name = "tax_rate")
    private BigDecimal taxRate;
    
    @Column(name = "is_available")
    private Boolean isAvailable;
    
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
}
