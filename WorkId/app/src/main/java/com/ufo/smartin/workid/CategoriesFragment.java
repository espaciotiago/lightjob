package com.ufo.smartin.workid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import utilities.Category;
import utilities.User;


public class CategoriesFragment extends Fragment {

    GridView gridview;

    public CategoriesFragment() {
        // Required empty public constructor
    }

    public static CategoriesFragment newInstance(User user) {
        CategoriesFragment fragment = new CategoriesFragment();
        Bundle args = new Bundle();
        args.putSerializable("user",user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final User user = (User) getArguments().getSerializable("user");
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_categories, container, false);

        final List<Category> listCategories=new ArrayList<>();
        listCategories.add(new Category(R.drawable.administracion,"Administración de empresas",11));
        listCategories.add(new Category(R.drawable.secretariado,"Administración y secretariado",12));
        listCategories.add(new Category(R.drawable.atencion,"Atención al cliente",14));
        listCategories.add(new Category(R.drawable.banca,"Banca y seguros",8));
        listCategories.add(new Category(R.drawable.medio_ambiente,"Calidad, I+D, PRL, medio ambiente",6));
        listCategories.add(new Category(R.drawable.ventas,"Comercial y ventas",1));
        listCategories.add(new Category(R.drawable.logistica,"Compras, logística y transporte",9));
        listCategories.add(new Category(R.drawable.inmobiliaria,"Construcción e inmobiliaria",13));
        listCategories.add(new Category(R.drawable.educacion,"Educación y formación",7));
        listCategories.add(new Category(R.drawable.hoteleria,"Hotelería y turismo",10));
        listCategories.add(new Category(R.drawable.ingenieria,"Ingeniería y producción",2));
        listCategories.add(new Category(R.drawable.legal,"Legal",19));
        listCategories.add(new Category(R.drawable.marketing,"Marketing y comunicación",17));
        listCategories.add(new Category(R.drawable.editorial,"Medios, editorial y artes gráficas",18));
        listCategories.add(new Category(R.drawable.contruccion,"Profesionales, artes y oficios",3));
        listCategories.add(new Category(R.drawable.rrhh,"Recursos humanos",15));
        listCategories.add(new Category(R.drawable.salud,"Sanidad, salud y servicios sociales",5));
        listCategories.add(new Category(R.drawable.ti,"Tecnología e información",4));
        listCategories.add(new Category(R.drawable.tele,"Telecomunicaciones",16));

        gridview = (GridView) view.findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(getActivity(),listCategories));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Category cat = listCategories.get(position);
                Intent goToSearch = new Intent(getActivity(),SearchResultsActivity.class);
                goToSearch.putExtra("category",cat.getTitle());
                goToSearch.putExtra("user", user);
                startActivity(goToSearch);
            }
        });

        return view;
    }

    //------------------------------------------------------------------------------------------------------
    public class ImageAdapter extends BaseAdapter {
        private Context context;
        private List<Category> categories;

        public ImageAdapter(Context context, List<Category> categories) {
            this.context = context;
            this.categories = categories;
        }

        @Override
        public int getCount() {
            return categories.size();
        }

        @Override
        public Object getItem(int position) {
            return categories.get(position);
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
                rowView = inflater.inflate(R.layout.category_item, parent, false);
            }

            // Set data into the view.
            ImageView image = (ImageView) rowView.findViewById(R.id.imageCat);
            TextView title = (TextView) rowView.findViewById(R.id.category);
            Category item = this.categories.get(position);
            title.setText(item.getTitle());
            image.setBackgroundResource(item.getImage());

            return rowView;
        }
    }
}
