package com.nutrix.sdk;

import com.nutrix.sdk.references.IntegerReference;
import com.sun.jna.Library;
import com.sun.jna.Native;

public interface SampleSdk extends Library {

    SampleSdk INSTANCE = Native.load("SSMINI", SampleSdk.class);

    /**
     * Initialize SSMINIX and select a default scanning device. It also authenticates the Decoder’s passcode or the
     * Decoder’s dongle (one of them must be valid for SampleScan decoder to fully function).
     *
     * @return 0 Success 1 Internal Error 2 Scanner interface initialization failed 3 No default device found
     */
    int SSMINI_Initialize();

    /**
     * cp-
     * Displays a Select Source dialog which allows user to select a scanner. If your SampleScan scanner is a Fujitsu
     * fi-60F, select the entry with WIA-fi-60F substring from the list. If your SampleScan scanner is an Epson
     * Perfection V33, select the entry with WIA-EPSON Perfection V33/V330 substring from the list.
     *
     * @return 1 if a scanner is selected after the Select Source is closed. If the user clicked the CANCEL button to
     * close the dialog, the return value would be 1 if a default device was already selected.
     */
    Long SSMINI_SelectScanner();

    /**
     * Retrieve the Scanner-Name of the current scanning device
     *
     * @return String Device family name of the scanning device. Returns empty string if no scanner is selected.
     */
    String SSMINI_GetScannerName();

    /**
     * Display a rack template editor dialog. The template editor enables users to define a unique template
     * for each rack configuration.
     *
     * @param rows Specify the number of rows in the tube rack. This value is used in conjunction with parameter cols
     *             to identify the rack template to be created or edited
     * @param cols Specify the number of columns in the tube rack. This value is used in conjunction with parameter rows
     *             to identify the rack template to be created or edited
     * @return 0 User clicked the CANCEL button exit the Rack Template Editor dialog
     * 1 User clicked the REGISTER button to register the rack template before exiting the rack dialog.
     */
    int SSMINI_EditGrid(int rows, int cols);

    /**
     * Retrieve a rack template (previously registered using SSMINI_EditGrid or SSMINI_RegWriteFrame)
     *
     * @param rows   Specify the number of rows in the tube rack. This value is used in conjunction with parameter Cols to
     *               identify a rack template to be retrieved.
     * @param cols   Specify the number of columns in the tube rack. This value is used in conjunction with parameter Rows to
     *               identify a rack template to be retrieved.
     * @param xOff   To receive the horizontal offset from the right of scanner’s scan-area in 1/1000th of an inch unit
     * @param yOff   To receive the vertical offset from the top of scanner’s scan-area in 1/1000th of an inch unit.
     * @param width  To receive the width of the tube rack in 1/1000th of an inch unit.
     * @param height To receive the height of the tube rack in 1/1000th of an inch unit
     * @return 0 The specified rack template does not exist
     * 1 The specified rack template successfully retrieved
     */
    int SSMINI_RegReadFrame(int rows, int cols, IntegerReference xOff, IntegerReference yOff, IntegerReference width, IntegerReference height);

    /**
     * Scan the rack image, and optionally save the image.
     *
     * @param resolution Desired scan-resolution in DPI (dots per inch, max 600)
     * @param progress   To enable or suppress the Progress Dialog while scanning is in progress.
     * @param xOff       Horizontal offset from the right edge of the scanner’s scan-area (in 1/1000th of an inch unit)
     * @param yOff       Vertical offset from the top of scanner’s scan-area (in 1/1000th of an inch unit)
     * @param width      Width of the tube rack in 1/1000th of an inch unit
     * @param height     Height of the tube rack in 1/1000th of an inch unit.
     * @param bmpFile    Optional filename (full path) to save the scanned image to a hard drive (.bmp extension). If you do not wish
     *                   to save the image, set this parameter to 0. You may call SSMINI_SaveImageOption prior to calling this
     *                   function to specify the Image-Flip options. Note that you may also call SSMINI_SaveImage later to save the
     *                   image.
     * @return 1 Success – rack image is ready for decoding operations
     * 2 Cancelled (user clicked CANCEL button on the progress dialog)
     * 3 Error
     */
    int SSMINI_AcquireImage(int resolution, int progress, int xOff, int yOff, int width, int height, String bmpFile);

