package com.koreait.ursrest.domain;

import lombok.Data;

@Data
public class Paybill {
    private int member_id;
    private String store_id;
    private int receipt_totalamount;
    private int menu_quantity;
    private String unavailable;
    private String reservation_table;
    private String menu_ids;
    private String bootpay_id;
}
