package com.nutrix.sdk.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BarcodeResponse {

    private String barcode;

    private String location;

    private String row;

    private String column;
}
