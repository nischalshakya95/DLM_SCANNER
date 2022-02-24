package com.nutrix.scanner;

import com.nutrix.sdk.exception.RowLimitException;
import com.nutrix.sdk.model.ScannerResponse;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import jakarta.inject.Inject;

@Controller("/scanners")
public class ScannerController {

    @Inject
    private ScannerService scannerService;

    @Get("/configure")
    public HttpResponse<Integer> configureScanner() {
        return HttpResponse.ok(scannerService.configureScanner());
    }

    @Get("/read-barcode")
    public HttpResponse<ScannerResponse> getScannerData() throws RowLimitException {
        return HttpResponse.ok(scannerService.getScannerResponse());
    }

}
