package com.nutrix.sdk.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ScannerResponse {

    private String rackBarcode;

    private List<BarcodeResponse> barcodeResponses;

}
