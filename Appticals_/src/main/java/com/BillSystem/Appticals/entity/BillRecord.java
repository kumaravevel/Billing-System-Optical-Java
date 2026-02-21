package com.BillSystem.Appticals.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class BillRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "bill_number", unique = true)
    private String billNumber;
    
    // Customer Details
    @Column(name = "customer_name")
    private String customerName;
    
    @Column(name = "customer_phone")
    private String customerPhone;
    
    @Column(name = "customer_address")
    private String customerAddress;
    
    // Frame Details
    @ManyToOne
    @JoinColumn(name = "frame_id")
    private Frame frame;
    
    @Column(name = "frame_quantity")
    private Integer frameQuantity = 1;
    
    @Column(name = "frame_unit_price")
    private BigDecimal frameUnitPrice;
    
    // Lens Details
    @ManyToOne
    @JoinColumn(name = "lens_id")
    private Lens lens;
    
    @Column(name = "lens_quantity")
    private Integer lensQuantity = 1;
    
    @Column(name = "lens_unit_price")
    private BigDecimal lensUnitPrice;
    
    // Charges
    @Column(name = "fitting_charges")
    private BigDecimal fittingCharges = BigDecimal.ZERO;
    
    @Column(name = "other_charges")
    private BigDecimal otherCharges = BigDecimal.ZERO;
    
    @Column(name = "discount_amount")
    private BigDecimal discountAmount = BigDecimal.ZERO;
    
    // Totals
    @Column(name = "subtotal")
    private BigDecimal subtotal = BigDecimal.ZERO;
    
    @Column(name = "tax_amount")
    private BigDecimal taxAmount = BigDecimal.ZERO;
    
    @Column(name = "grand_total")
    private BigDecimal grandTotal = BigDecimal.ZERO;
    
    // Payment Info
    @Column(name = "amount_paid")
    private BigDecimal amountPaid = BigDecimal.ZERO;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_mode")
    private PaymentMode paymentMode;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;
    
    // Dates
    @Column(name = "bill_date")
    private LocalDate billDate;
    
    @Column(name = "due_date")
    private LocalDate dueDate;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BillStatus status = BillStatus.DRAFT;
    
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    // Enums
    public enum PaymentMode {
        CASH, CARD, UPI, BANK_TRANSFER
    }
    
    public enum PaymentStatus {
        PENDING, PARTIAL, PAID
    }
    
    public enum BillStatus {
        DRAFT, CONFIRMED, COMPLETED, CANCELLED
    }
    
    // Constructors
    public BillRecord() {
        // Default constructor
    }
    
    public BillRecord(String customerName, String customerPhone, Frame frame, Lens lens) {
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.frame = frame;
        this.lens = lens;
        this.billDate = LocalDate.now();
        this.dueDate = LocalDate.now().plusDays(15);
    }
    
    // Getters and Setters (VERY IMPORTANT - Add all)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getBillNumber() { return billNumber; }
    public void setBillNumber(String billNumber) { this.billNumber = billNumber; }
    
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    
    public String getCustomerPhone() { return customerPhone; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }
    
    public String getCustomerAddress() { return customerAddress; }
    public void setCustomerAddress(String customerAddress) { this.customerAddress = customerAddress; }
    
    public Frame getFrame() { return frame; }
    public void setFrame(Frame frame) { this.frame = frame; }
    
    public Integer getFrameQuantity() { return frameQuantity; }
    public void setFrameQuantity(Integer frameQuantity) { this.frameQuantity = frameQuantity; }
    
    public BigDecimal getFrameUnitPrice() { return frameUnitPrice; }
    public void setFrameUnitPrice(BigDecimal frameUnitPrice) { this.frameUnitPrice = frameUnitPrice; }
    
    public Lens getLens() { return lens; }
    public void setLens(Lens lens) { this.lens = lens; }
    
    public Integer getLensQuantity() { return lensQuantity; }
    public void setLensQuantity(Integer lensQuantity) { this.lensQuantity = lensQuantity; }
    
    public BigDecimal getLensUnitPrice() { return lensUnitPrice; }
    public void setLensUnitPrice(BigDecimal lensUnitPrice) { this.lensUnitPrice = lensUnitPrice; }
    
    public BigDecimal getFittingCharges() { return fittingCharges; }
    public void setFittingCharges(BigDecimal fittingCharges) { this.fittingCharges = fittingCharges; }
    
    public BigDecimal getOtherCharges() { return otherCharges; }
    public void setOtherCharges(BigDecimal otherCharges) { this.otherCharges = otherCharges; }
    
    public BigDecimal getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; }
    
    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
    
    public BigDecimal getTaxAmount() { return taxAmount; }
    public void setTaxAmount(BigDecimal taxAmount) { this.taxAmount = taxAmount; }
    
    public BigDecimal getGrandTotal() { return grandTotal; }
    public void setGrandTotal(BigDecimal grandTotal) { this.grandTotal = grandTotal; }
    
    public BigDecimal getAmountPaid() { return amountPaid; }
    public void setAmountPaid(BigDecimal amountPaid) { this.amountPaid = amountPaid; }
    
    public PaymentMode getPaymentMode() { return paymentMode; }
    public void setPaymentMode(PaymentMode paymentMode) { this.paymentMode = paymentMode; }
    
    public PaymentStatus getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; }
    
    public LocalDate getBillDate() { return billDate; }
    public void setBillDate(LocalDate billDate) { this.billDate = billDate; }
    
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    
    public BillStatus getStatus() { return status; }
    public void setStatus(BillStatus status) { this.status = status; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}