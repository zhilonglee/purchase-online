package com.zhilong.springcloud.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class NetsTransactionPayLoad implements Serializable {

    /**
     * Message Type Identifier:
     * Message Type Identifier indicates the type of message
     */
    private String mti;

    /**
     * Processing code:
     * The processing code is used with the message type to define the type
     * of transaction sent by the point of service device
     */
    private String process_code;

    /**
     * SOF_URL is not available if Order's getQRCode='Y'
     */
    private String sof_uri;

    /**
     * Transaction Amount:
     * This field is the total transaction amount requested by the consumer in the local
     * currency of the transaction originator. The value is left padded with zeros, right
     * justified with the currency's minor unit and excluding the decimal point.
     */
    private String amount;

    /**
     * Transmission Date Time:
     * MMDDhhmmss Echoed back in response
     */
    private String transmission_time;

    /**
     * System Trace Audit Number:
     * A 6-digit number assigned by a transaction originator that provides a unique
     * identifier for a message under a MID and TID. The trace number remains unchanged for life of the transaction.
     * This number should be unique within a settlement cycle.
     *
     * Value must be starting from "000001" and limited to "999999", front padded with zeros.
     */
    private String stan;

    /**
     * Local Transaction Time:
     * This is the local time at which the transaction takes place at the point of card acceptor location.
     * Field value format is "hhmmss"
     */
    private String transaction_time;

    /**
     * Local Transaction Date:
     * This is the local month and day on which the transaction takes place at the point of card acceptor location.
     * Field value format is "MMDD"
     */
    private String transaction_date;

    /**
     * Entry Mode:
     * This field is used to indicate how the payment was entered.
     */
    private String entry_mode;

    /**
     * Condition Code:
     * Indicates the method by which the transaction was entered into the interchange. This field is present
     * in all financial request,advice,and reversal message.It indicates the type of device and the conditions
     * where the transaction originated.
     */
    private String condition_code;

    /**
     *Acquiring Institution Identified Code
     */
    private String institution_code;

    /**
     *Retrieval Reference Number:
     * This field will contain a 12 character reference number generated by the host.
     * This number will be used to refernce a transaction for further processing.
     */
    private String retrieval_ref;

    /**
     * For order request, this is echo of the same parameter in request.
     */
    private String approval_code;

    /**
     * The response status from NPS
     */
    private String response_code;

    /**
     * Card Acceptor Terminal Identification(TID)
     * This field identifies a terminal at the merchant location where the transaction originates,
     * or the terminal id assigned by Acquire or Host.
     */
    private String host_tid;

    /**
     * Card Acceptor Identification Code(MID)
     * This is the 10-digit merchant identifier number as provided by Acquire or Host, e.g.,"1231234567".
     * Extra characters will be padded '0' on the left
     */
    private String host_mid;

    /**
     * Merchant Name:
     * Assigned by NETS
     */
    private String acceptor_name;

    /**
     * Transaction Identifier:
     * This field contains data that uniquely identifies a transaction.
     * In the case of a QR Code payment, it contains the QR Code details.
     */
    private String txn_identifier;

    /**
     * NPX Related Data
     * This field contains extended information required by NPX gateway
     * In JSON format,the tags are subfields.
     */
    private Map<String,String> npx_data;

    /**
     * Invoice Ref:
     * This field contains the 16-digit Invoice Reference.
     * Invoice Reference must be unique under the same MID and TID
     */
    private String invoice_ref;

    /**
     * User metadata with separator "|"
     */
    private String user_data;

    /**
     * Communication Data:
     * Return response back to caller.
     * Mandatory if getQRCode = "Y"
     */
    private List<CommunicationData> communication_data;

    /**
     * Get QR Code flag:
     * "Y " for getting QR in return
     */
    private String getQRCode;

    /**
     * QR Code image in PNG format,Base64 enchode. Returned if the getQRCode flag
     * in the Order request is set to "Y"
     */
    private String qr_code;


    public String getMti() {
        return mti;
    }

    public void setMti(String mti) {
        this.mti = mti;
    }

    public String getProcess_code() {
        return process_code;
    }

    public void setProcess_code(String process_code) {
        this.process_code = process_code;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getStan() {
        return stan;
    }

    public void setStan(String stan) {
        this.stan = stan;
    }

    public String getTransmission_time() {
        return transmission_time;
    }

    public void setTransmission_time(String transmission_time) {
        this.transmission_time = transmission_time;
    }

    public String getTransaction_time() {
        return transaction_time;
    }

    public void setTransaction_time(String transaction_time) {
        this.transaction_time = transaction_time;
    }

    public String getTransaction_date() {
        return transaction_date;
    }

    public void setTransaction_date(String transaction_date) {
        this.transaction_date = transaction_date;
    }

    public String getEntry_mode() {
        return entry_mode;
    }

    public void setEntry_mode(String entry_mode) {
        this.entry_mode = entry_mode;
    }

    public String getCondition_code() {
        return condition_code;
    }

    public void setCondition_code(String condition_code) {
        this.condition_code = condition_code;
    }

    public String getInstitution_code() {
        return institution_code;
    }

    public void setInstitution_code(String institution_code) {
        this.institution_code = institution_code;
    }

    public String getRetrieval_ref() {
        return retrieval_ref;
    }

    public void setRetrieval_ref(String retrieval_ref) {
        this.retrieval_ref = retrieval_ref;
    }

    public String getHost_tid() {
        return host_tid;
    }

    public void setHost_tid(String host_tid) {
        this.host_tid = host_tid;
    }

    public String getHost_mid() {
        return host_mid;
    }

    public void setHost_mid(String host_mid) {
        this.host_mid = host_mid;
    }

    public String getAcceptor_name() {
        return acceptor_name;
    }

    public void setAcceptor_name(String acceptor_name) {
        this.acceptor_name = acceptor_name;
    }

    public String getTxn_identifier() {
        return txn_identifier;
    }

    public void setTxn_identifier(String txn_identifier) {
        this.txn_identifier = txn_identifier;
    }

    public Map<String, String> getNpx_data() {
        return npx_data;
    }

    public void setNpx_data(Map<String, String> npx_data) {
        this.npx_data = npx_data;
    }

    public String getInvoice_ref() {
        return invoice_ref;
    }

    public void setInvoice_ref(String invoice_ref) {
        this.invoice_ref = invoice_ref;
    }

    public String getUser_data() {
        return user_data;
    }

    public void setUser_data(String user_data) {
        this.user_data = user_data;
    }

    public String getGetQRCode() {
        return getQRCode;
    }

    public void setGetQRCode(String getQRCode) {
        this.getQRCode = getQRCode;
    }

    public List<CommunicationData> getCommunication_data() {
        return communication_data;
    }

    public void setCommunication_data(List<CommunicationData> communication_data) {
        this.communication_data = communication_data;
    }

    public String getSof_uri() {
        return sof_uri;
    }

    public void setSof_uri(String sof_uri) {
        this.sof_uri = sof_uri;
    }

    public String getApproval_code() {
        return approval_code;
    }

    public void setApproval_code(String approval_code) {
        this.approval_code = approval_code;
    }

    public String getResponse_code() {
        return response_code;
    }

    public void setResponse_code(String response_code) {
        this.response_code = response_code;
    }

    public String getQr_code() {
        return qr_code;
    }

    public void setQr_code(String qr_code) {
        this.qr_code = qr_code;
    }

    public static class CommunicationData {
        String type;
        String category;
        String destination;
        Addon addon;

        public static class Addon {
            String external_API_keyID;

            public String getExternal_API_keyID() {
                return external_API_keyID;
            }

            public void setExternal_API_keyID(String external_API_keyID) {
                this.external_API_keyID = external_API_keyID;
            }
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getDestination() {
            return destination;
        }

        public void setDestination(String destination) {
            this.destination = destination;
        }

        public Addon getAddon() {
            return addon;
        }

        public void setAddon(Addon addon) {
            this.addon = addon;
        }
    }


}