package com.ebuy.userservice.resource;

import com.ebuy.userservice.embedded.TransactionStatus;
import com.ebuy.userservice.embedded.TransactionType;
import com.ebuy.userservice.entity.VendorTransaction;
import com.ebuy.userservice.service.VendorTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@RequestMapping("/api/v1/vendor-transactions")
@RestController
public class VendorTransactionResource extends CrudController<VendorTransaction> {
    private final VendorTransactionService service;

    @Autowired
    public VendorTransactionResource(VendorTransactionService service) {
        super(service);
        this.service = service;
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<VendorTransaction> putStatusById(@PathVariable Long id, @RequestBody TransactionStatus newStatus) {
        this.service.updateStatusById(id, newStatus);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/revenue")
    public ResponseEntity<BigDecimal> getTotalRevenue() {
        return new ResponseEntity<>(this.service.getTotalRevenue(), HttpStatus.OK);
    }

    @GetMapping("/revenue/vendor/{vendorId}")
    public ResponseEntity<BigDecimal> getRevenueByVendorId(@PathVariable Long vendorId) {
        return new ResponseEntity<>(this.service.getRevenueByVendorId(vendorId), HttpStatus.OK);
    }

    @GetMapping("/revenue/period")
    public ResponseEntity<BigDecimal> getRevenueByPeriod(@RequestParam Date beginDate, @RequestParam Date endDate) {
        return new ResponseEntity<>(this.service.getRevenueByPeriod(beginDate, endDate), HttpStatus.OK);
    }

    @GetMapping("/revenue/vendor/{vendorId}/period")
    public ResponseEntity<BigDecimal> getRevenueByVendorIdAndPeriod(@PathVariable Long vendorId, @RequestParam Date beginDate, @RequestParam Date endDate) {
        return new ResponseEntity<>(this.service.getRevenueByVendorIdAndPeriod(vendorId, beginDate, endDate), HttpStatus.OK);
    }

    @GetMapping("/vendor/{vendorId}/revenue")
    public ResponseEntity<BigDecimal> getVendorTotalRevenueByVendorId(@PathVariable Long vendorId) {
        return new ResponseEntity<>(this.service.getVendorTotalRevenueByVendorId(vendorId), HttpStatus.OK);
    }

    @GetMapping("/vendor/{vendorId}/revenue/period")
    public ResponseEntity<BigDecimal> getVendorRevenueByPeriodByVendorId(@PathVariable Long vendorId, @RequestParam Date beginDate, @RequestParam Date endDate) {
        return new ResponseEntity<>(this.service.getVendorRevenueByPeriodByVendorId(vendorId, beginDate, endDate), HttpStatus.OK);
    }

    @GetMapping("/vendor/{vendorId}")
    public ResponseEntity<List<VendorTransaction>> getByVendorId(@PathVariable Long vendorId) {
        return new ResponseEntity<>(this.service.getByVendorId(vendorId), HttpStatus.OK);
    }

    @GetMapping("/period")
    public ResponseEntity<List<VendorTransaction>> getByPeriod(@RequestParam Date beginDate, @RequestParam Date endDate) {
        return new ResponseEntity<>(this.service.getByPeriod(beginDate, endDate), HttpStatus.OK);
    }

    @GetMapping("/{type}")
    public ResponseEntity<List<VendorTransaction>> getByType(@PathVariable TransactionType type) {
        return new ResponseEntity<>(this.service.getByType(type), HttpStatus.OK);
    }
                                                             @GetMapping("/{status}")
    public ResponseEntity<List<VendorTransaction>> getByStatus(@PathVariable TransactionStatus status) {
        return new ResponseEntity<>(this.service.getByStatus(status), HttpStatus.OK);
    }
}
