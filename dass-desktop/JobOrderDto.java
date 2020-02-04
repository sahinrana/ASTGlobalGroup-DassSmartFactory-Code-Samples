package com.dasssmart.dto;

import com.dasssmart.pagination.DisplayAs;

public class JobOrderDto {
    
    private int jobOrderId;
    
    private String operationName;
    
    private String description;
    
    private int stockToProduceId;
    
    private String stockToProduceName;
    
    private Double quantity;
    
    private String startTime;

      @DisplayAs(value = "JobOrder", columnName = "JOB_ORDER_ID", index = 0)
    public int getJobOrderId() {
        return jobOrderId;
    }

    public void setJobOrderId(int jobOrderId) {
        this.jobOrderId = jobOrderId;
    }

     // @DisplayAs(value = "Description", columnName = "DESCRIPTION", index = 1)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @DisplayAs(value = "Stock ID", columnName = "STOCK_ID", index = 2)
    public int getStockToProduceId() {
        return stockToProduceId;
    }

    public void setStockToProduceId(int stockToProduceId) {
        this.stockToProduceId = stockToProduceId;
    }

      @DisplayAs(value = "Produce Stock Name", columnName = "STOCK_NAME", index = 3)
    public String getStockToProduceName() {
        return stockToProduceName;
    }

    public void setStockToProduceName(String stockToProduceName) {
        this.stockToProduceName = stockToProduceName;
    }

      @DisplayAs(value = "Quantity", columnName = "QUANTITY", index = 4)
    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

     @DisplayAs(value = "Operation Name", columnName = "OPERATION_NAME", index = 1)
    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    @DisplayAs(value = "Start Time", columnName = "START_TIME", index = 5)
    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
}
