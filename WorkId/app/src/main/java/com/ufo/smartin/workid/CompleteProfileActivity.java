package com.ufo.smartin.workid;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import com.getbase.floatingactionbutton.FloatingActionButton;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import harmony.java.awt.Color;
import utilities.AcademicFormation;
import utilities.JSONParser;
import utilities.ProfessionalExperience;
import utilities.Reference;
import utilities.User;

public class CompleteProfileActivity extends AppCompatActivity {

    private final static String NOMBRE_DIRECTORIO = "MiCv";
    private final static String ETIQUETA_ERROR = "ERROR";
    private static final String PATH =LaunchActivity.IP+"/images/";

    private User user;
    private Menu menu;
    private FloatingActionButton fab_edit;
    private FloatingActionButton fab_config;
    private FloatingActionButton fab_pdf;
    private TextView name;
    private TextView location;
    private TextView title;
    private TextView resume;
    private TextView watch_video;
    private ImageView profilePicture;
    private LinearLayout academicList;
    public ArrayList<AcademicFormation> listAcademics;
    private LinearLayout professionalsList;
    public ArrayList<ProfessionalExperience> listProfessionals;
    private LinearLayout referencesList;
    public ArrayList<Reference> listReferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        user=(User)getIntent().getSerializableExtra("user");
        Intent intent = new Intent();
        intent.putExtra("user", user);
        setResult(RESULT_OK, intent);

        name=(TextView)findViewById(R.id.name);
        location=(TextView)findViewById(R.id.location);
        title=(TextView)findViewById(R.id.title);
        resume=(TextView)findViewById(R.id.resume);
        watch_video=(TextView)findViewById(R.id.watch_video);

        name.setText(user.getName());
        location.setText(user.getLocation());
        title.setText(user.getTitle());
        resume.setText(user.getResume());

