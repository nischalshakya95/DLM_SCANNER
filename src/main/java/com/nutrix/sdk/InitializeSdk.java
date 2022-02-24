package com.nutrix.sdk;

import com.nutrix.constant.SdkConstant;
import com.nutrix.sdk.exception.RowLimitException;
import com.nutrix.sdk.model.BarcodeResponse;
import com.nutrix.sdk.model.ScannerResponse;
import com.nutrix.sdk.references.IntegerReference;
import com.nutrix.utils.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;


public class InitializeSdk {

    private static final Logger logger = LogManager.getLogger(InitializeSdk.class);

    public static ScannerResponse loadAndInitializeSdk() throws RowLimitException {
        List<BarcodeResponse> barcodeResponses = new ArrayList<>();
        ScannerResponse scannerResponse = new ScannerResponse();
        var ssmini = SampleSdk.INSTANCE;
        int rows = SdkConstant.SCANNER_ROW;
        int columns = SdkConstant.SCANNER_COLUMN;

        IntegerReference xOff = new IntegerReference();
        IntegerReference yOff = new IntegerReference();
        IntegerReference width = new IntegerReference();
        IntegerReference height = new IntegerReference();
        IntegerReference embLeftIntegerReference = new IntegerReference(50);
        IntegerReference embTopIntegerReference = new IntegerReference(50);
        IntegerReference embRightIntegerReference = new IntegerReference(50);
        IntegerReference embBottomIntegerReference = new IntegerReference(50);

        try {
            int init = ssmini.SSMINI_Initialize();
            logger.info("Scanner initialized with code " + init);

            //ToDo: We can move this part to the separate button
//            var selectScanner = ssmini.SSMINI_SelectScanner();
//            logger.info("Scanner successfully selected with value " + selectScanner);

            int startDecodeEmbeddedBarcode = ssmini.SSMINI_EnableEmbeddedBarcode(SdkConstant.START_DECODE_EMBEDDED_RACK_BAR_CODE);
            logger.info("Start decoding embedded rack barcode status " + startDecodeEmbeddedBarcode);

            int regReadFrame = ssmini.SSMINI_RegReadFrameEx(
                    rows, columns,
                    xOff, yOff,
                    width, height,
                    embLeftIntegerReference, embTopIntegerReference, embRightIntegerReference, embBottomIntegerReference
            );
            logger.info("SSMINI RegReadFrame status: " + regReadFrame);

            int acquireImage = ssmini.SSMINI_AcquireImage(
                    SdkConstant.SCANNER_IMAGE_RESOLUTION, SdkConstant.SHOW_PROGRESS,
                    xOff.getValue(), yOff.getValue(),
                    width.getValue(), height.getValue(),
                    String.valueOf(0)
            );
            logger.info("Acquire image: " + acquireImage);

            decodeAndSet(ssmini, rows, columns, scannerResponse, barcodeResponses);
        } catch (Exception exception) {
            logger.error("Exception occurred while using scanner DLL functions", exception);
        }
        return scannerResponse;
    }


    public static void decodeAndSet(SampleSdk ssmini, int rows, int columns, ScannerResponse scannerResponse, List<BarcodeResponse> barcodeResponses) {
        var resetBarcode = ssmini.SSMINI_ResetBarcode();
        logger.info("Reset barcode " + resetBarcode);

        var setDecoder = ssmini.SSMINI_SetDecoderEx(rows, columns,
                SdkConstant.TUBE_DECODE_FILTER_LEVEL, 0, SdkConstant.ENABLE_DETECT_MISSING_TUBE,
                SdkConstant.TUBE_DECODE_INCLUDING_BORDER_IMAGE_FROM_NEIGHBOURING_TUBE, true);
        logger.info("Set Decoder value " + setDecoder);

        var startDecode = ssmini.SSMINI_StartDecode(SdkConstant.START_DECODE);
        logger.info("Starting decoding with " + startDecode);
        decodeEmbeddedRackBarcode(ssmini, scannerResponse);

        char characterRow = 'A';
        int noOfDecodedTubes = 0;

        logger.info("Retrieving barcode");

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                int status = ssmini.SSMINI_GetBarcodeInfo(SdkConstant.TUBE_SUCCESSFULLY_DECODE_INFO, row, col);
                logger.info("Barcode info received with status " + status);
                if (status == 0) {
                    // as per the index col begins from 0 but as per the value it begins at 1. So, col + 1 is passed into the getSDKResponse
                    BarcodeResponse barcodeResponse = getBarcodeResponse(ssmini, row, col, characterRow);
                    barcodeResponses.add(barcodeResponse);
                    noOfDecodedTubes++;

                    logger.info("Barcode retrieved of row " + row + " and column " + col);
                } else {
                    logger.info("(" + row + "," + col + ") *** No Read ***");
                }
            }
            characterRow++;
        }

        logger.info(noOfDecodedTubes + " tubes decoded successfully.");

        scannerResponse.setBarcodeResponses(barcodeResponses);
    }

    private static void decodeEmbeddedRackBarcode(SampleSdk ssmini, ScannerResponse scannerResponse) {
        var decodedEmbedded = ssmini.SSMINI_DecodeEmbedded(SdkConstant.RACK_BARCODE_DECODE_FILTER_LEVEL);
        logger.info("Decode embedded rack barcode status " + decodedEmbedded);

        if (decodedEmbedded == 0) {
            byte[] rackStr = new byte[128];

            var rackBarcode = ssmini.SSMINI_GetEmbeddedBarcode(rackStr, 128);
            var embeddedRackBarcode = StringUtils.removeUnicodeCharacterFromString(new String(rackStr));

            logger.info("Retrieve embedded rack barcode length " + rackBarcode);
            logger.info("Embedded rack barcode " + embeddedRackBarcode);

            scannerResponse.setRackBarcode(embeddedRackBarcode);
        }
    }

    @NotNull
    private static BarcodeResponse getBarcodeResponse(SampleSdk ssmini, int row, int col, char characterRow) {
        byte[] str = new byte[128];

        int codeLength = ssmini.SSMINI_GetBarcodeInfo(SdkConstant.TUBE_DECODE_CODE_LENGTH_INFO, row, col);
        // Here, codeLength with value 0 indicate success
        logger.info("Get barcode info status " + codeLength);

        var getBarCode = ssmini.SSMINI_GetBarcode(row, col, str, 128);
        String barcodeString = StringUtils.removeUnicodeCharacterFromString(new String(str));

        var rowString = Character.toString(characterRow);
        var columnString = String.valueOf(col + 1);

        // Append 0 to the single integer i.e. 1 will be 01.
        if (columnString.length() == 1) {
            columnString = String.valueOf(0).concat(columnString);
        }

        logger.info("Row Index " + row);
        logger.info("Column Index " + columnString);
        logger.info("Get Barcode return with barcode length " + getBarCode);
        logger.info("Barcode return is " + barcodeString);

        BarcodeResponse barcodeResponse = new BarcodeResponse();
        barcodeResponse.setRow(rowString);
        barcodeResponse.setColumn(columnString);
        barcodeResponse.setLocation(rowString.concat(columnString));
        barcodeResponse.setBarcode(barcodeString);

        logger.info("Barcode Response " + barcodeResponse);

        return barcodeResponse;
    }

}
