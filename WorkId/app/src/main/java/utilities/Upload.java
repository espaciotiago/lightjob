package utilities;

import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ufo.smartin.workid.LaunchActivity;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by 'Santiago on 26/6/2016.
 */
public class Upload {
    //public static final String UPLOAD_URL= "http://www.lightjob.org/videoUpload.php";
    public static String UPLOAD_URL="http://138.128.188.98/~lightjo1/videoUpload.php";
    //public static String UPLOAD_URL="http://cosmoagro.com/sdcpruebas/prueba_tiago/videoUpload.php";
    //public static final String UPLOAD_URL= "http://192.168.0.25:5000/lightjob/videoUpload.php";

    private int serverResponseCode;

    /*

    public String uploadVideo(String file,String name) {

        String fileName = "video.mp4";//name+".mp4";
        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 4 * 1024;

        File sourceFile = new File(file);
        if (!sourceFile.isFile()) {
            Log.e("Huzza", "Source File Does not exist");
            return null;
        }

        try {
            FileInputStream fileInputStream = new FileInputStream(sourceFile);
            URL url = new URL(UPLOAD_URL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            //conn.setFixedLengthStreamingMode(10*1024*1024);
            conn.setChunkedStreamingMode(maxBufferSize);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("ENCTYPE", "multipart/form-data");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            conn.setRequestProperty("myFile", fileName);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                        dos = new DataOutputStream(conn.getOutputStream());

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"myFile\";filename=\""
                    + fileName + "\"" + lineEnd);
            dos.writeBytes(lineEnd);

            Log.d("NAMES",fileName);

            bytesAvailable = fileInputStream.available();
            Log.i("Huzza", "Initial .available : " + bytesAvailable);

            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];
            Log.d("SIZE",bufferSize+"");

            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }

            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            serverResponseCode = conn.getResponseCode();
            Log.d("RET", conn.getResponseMessage()+" "+serverResponseCode);


            if(serverResponseCode!=200){
                BufferedReader br = new BufferedReader(new InputStreamReader((conn.getErrorStream())));
                String output;
                String ss="";
                try {
                    while ((output = br.readLine()) != null) {
                        ss+=output;
                    }
                    Log.d("ERROR", ss);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            fileInputStream.close();
            dos.flush();
            dos.close();

        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (serverResponseCode == 200) {
            StringBuilder sb = new StringBuilder();
            try {
                BufferedReader rd = new BufferedReader(new InputStreamReader(conn
                        .getInputStream()));
                String line;
                while ((line = rd.readLine()) != null) {
                    sb.append(line);
                }
                rd.close();
                //conn.disconnect();
            } catch (IOException ioex) {
            }
            return sb.toString();
        }else {
            conn.disconnect();
            return "Could not upload";
        }
    }
    */

    public String uploadVideo(String file, String name) {

        String fileName = name+".mp4";
        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 4 * 1024;
        boolean send = true;

        File sourceFile = new File(file);
        if (!sourceFile.isFile()) {
            Log.e("Huzza", "Source File Does not exist");
            return null;
        }

        try {
            FileInputStream fileInputStream = new FileInputStream(sourceFile);
            URL url = new URL(UPLOAD_URL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            //conn.setChunkedStreamingMode(maxBufferSize);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            //conn.setRequestProperty("Transfer-Encoding", "chunked");
            conn.setRequestProperty("ENCTYPE", "multipart/form-data");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            conn.setRequestProperty("myFile", fileName);
            //conn.setFixedLengthStreamingMode(file.getBytes().length+boundary.getBytes().length);
            dos = new DataOutputStream(conn.getOutputStream());

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"myFile\";filename=\"" + fileName + "\"" + lineEnd);
            dos.writeBytes(lineEnd);

            bytesAvailable = fileInputStream.available();
            Log.i("Huzza", "Initial .available : " + bytesAvailable);

            if(bytesAvailable<15000000) {
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {
                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }

                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                serverResponseCode = conn.getResponseCode();
                Log.d("RET", conn.getResponseMessage()+" "+serverResponseCode);

                if(serverResponseCode!=200){
                    BufferedReader br = new BufferedReader(new InputStreamReader((conn.getErrorStream())));
                    String output;
                    String ss="";
                    try {
                        while ((output = br.readLine()) != null) {
                            ss+=output;
                        }
                        Log.d("ERROR", ss);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                fileInputStream.close();
                dos.flush();
                dos.close();
            }else{
                send = false;
            }

        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }



        if(send) {
            if (serverResponseCode == 200) {
                StringBuilder sb = new StringBuilder();
                try {
                    BufferedReader rd = new BufferedReader(new InputStreamReader(conn
                            .getInputStream()));
                    String line;
                    while ((line = rd.readLine()) != null) {
                        sb.append(line);
                    }
                    rd.close();
                } catch (IOException ioex) {
                }
                return sb.toString();
            } else {
                return "Could not upload";
            }
        }else {
            return "size limit";
        }
    }

}
