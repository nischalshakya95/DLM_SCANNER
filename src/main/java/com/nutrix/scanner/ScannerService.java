package com.nutrix.scanner;

import com.nutrix.constant.SdkConstant;
import com.nutrix.sdk.InitializeSdk;
import com.nutrix.sdk.SampleSdk;
import com.nutrix.sdk.exception.RowLimitException;
import com.nutrix.sdk.model.ScannerResponse;
import com.nutrix.sdk.references.IntegerReference;
import jakarta.inject.Singleton;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Singleton
public class ScannerService {

    private static final Logger logger = LogManager.getLogger(ScannerController.class);

    public int configureScanner() {
        var ssmini = SampleSdk.INSTANCE;
        int initialize = ssmini.SSMINI_Initialize();
        logger.info("Scanner initialize " + initialize);

        int rows = SdkConstant.SCANNER_ROW;
        int columns = SdkConstant.SCANNER_COLUMN;

        IntegerReference xOff = new IntegerReference();
        IntegerReference yOff = new IntegerReference();
        IntegerReference width = new IntegerReference();
        IntegerReference height = new IntegerReference();

        if (initialize == 0) {
            var selectScanner = ssmini.SSMINI_SelectScanner();
            logger.info("Select scanner" + selectScanner);
            var editGrid = ssmini.SSMINI_EditGrid(rows, columns);
            logger.info("Grid successfully edited with value " + editGrid);

            int regReadFrame = ssmini.SSMINI_RegReadFrame(rows, columns, xOff, yOff, width, height);
            logger.info("SSMINI RegReadFrame status: " + regReadFrame);
        }

        return initialize;
    }

    public ScannerResponse getScannerResponse() throws RowLimitException {
        return InitializeSdk.loadAndInitializeSdk();
    }
}