    /**
     * Retrieve the X-Offset value (in 1/1000th of an inch unit) of the scan-area previously registered using
     * SSMINI_EditGrid of SSMINI_RegWriteFrame methods.
     *
     * @return X-Offset value (in 1/1000th of an inch unit) of the scan-area previously registered using SSMINI_EditGrid of
     * SSMINI_RegWriteFrame methods. If this method fails to retrieve a value, it returns a negative value (-1).
     */
    Long SSMINI_ReadFrameXoff();

    /**
     * Retrieve the Y-Offset (in 1/1000th of an inch unit) value of the scan-area previously registered using
     * SSMINI_EditGrid of SSMINI_RegWriteFrame methods.
     *
     * @return Y-Offset value (in 1/1000th of an inch unit) of the scan-area previously registered using SSMINI_EditGrid of
     * SSMINI_RegWriteFrame methods. If this method fails to retrieve a value, it returns a negative value (-1).
     */
    Long SSMINI_ReadFrameYoff();

    /**
     * Retrieve the width (in 1/1000th of an inch unit) of the scan-area previously registered using SSMINI_EditGrid of
     * SSMINI_RegWriteFrame methods.
     *
     * @return The width (in 1/1000th of an inch unit) of the scan-area previously registered using SSMINI_EditGrid of
     * SSMINI_RegWriteFrame methods. If this method fails to retrieve a value, it returns a negative value (-1).
     */
    Long SSMINI_ReadFrameWidth();

    /**
     * Retrieve the height (in 1/1000th of an inch unit) of the scan-area previously registered using SSMINI_EditGrid of
     * SSMINI_RegWriteFrame methods.
     *
     * @return The height (in 1/1000th of an inch unit) of the scan-area previously registered using SSMINI_EditGrid of
     * SSMINI_RegWriteFrame methods. If this method fails to retrieve a value, it returns a negative value (-1).
     */
    Long SSMI_ReadFrameHeight();

    String SSMINI_GetScannerManufacturer();

    /**
     * Specify rack's spatial partitioning information and image-processing options for decoding
     *
     * @param rows          Number of rows in the tube rack to be decoded
     * @param cols          Number of columns in the tube rack to be decoded
     * @param filterLevel   Number of digital filters to be applied if the first decode attempt fails. If a tube fails to decode, the program
     *                      digitally process the tube image using a different filter and retry decoding. This process continues until a
     *                      barcode is obtained, or the specified number of filters has been exhausted. Set FilterLevel to 0 to disable digital
     *                      filter processing. The maximum value for FilterLevel is 10. We suggest using FilterLevel value of 8
     * @param detectMissing Set this flag to 1 to enable missing-tube-detection. This is useful for automatically skipping empty tube positions
     *                      (without lengthy retries). Set this flag to 0 to disable missing-tube-detection.
     * @param pctOverlap    Allow decoding to include border image from neighboring tubes, up to PctOverlap (percent) of barcode size in
     *                      each direction. For example, if a cell (containing a tube barcode) is 100 x 100 pixels, and PctOverlap is 20%,
     *                      then the decoder will extend the tube image to include 20 pixels into the neighboring cells’ image. This results in
     *                      a 140 x 140 image used for decoding. This is useful for decoding tube racks with non-uniform grid partition or
     *                      racks with very narrow distance between tubes. Please limit this value to 0% – 30% range, as larger values
     *                      would increase the chance of decoding neighboring tube’s barcode
     * @return Always 0
     */
    int SSMINI_SetDecoder(int rows, int cols,
                          int filterLevel, int detectMissing,
                          int pctOverlap);

    /**
     * Clears all previously decoded barcodes from the memory. This function should be called prior to decoding a
     * new rack of tubes using SSMINI_StartDecode().
     *
     * @return Always 0
     */
    int SSMINI_ResetBarcode();

    /**
     * Starts decoding the mostly recently scanned tube
     *
     * @param immediate If Immediate is set to 1, this method starts the decoding process in a separate thread and returns control to the
     *                  caller immediately. To determine if decoding has completed, call SSMINI_GetDecodeStatus to poll for the
     *                  status of the decoding process.
     *                  <p>
     *                  If Immediate is set to 0, the method returns after the completion of decoding process. Call
     *                  SSMINI_GetDecodeStatus to determine the number of decoded and unreadable tubes. Call
     *                  SSMINI_GetBarcode and SSMINI_GetBarcodeInfo to obtain individual tube barcode information.
     * @return If Immediate is 1, this method returns 1 if the decoding thread is created successfully, and 0 if an error occurs
     * during the thread creation.
     * If Immediate is 0, this method returns the number of successfully decoded tubes.
     */
    int SSMINI_StartDecode(int immediate);

