jpackage --name DLM_SCANNER --type msi --input build/libs --main-jar DLM_SCANNER-0.1-all.jar --main-class com.nutrix.Application --java-options "-DCP_SERVER_URL=https://cp-backend-dev.nurixtx.net/api/v1/ -Djna.library.path='C:\\Program Files (x86)\\BioMicroLab\\SampleScan Mini SDK'" --runtime-image jdk-14+fx --win-console
pause
