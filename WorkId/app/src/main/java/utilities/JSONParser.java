package utilities;

import android.annotation.TargetApi;
import android.os.Build;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by smartin on 19/04/2016.
 */
public class JSONParser {

    public JSONParser() {

    }

    //GET PHOTO
    public String getImage(String url) {
        String ret = "";
        try {
            URL listItemsURL = new URL(url);

            URLConnection tc = listItemsURL.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(tc.getInputStream()));

            String line;

            while ((line = in.readLine()) != null) {
                ret += line;
            }

        } catch (MalformedURLException e) {

        } catch (IOException e) {

        }
        return ret;
    }

    //POST PHOTO
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public String uploadImage(String url, String user, String photo, User u) {
        String ret = "";
        try {
            String data = URLEncoder.encode("mail", "UTF-8") + "=" + URLEncoder.encode(user, "UTF-8");
            data += "&" + URLEncoder.encode("image", "UTF-8") + "=" + URLEncoder.encode(photo, "UTF-8");
            URL avisoURL = new URL(url);
            URLConnection tc = avisoURL.openConnection();


            tc.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(tc.getOutputStream());
            wr.write(data);
            wr.flush();

            BufferedReader rd = new BufferedReader(new InputStreamReader(tc.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                ret = line;
            }
            wr.close();
            rd.close();
            return ret;

        } catch (MalformedURLException e) {
            return e.getMessage();
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    //POST User
    public String uploadUser(String url, User u) {
        String ret = "";
        try {
            Gson gson = new Gson();
            String json = gson.toJson(u);
            Log.d("JSON", json);
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            URL url1 = new URL(url);
            urlConnection = (HttpURLConnection) url1.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");
            Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
            writer.write(String.valueOf(json));
            writer.close();
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String inputLine;
            while ((inputLine = reader.readLine()) != null)
                buffer.append(inputLine + "\n");
            if (buffer.length() == 0) {
                return null;
            }
            ret = buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    //POST PHOTO
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public String createUser(String url, String mail, String pass, String name, String image,
                             String location, String tittle, String resume, String account) {
        String ret = "";
        try {
            String data = URLEncoder.encode("mail", "UTF-8") + "=" + URLEncoder.encode(mail, "UTF-8");
            data += "&" + URLEncoder.encode("image", "UTF-8") + "=" + URLEncoder.encode(image, "UTF-8");
            data += "&" + URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8");
            data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(pass, "UTF-8");
            data += "&" + URLEncoder.encode("location", "UTF-8") + "=" + URLEncoder.encode(location, "UTF-8");
            data += "&" + URLEncoder.encode("title", "UTF-8") + "=" + URLEncoder.encode(tittle, "UTF-8");
            data += "&" + URLEncoder.encode("resume", "UTF-8") + "=" + URLEncoder.encode(resume, "UTF-8");
            data += "&" + URLEncoder.encode("account", "UTF-8") + "=" + URLEncoder.encode(account, "UTF-8");
            URL avisoURL = new URL(url);
            URLConnection tc = avisoURL.openConnection();


            tc.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(tc.getOutputStream());
            wr.write(data);
            wr.flush();

            BufferedReader rd = new BufferedReader(new InputStreamReader(tc.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                ret = line;
            }
            wr.close();
            rd.close();
            return ret;

        } catch (MalformedURLException e) {
            return e.getMessage();
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    //POST User
    public User authenticateUser(String url, String mail, String pass, String token) {
        User ret = null;
        try {
            String data = URLEncoder.encode("mail", "UTF-8") + "=" + URLEncoder.encode(mail, "UTF-8");
            data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(pass, "UTF-8");
            data += "&" + URLEncoder.encode("token", "UTF-8") + "=" + URLEncoder.encode(token, "UTF-8");


            URL avisoURL = new URL(url);
            URLConnection tc = avisoURL.openConnection();


            tc.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(tc.getOutputStream());
            wr.write(data);
            wr.flush();

            BufferedReader rd = new BufferedReader(new InputStreamReader(tc.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                if(!line.equals("")) {
                    ArrayList<AcademicFormation> academic=new ArrayList<AcademicFormation>();
                    ArrayList<ProfessionalExperience> profess=new ArrayList<ProfessionalExperience>();
                    ArrayList<Reference> refer=new ArrayList<Reference>();
                    JSONObject jo = new JSONObject(line);
                    //------------------------------------------------------------------------------------------------------
                    JSONArray jaAc=jo.getJSONArray("academic");
                    for(int i =0;i<jaAc.length();i++){
                        JSONObject joAc=jaAc.getJSONObject(i);
                        AcademicFormation a = new AcademicFormation(joAc.getString("degree"),
                                joAc.getString("discipline"),joAc.getString("place"),joAc.getString("academ"),
                                joAc.getString("iniDate"),joAc.getString("endDate"));
                        academic.add(a);
                    }
                    //------------------------------------------------------------------------------------------------------
                    JSONArray jaPr=jo.getJSONArray("professional");
                    for(int i =0;i<jaPr.length();i++){
                        JSONObject joPr=jaPr.getJSONObject(i);
                        ProfessionalExperience a = new ProfessionalExperience(joPr.getString("place"),
                                joPr.getString("position"),joPr.getString("iniDate"),joPr.getString("endDate"));
                        profess.add(a);
                    }
                    //------------------------------------------------------------------------------------------------------
                    JSONArray jaRe=jo.getJSONArray("reference");
                    for(int i =0;i<jaRe.length();i++){
                        JSONObject joRe=jaRe.getJSONObject(i);
                        Reference a = new Reference(joRe.getInt("type"),
                                joRe.getString("name"),joRe.getString("relation"),joRe.getString("occupation"),
                                joRe.getString("contact"));
                        refer.add(a);
                    }
                    //------------------------------------------------------------------------------------------------------
                    ret = new User(jo.getString("name"), jo.getString("mail"), jo.getString("pass"),
                            jo.getString("location"), jo.getString("title"), jo.getString("resume"), jo.getString("image"),
                            jo.getString("account"), academic, profess,refer);
                }

            }
            wr.close();
            rd.close();
            return ret;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ret;
    }


    //POST chg psd
    public String changePassword(String url, String mail, String pass) {
        String ret = null;
        try {
            String data = URLEncoder.encode("mail", "UTF-8") + "=" + URLEncoder.encode(mail, "UTF-8");
            data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(pass, "UTF-8");

            URL avisoURL = new URL(url);
            URLConnection tc = avisoURL.openConnection();


            tc.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(tc.getOutputStream());
            wr.write(data);
            wr.flush();

            BufferedReader rd = new BufferedReader(new InputStreamReader(tc.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                if(!line.equals("")) {
                    //------------------------------------------------------------------------------------------------------
                    ret += line;
                }

            }
            wr.close();
            rd.close();
            return ret;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }
}
