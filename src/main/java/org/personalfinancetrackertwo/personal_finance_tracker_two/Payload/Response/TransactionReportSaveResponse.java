package org.personalfinancetrackertwo.personal_finance_tracker_two.Payload.Response;

public class TransactionReportSaveResponse {

    private String message;

    public TransactionReportSaveResponse(String message) {
        this.message = message;
    }

    public TransactionReportSaveResponse() {

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