        listAcademics=user.getAcademics();
        academicList=(LinearLayout)findViewById(R.id.academics);
        if(listAcademics!=null) {
            loadListA(listAcademics);
        }
        listProfessionals=user.getProfessionals();
        professionalsList=(LinearLayout)findViewById(R.id.professionals);
        if(listProfessionals!=null) {
            loadListP(listProfessionals);
        }
        listReferences=user.getReferences();
        referencesList=(LinearLayout)findViewById(R.id.references);
        if(listReferences!=null) {
            loadListR(listReferences);
        }
        fab_edit=(com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.edit);
        fab_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToEditProfile = new Intent(getApplicationContext(), ProfileEditorActivity.class);
                goToEditProfile.putExtra("user", user);
                startActivityForResult(goToEditProfile, 2);
            }
        });

        fab_config=(com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.settings);
        fab_config.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToConfiguration=new Intent(getApplicationContext(),ConfigurationActivity.class);
                goToConfiguration.putExtra("user",user);
                startActivityForResult(goToConfiguration, 2);
            }
        });

        fab_pdf=(com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.pdf);
        fab_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PDFGenerate();
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.pdf_generated), Toast.LENGTH_SHORT).show();
            }
        });

        profilePicture=(ImageView) findViewById(R.id.profile_picture);
        if(!user.getImage().equals("")&&user.getImage()!=null){
            Bitmap imag = null;
            byte[] decodedString = Base64.decode(user.getImage(), Base64.DEFAULT);
            imag = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            profilePicture.setImageBitmap(imag);
        }

        watch_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user.isAccount().equals("true")) {
                    String path = PATH + user.getEmail().split("\\.")[0] + ".mp4";
                    //String path=PATH+"msantim@hotmail.mp4";
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(path));
                    intent.setDataAndType(Uri.parse(path), "video/mp4");
                    startActivity(intent);
                }else{
                    new AlertDialog.Builder(CompleteProfileActivity.this)
                            .setTitle("Mejora tu cuenta")
                            .setMessage("Para agregar un video a su cuenta debe mejorarla. \n" +
                                    "Usted no posee una cuenta mejorada. Para hacerlo edite su perfil y mejore su cuenta.")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setIcon(R.drawable.upgrade_selected)
                            .show();
                }
            }
        });

        //new HttpRequestTask_getImage().execute();
        //getImage();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.menu=menu;
        getMenuInflater().inflate(R.menu.menu_profile, this.menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_pdf:
                PDFGenerate();
                Toast.makeText(getApplicationContext(),
                        getResources().getString(R.string.pdf_generated )+ " en: "+
                        getRuta().getPath(), Toast.LENGTH_SHORT).show();
                return  true;
            case R.id.action_edit:
                Intent goToEditProfile = new Intent(getApplicationContext(), ProfileEditorActivity.class);
                goToEditProfile.putExtra("user", user);
                startActivityForResult(goToEditProfile, 2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void loadListA(ArrayList<AcademicFormation> list){
        listAcademics=list;
        displayListA();
    }

    public void displayListA(){
        ListAdapterA adapter=new ListAdapterA(getApplicationContext(), listAcademics);
        for(int i=0;i<adapter.getCount();i++)
        academicList.addView(adapter.getView(i, null, null));
    }

     public void loadListP(ArrayList<ProfessionalExperience> list){
        listProfessionals=list;
        displayListP();
    }

    public void displayListP(){
        ListAdapterP adapter=new ListAdapterP(getApplicationContext(), listProfessionals);
        for(int i=0;i<adapter.getCount();i++)
            professionalsList.addView(adapter.getView(i, null, null));
    }

    public void loadListR(ArrayList<Reference> list){
        listReferences=list;
        displayListR();
    }

    public void displayListR(){
        ListAdapterR adapter=new ListAdapterR(getApplicationContext(), listReferences);
        for(int i=0;i<adapter.getCount();i++)
            referencesList.addView(adapter.getView(i, null, null));
    }

    //----------------------------------------------------------------------------------------------------------------------------
    public class ListAdapterA extends BaseAdapter {

        private Context context;
        private List<AcademicFormation> list;

        public ListAdapterA(Context context, List<AcademicFormation> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View rowView = convertView;

            if (convertView == null) {
                // Create a new view into the list.
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                rowView = inflater.inflate(R.layout.academic_item, parent, false);
            }

            // Set data into the view.
            TextView academic = (TextView) rowView.findViewById(R.id.academic);
            TextView period = (TextView) rowView.findViewById(R.id.period);
            TextView place = (TextView) rowView.findViewById(R.id.place);
            TextView degree = (TextView) rowView.findViewById(R.id.degree);

            AcademicFormation item = this.list.get(position);
            academic.setText(item.academic);
            period.setText("("+item.iniDate+"-"+item.endDate+")");
            place.setText(item.place);
            degree.setText(item.degree);
            return rowView;
        }
    }

    //----------------------------------------------------------------------------------------------------------------------------
    public class ListAdapterP extends BaseAdapter {

        private Context context;
        private List<ProfessionalExperience> list;

        public ListAdapterP(Context context, List<ProfessionalExperience> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View rowView = convertView;

            if (convertView == null) {
                // Create a new view into the list.
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                rowView = inflater.inflate(R.layout.professional_item, parent, false);
            }

            // Set data into the view.
            TextView pos = (TextView) rowView.findViewById(R.id.position);
            TextView period = (TextView) rowView.findViewById(R.id.period);
            TextView place = (TextView) rowView.findViewById(R.id.place);


            ProfessionalExperience item = this.list.get(position);
            pos.setText(item.position);
            period.setText("("+item.iniDate+"-"+item.endDate+")");
            place.setText(item.place);
            return rowView;
        }
    }
    //----------------------------------------------------------------------------------------------------------------------------
    public class ListAdapterR extends BaseAdapter {

        private Context context;
        private List<Reference> list;

        public ListAdapterR(Context context, List<Reference> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View rowView = convertView;

            if (convertView == null) {
                // Create a new view into the list.
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                rowView = inflater.inflate(R.layout.reference_item, parent, false);
            }

            // Set data into the view.
            TextView name = (TextView) rowView.findViewById(R.id.name);
            TextView relation = (TextView) rowView.findViewById(R.id.relation);
            TextView occupation = (TextView) rowView.findViewById(R.id.occupation);
            TextView contact = (TextView) rowView.findViewById(R.id.contact);

            Reference item = this.list.get(position);
            name.setText(item.getName());
            if(item.getType()==Reference.FAMILIAR) {
                relation.setText(item.getRelation()+" ("+getResources().getString(R.string.familiar_reference)+")");
            }else{
                relation.setText("("+getResources().getString(R.string.personal_reference)+")");
            }
            occupation.setText(item.getOccupation());
            contact.setText(item.getContact());
            return rowView;
        }
    }

    //----------------------------------------------------------------------------------------------------------------------------
    public void PDFGenerate(){
                /*
                // create a new document
                PdfDocument document = new PdfDocument();

                // crate a page description
                PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(500, 800, 1).create();

                // start a page
                PdfDocument.Page page = document.startPage(pageInfo);

                // draw something on the page
                View content = findViewById(R.id.presentation);
                content.draw(page.getCanvas());
                // finish the page
                document.finishPage(page);
                // add more pages

                PdfDocument.Page page2 = document.startPage(pageInfo);
                // draw something on the page
                View contentAF = findViewById(R.id.af);
                contentAF.draw(page2.getCanvas());
                // finish the page
                document.finishPage(page2);

                PdfDocument.Page page3 = document.startPage(pageInfo);
                // draw something on the page
                View contentPE = findViewById(R.id.pe);
                contentPE.draw(page3.getCanvas());
                // finish the page
                document.finishPage(page3);

                PdfDocument.Page page4 = document.startPage(pageInfo);
                // draw something on the page
                View contentMR = findViewById(R.id.mr);
                contentMR.draw(page4.getCanvas());
                // finish the page
                document.finishPage(page4);
                // write the document content
                try {
                    File f = new File(Environment.getExternalStorageDirectory().getPath() + "/"+name.getText().toString()+".pdf");
                    FileOutputStream fos = new FileOutputStream(f);
                    document.writeTo(fos);
                    document.close();
                    fos.close();
                    Log.d("Salio",Environment.getExternalStorageDirectory().getPath().toString());
                } catch (IOException e) {
                    throw new RuntimeException("Error generating file", e);
                }
*/
                Document documento = new Document();

                try {

                    // Creamos el fichero con el nombre que deseemos.
                    File f = crearFichero(user.getName()+".pdf");

                    // Creamos el flujo de datos de salida para el fichero donde
                    // guardaremos el pdf.
                    FileOutputStream ficheroPdf = new FileOutputStream(
                            f.getAbsolutePath());

                    // Asociamos el flujo que acabamos de crear al documento.
                    PdfWriter writer = PdfWriter.getInstance(documento, ficheroPdf);

                    // Incluimos el píe de página y una cabecera
                    /*
                    HeaderFooter cabecera = new HeaderFooter(new Phrase(
                            "Esta es mi cabecera"), false);
                    HeaderFooter pie = new HeaderFooter(new Phrase(
                            "Este es mi pie de página"), false);

                    documento.setHeader(cabecera);
                    documento.setFooter(pie);
                    */
                    // Abrimos el documento.
                    documento.open();

                    // Añadimos un título con una fuente personalizada.
                    Font font = FontFactory.getFont(FontFactory.HELVETICA, 28,
                            Font.BOLD, new Color(148,17,27));
                    Paragraph ap = new Paragraph("Datos personales", font);
                    documento.add(ap);
                    documento.add(new Paragraph("\n \n"));

                    Font font_tx = FontFactory.getFont(FontFactory.HELVETICA, 20,
                            Font.NORMAL);
                    Font font_tl = FontFactory.getFont(FontFactory.HELVETICA, 20,
                            Font.NORMAL, new Color(148,17,27));

                    documento.add(new Paragraph("Nombre: "+user.getName(),font_tx));
                    documento.add(new Paragraph("Titulo: "+user.getTitle(),font_tx));
                    documento.add(new Paragraph("Mail: "+user.getEmail(),font_tx));
                    documento.add(new Paragraph("Ciudad: "+user.getLocation(), font_tx));
                    documento.add(new Paragraph("\n \n"));

                    PdfPTable tabla = new PdfPTable(2);
                    String info = user.getResume();
                    Paragraph p = new Paragraph(info,font_tx);
                    tabla.addCell(p);

                    // Insertamos una imagen que se encuentra en los recursos de la
                    // aplicación.
                    if(!user.getImage().equals("")&&user.getImage()!=null){
                        Bitmap imag = null;
                        byte[] decodedString = Base64.decode(user.getImage(), Base64.DEFAULT);
                        imag = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        imag.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        Image imagen = Image.getInstance(stream.toByteArray());
                        imagen.scaleAbsolute(128,128);
                        tabla.addCell(imagen);
                    }


                    documento.add(tabla);

                    documento.add(new Paragraph("Datos academicos", font));
                    for(int i = 0; i<user.getAcademics().size();i++){
                        AcademicFormation a = user.getAcademics().get(i);
                        String dg = a.degree+":";
                        String af;
                        if(!a.place.equals("") && a.place!=null) {
                            af = a.academic + " en " + a.place + " (" + a.iniDate + "-" + a.endDate + ")";
                        }else{
                            af=a.academic + " (" + a.iniDate + "-" + a.endDate + ")";
                        }
                        documento.add(new Paragraph(dg, font_tl));
                        documento.add(new Paragraph(af, font_tx));
                        documento.add(new Paragraph("\n"));
                    }
                    documento.add(new Paragraph("\n"));

                    documento.add(new Paragraph("Experiencia laboral", font));
                    for(int i = 0; i<user.getProfessionals().size();i++){
                        ProfessionalExperience a = user.getProfessionals().get(i);
                        String dg = a.position+":";
                        String af= "En " + a.place + " (" + a.iniDate + "-" + a.endDate + ")";
                        documento.add(new Paragraph(dg, font_tl));
                        documento.add(new Paragraph(af, font_tx));
                        documento.add(new Paragraph("\n"));
                    }
                    documento.add(new Paragraph("\n"));

                    documento.add(new Paragraph("Referencias", font));
                    for(int i = 0; i<user.getReferences().size();i++){
                        Reference a = user.getReferences().get(i);
                        int ty = a.getType();
                        String dg;
                        if(ty == Reference.PROFESSIONAL){
                            dg="Referencia profesional: ";
                        }else if(ty == Reference.FAMILIAR){
                            dg="Referencia familiar: ";
                        }else{
                            dg="Referencia personal: ";
                        }
                        String rel= "Relación: "+a.getRelation();
                        String nom= "Nombre:: "+a.getName();
                        String con= "Contacto: "+a.getContact();
                        documento.add(new Paragraph(dg, font_tl));
                        documento.add(new Paragraph(nom, font_tx));
                        documento.add(new Paragraph(rel, font_tx));
                        documento.add(new Paragraph(con, font_tx));
                        documento.add(new Paragraph("\n"));
                    }
                    documento.add(new Paragraph("\n"));

                } catch (DocumentException e) {

                    Log.e(ETIQUETA_ERROR, e.getMessage());

                } catch (IOException e) {

                    Log.e(ETIQUETA_ERROR, e.getMessage());

                } finally {

                    // Cerramos el documento.
                    documento.close();
                }
    }

    /**
     * Crea un fichero con el nombre que se le pasa a la función y en la ruta
     * especificada.
     *
     * @param nombreFichero
     * @return
     * @throws IOException
     */
    public static File crearFichero(String nombreFichero) throws IOException {
        File ruta = getRuta();
        File fichero = null;
        if (ruta != null)
            fichero = new File(ruta, nombreFichero);
        return fichero;
    }

    /**
     * Obtenemos la ruta donde vamos a almacenar el fichero.
     *
     * @return
     */
    public static File getRuta() {

        // El fichero será almacenado en un directorio dentro del directorio
        // Descargas
        File ruta = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            ruta = new File(
                    Environment
                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    NOMBRE_DIRECTORIO);

            if (ruta != null) {
                if (!ruta.mkdirs()) {
                    if (!ruta.exists()) {
                        return null;
                    }
                }
            }
        } else {
        }

        return ruta;
    }

    //------------------------------------------------------------------------------------------------------------------
    // CONEXION DE PRUEBA
    //------------------------------------------------------------------------------------------------------------------
    private class HttpRequestTask_getImage extends AsyncTask<Void, Void, String>
    {
        public HttpRequestTask_getImage(){
        }
        @Override
        protected String doInBackground(Void... params)
        {   String ret="404";
            try
            {
                final String url = "http://192.168.0.17:5000/getImageText.php";
                JSONParser jParser = new JSONParser();
                String noti = jParser.getImage(url);
                ret=noti;
                return ret;

            } catch (Exception e)
            {
                Log.e("MainActivity", e.getMessage(), e);
            }

            return ret;
        }

        @Override
        protected void onPostExecute(String image) {
            Log.d("TAG IMAGE", image);
            /*
            if(image!=null && !image.equals("404")) {
                Bitmap imag = null;
                byte[] decodedString = Base64.decode(image, Base64.DEFAULT);
                imag = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                profilePicture.setImageBitmap(imag);
            }
            */
        }
    }

    private void getImage() {

        class GetImage extends AsyncTask<String,Void,Bitmap>{
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(CompleteProfileActivity.this, "Uploading...", null,true,true);
            }

            @Override
            protected void onPostExecute(Bitmap b) {
                super.onPostExecute(b);
                loading.dismiss();
                profilePicture.setImageBitmap(b);
            }

            @Override
            protected Bitmap doInBackground(String... params) {

                String add = "http://192.168.0.17:5000/getImage.php";
                URL url = null;
                Bitmap image = null;
                try {
                    url = new URL(add);
                    image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    Log.d("TAG IMAGE",getStringImage(image));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return image;
            }
        }

        GetImage gi = new GetImage();
        gi.execute();
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

}