    /**
     * Use this function to obtain status of decoding process (previously started using SSMINI_StartDecode)
     *
     * @param success To receive the number of successfully decoded status so far.
     * @param noRead  To receive the number of tubes unreadable so far.
     * @return 1 if decoding is in progress
     * 0 if decoding has completed
     */
    boolean SSMINI_GetDecodeStatus(int success, int noRead);

    /**
     * Obtain barcode information
     *
     * @param info Integer indicating the type of information to retrieve
     * @param row  Row number of tube of interest
     * @param col  Column number of tube of interest
     * @return Meaning depends on info
     */
    int SSMINI_GetBarcodeInfo(int info, int row, int col);

    /**
     * Retrieve the barcode
     *
     * @param row Row number of the tube of interest
     * @param col Column number of the tube of interest
     * @return int
     */
    int SSMINI_GetBarcode(int row, int col, byte[] strBarcode, int bufLength);

    /**
     * Returns the floating point value and it provides additional information about the decoded barcodes
     *
     * @param quality Specifies which quality type you wish to query
     * @param row     Row number of tube of interest
     * @param col     Column number of tube of interest
     * @return float
     */
    float SSMINI_GetBarcodeQuality(int quality, int row, int col);

    /**
     * Decode the embedded barcode on the last scanned image
     *
     * @param filterLevel Specify the number of filters to be used in decoding. Valid range of this number is 0 through
     *                    50. A value of 0 means no filter will be applied if decoding was unsuccessful on the original
     *                    scanned image.
     * @return int 0 The tubes was decoded successfully
     */
    int SSMINI_DecodeEmbedded(int filterLevel);

    /**
     * Retrieve the barcode obtained from the SSMINI_DecodeEmbedded()
     *
     * @param strBarcode Character buffer to receive the barcode
     * @param buffLength Number of bytes allocated to the strBarcode
     * @return int This function returns the length of the decoded barcode. If the return value is larger than BufLen, the calling
     * program should call this function again with a larger allocated buffer for strBarcode (and update BufLen
     * accordingly).
     */
    int SSMINI_GetEmbeddedBarcode(byte[] strBarcode, int buffLength);

    /**
     * Register the rack template (uniquely identified by Rows and Cols). Registered rack templates can later be retrieved using
     * SSMINI_RegReadFrame, or used the default templates in SSMINI_EditGrid
     *
     * @param rows   Specify the number of rows in the tube rack.This value is used in conjunction with parameter Cols to identify a rack
     *               template to be registered
     * @param cols   Specify the number of columns in the tube rack.This value is used in conjunction with parameter Rows to identify a rack
     *               template to be registered
     * @param xoff   The horizontal offset from the right os scanner's scan-area in 1/1000th of an inch unit
     * @param yoff   The vertical offset from the top of scanner's scan-area in 1/1000th of an inch unit
     * @param width  The width of the tube rack in 1/1000th of an inch unit
     * @param height The height of the tube rack in 1/1000th of an inch unit
     * @return This function always return 1
     */
    int SSMINI_RegWriteFrame(int rows, int cols, int xoff, int yoff, int width, int height);

    /**
     * Indicates that the next scan should include the embedded barcode. Note that some embedded barcode may be located
     * externally to the actual boundary of the tubes.
     *
     * @param enable is set to 1, SSMINI_AcquireImage will extend the scan area to include the external embedded
     *               barcode.
     *               enable is set to 0, SSMINI_AcquireImage will not extend the scan area to include the external embedded
     *               barcode
     * @return This function always return 1
     */
    int SSMINI_EnableEmbeddedBarcode(int enable);

    /**
     * Obtain the barcode information after calling SSMINI_DecodeEmbedded
     *
     * @param info Integer indicating the type of information to retrieve
     * @return Meaning depends upon the info
     */
    int SSMINI_GetEmbeddedBarCodeInfo(int info);

