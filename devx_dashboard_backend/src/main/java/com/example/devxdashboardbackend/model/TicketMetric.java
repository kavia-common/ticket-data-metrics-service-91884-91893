package com.example.devxdashboardbackend.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * PUBLIC_INTERFACE
 * Represents a single ticket metric record derived from the uploaded Excel data.
 * Field names and JSON capitalization follow strict acceptance criteria.
 */
public class TicketMetric {

    // PUBLIC_INTERFACE
    @JsonProperty("Application")
    private String application;

    // PUBLIC_INTERFACE
    @JsonProperty("Month")
    private String month;

    // PUBLIC_INTERFACE
    @JsonProperty("NoOfTicketsReceived")
    private Integer noOfTicketsReceived;

    // PUBLIC_INTERFACE
    @JsonProperty("NoOfTicketsRespondedByTel")
    private Integer noOfTicketsRespondedByTel;

    // PUBLIC_INTERFACE
    @JsonProperty("MTTRRespondMin")
    private Double mttrRespondMin;

    // PUBLIC_INTERFACE
    @JsonProperty("AdherenceToResponseSLA")
    private Integer adherenceToResponseSLA;

    // PUBLIC_INTERFACE
    @JsonProperty("SlippedResponseSLA")
    private Integer slippedResponseSLA;

    // PUBLIC_INTERFACE
    @JsonProperty("ResponseAdherenceRate")
    private Double responseAdherenceRate;

    // PUBLIC_INTERFACE
    @JsonProperty("NoOfTicketsResolvedByTel")
    private Integer noOfTicketsResolvedByTel;

    // PUBLIC_INTERFACE
    @JsonProperty("MTTRResolveMin")
    private Double mttrResolveMin;

    // PUBLIC_INTERFACE
    @JsonProperty("AdherenceToResolutionSLA")
    private Integer adherenceToResolutionSLA;

    // PUBLIC_INTERFACE
    @JsonProperty("SlippedResolutionSLA")
    private Integer slippedResolutionSLA;

    // PUBLIC_INTERFACE
    @JsonProperty("ResolutionAdherenceRate")
    private Double resolutionAdherenceRate;

    // PUBLIC_INTERFACE
    @JsonProperty("Remarks")
    private String remarks;

    // PUBLIC_INTERFACE
    @JsonProperty("ResolutionRemarks")
    private String resolutionRemarks;

    public TicketMetric() {}

    public String getApplication() { return application; }
    public void setApplication(String application) { this.application = application; }

    public String getMonth() { return month; }
    public void setMonth(String month) { this.month = month; }

    public Integer getNoOfTicketsReceived() { return noOfTicketsReceived; }
    public void setNoOfTicketsReceived(Integer noOfTicketsReceived) { this.noOfTicketsReceived = noOfTicketsReceived; }

    public Integer getNoOfTicketsRespondedByTel() { return noOfTicketsRespondedByTel; }
    public void setNoOfTicketsRespondedByTel(Integer noOfTicketsRespondedByTel) { this.noOfTicketsRespondedByTel = noOfTicketsRespondedByTel; }

    public Double getMttrRespondMin() { return mttrRespondMin; }
    public void setMttrRespondMin(Double mttrRespondMin) { this.mttrRespondMin = mttrRespondMin; }

    public Integer getAdherenceToResponseSLA() { return adherenceToResponseSLA; }
    public void setAdherenceToResponseSLA(Integer adherenceToResponseSLA) { this.adherenceToResponseSLA = adherenceToResponseSLA; }

    public Integer getSlippedResponseSLA() { return slippedResponseSLA; }
    public void setSlippedResponseSLA(Integer slippedResponseSLA) { this.slippedResponseSLA = slippedResponseSLA; }

    public Double getResponseAdherenceRate() { return responseAdherenceRate; }
    public void setResponseAdherenceRate(Double responseAdherenceRate) { this.responseAdherenceRate = responseAdherenceRate; }

    public Integer getNoOfTicketsResolvedByTel() { return noOfTicketsResolvedByTel; }
    public void setNoOfTicketsResolvedByTel(Integer noOfTicketsResolvedByTel) { this.noOfTicketsResolvedByTel = noOfTicketsResolvedByTel; }

    public Double getMttrResolveMin() { return mttrResolveMin; }
    public void setMttrResolveMin(Double mttrResolveMin) { this.mttrResolveMin = mttrResolveMin; }

    public Integer getAdherenceToResolutionSLA() { return adherenceToResolutionSLA; }
    public void setAdherenceToResolutionSLA(Integer adherenceToResolutionSLA) { this.adherenceToResolutionSLA = adherenceToResolutionSLA; }

    public Integer getSlippedResolutionSLA() { return slippedResolutionSLA; }
    public void setSlippedResolutionSLA(Integer slippedResolutionSLA) { this.slippedResolutionSLA = slippedResolutionSLA; }

    public Double getResolutionAdherenceRate() { return resolutionAdherenceRate; }
    public void setResolutionAdherenceRate(Double resolutionAdherenceRate) { this.resolutionAdherenceRate = resolutionAdherenceRate; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public String getResolutionRemarks() { return resolutionRemarks; }
    public void setResolutionRemarks(String resolutionRemarks) { this.resolutionRemarks = resolutionRemarks; }
}
