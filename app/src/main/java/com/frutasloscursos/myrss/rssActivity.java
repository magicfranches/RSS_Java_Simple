package com.frutasloscursos.myrss;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class rssActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;
    ArrayList<String> noticias;
    ArrayList<String> links;
    ArrayList<String> pubDate;
    ProgressBar loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rss);

        noticias = new ArrayList<>();   //Array Noticias
        links = new ArrayList<>();      //Array Links
        pubDate = new ArrayList<>();    //Array Fecha de publicacion

        //Recycler
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new MainAdapter(noticias, links, pubDate);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //ProgressBar
        loading = (ProgressBar) findViewById(R.id.progress_circular_bar);

        //Pone visible la view del ProgressBar y oculta la del RecyclerView
        loading.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(new Runnable() {
            @Override
            public void run() {
                //Background work here
                Exception exception = null;

                //Aqui la url del feed RSS
                try {
                    URL url = new URL("https://frutasloscursos.com/feed");

                    //Parser xml
                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(false);
                    XmlPullParser xpp = factory.newPullParser();
                    xpp.setInput(url.openConnection().getInputStream(), "UTF_8");
                    boolean insideItem = false;
                    int eventType = xpp.getEventType();

                    //Bucle lectura dentro de item
                    while (eventType != XmlPullParser.END_DOCUMENT){
                        if(eventType ==XmlPullParser.START_TAG){
                            if (xpp.getName().equalsIgnoreCase("item")){
                                insideItem = true;
                            }
                            //Extraccion de del string de dentro de la etiqueta.
                            else if (xpp.getName().equalsIgnoreCase("title")){
                                if(insideItem){
                                    noticias.add(xpp.nextText());
                                }
                            }
                            else if (xpp.getName().equalsIgnoreCase("link")){
                                if(insideItem){
                                    links.add(xpp.nextText());
                                }
                            }
                            else if (xpp.getName().equalsIgnoreCase("pubDate")){
                                if(insideItem){
                                    pubDate.add(xpp.nextText());
                                }
                            }
                        }
                        else if (eventType == XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("item")){
                            insideItem = false;
                        }

                        eventType = xpp.next();
                    }
                } catch (XmlPullParserException | IOException e){
                    exception = e;
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //UI Thread work here do
                        mRecyclerView.setAdapter(mAdapter);
                        //Mensaje de noticias cargadas
                        Toast toast1 = Toast.makeText(getApplicationContext(),"Noticias cargadas!", Toast.LENGTH_SHORT);
                        toast1.show();
                        //Oculta la view de la ProgressBar y muestra el RecyclerView
                        loading.setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
    }
}