    /**
     * Retrieve a rack template (previously registered using SSMINI_EditGrid or SSMINI_RegWriteFrame) .
     *
     * @param rows      Specify the number of rows in the tube rack. This value is used in conjunction with parameter Cols to
     *                  identify a rack template to be retrieved.
     * @param cols      Specify the number of columns in the tube rack. This value is used in conjunction with parameter Rows to
     *                  identify a rack template to be retrieved.
     * @param xOff      To receive the horizontal offset from the right of scanner’s scan-area in 1/1000th of an inch unit.
     * @param yOff      To receive the vertical offset from the top of scanner’s scan-area in 1/1000th of an inch unit.
     * @param width     To receive the width of the tube rack in 1/1000th of an inch unit.
     * @param height    To receive the height of the tube rack in 1/1000th of an inch unit.
     * @param embLeft   To receive the leftmost position of the embedded barcode
     * @param embTop    To receive the topmost position of the embedded barcode
     * @param embRight  To receive the rightmost position of the embedded barcode
     * @param embBottom To receive the bottommost position of the embedded barcode
     * @return 0 The specified rack template does not exist.
     * 1 The specified rack template successfully retrieved.
     */
    int SSMINI_RegReadFrameEx(int rows, int cols, IntegerReference xOff, IntegerReference yOff, IntegerReference width, IntegerReference height,
                              IntegerReference embLeft, IntegerReference embTop, IntegerReference embRight, IntegerReference embBottom);

    /**
     * Register a rack template (uniquely identified by Rows and Cols). Registered rack templates can later be
     * retrieved using SSMINI_RegReadFrame, or used as the default templates in SSMINI_EditGrid.
     *
     * @param rows      Specify the number of rows in the tube rack. This value is used in conjunction with parameter Cols to
     *                  identify a rack template to be registered.
     * @param cols      Specify the number of columns in the tube rack. This value is used in conjunction with parameter Rows to
     *                  identify a rack template to be registered.
     * @param xOff      The horizontal offset from the right of scanner’s scan-area in 1/1000th of an inch unit.
     * @param yOff      The vertical offset from the top of scanner’s scan-area in 1/1000th of an inch unit.
     * @param width     The width of the tube rack in 1/1000th of an inch unit.
     * @param height    The height of the tube rack in 1/1000th of an inch unit.
     * @param embLeft   To receive the leftmost position of the embedded barcode
     * @param embTop    To receive the topmost position of the embedded barcode
     * @param embRight  To receive the rightmost position of the embedded barcode
     * @param embBottom To receive the bottommost position of the embedded barcode
     * @return This function always return 1
     */
    int SSMINI_RegWriteFrameEx(int rows, int cols, IntegerReference xOff, IntegerReference yOff, IntegerReference width, IntegerReference height,
                               IntegerReference embLeft, IntegerReference embTop, IntegerReference embRight, IntegerReference embBottom);

    /**
     * Specify the grid-structure (rows, columns) of a tube rack and image-processing options for decoding
     *
     * @param rows          Number of rows in the tube rack to be decoded.
     * @param cols          Number of columns in the tube rack to be decoded.
     * @param filerLevel    Number of digital filters to be applied if the first decode attempt fails. If a tube fails to decode, the program
     *                      digitally process the tube image using a different filter and retry decoding. This process continues until a
     *                      barcode is obtained, or the specified number of filters have been exhausted. Set FilterLevel to 0 to disable
     *                      digital filter processing. The maximum value for FilterLevel is 10. We suggest using FilterLevel value of 5.
     * @param filterStr     Reserved for future enhancement. Should be set to 0 at this time.
     * @param detectMissing Set this flag to 1 to enable missing-tube-detection. This is useful for automatically skipping empty tube
     *                      positions (without lengthy retries). Set this flag to 0 to disable missing-tube-detection.
     * @param pctOverlap    Allow decoding to include border image from neighboring tubes, up to PctOverlap (percent) of barcode size
     *                      in each direction. For example, if a cell (containing a tube barcode) is 100 x 100 pixels, and PctOverlap is
     *                      20%, then the decoder will extend the tube image to include 20 pixels into the neighboring cell’s image. This
     *                      results in a 140 x 140 image used for decoding. This is useful for decoding tube racks with non-uniform
     *                      grid partition or racks with very narrow distance between tubes. Please limit this value to 0% – 35% range,
     *                      as larger values would increase the chance of decoding neighboring tube’s barcode
     * @param powerMode     Set to TRUE to allow decoding in PowerMode. PowerMode enhances decoding for certain 2D barcodes,
     *                      especially smaller barcodes or those with narrow quiet-zones. However, it may increase decoding time.
     * @return Always 0
     */
    int SSMINI_SetDecoderEx(int rows, int cols, int filerLevel, int filterStr, int detectMissing, int pctOverlap, boolean powerMode);

}

