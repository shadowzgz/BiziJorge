package com.seas.a10.bizijorge.fragments;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.seas.a10.bizijorge.R;
import com.seas.a10.bizijorge.adapters.CustomWindowInfoAdapter;
import com.seas.a10.bizijorge.beans.Estacion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static android.content.ContentValues.TAG;

/**
 * Fragmento que contiene el mapa en el que se muestran todas las estaciones de Bizi Zaragoza
 *http://zaragoza-sedeelectronica.github.io/rest/queries/
 * https://www.zaragoza.es/sede/servicio/urbanismo-infraestructuras/estacion-bicicleta.json?fl=id%2Ctitle%2Cestado%2CbicisDisponibles%2CanclajesDisponibles%2ClastUpdated%2Cgeometry&rf=html&srsname=wgs84&start=0&rows=200&distance=5000
 */
public class fMap extends Fragment implements OnMapReadyCallback {

    //region variables
    float DEFAULT_ZOOM = 13;
    Location mLastKnownLocation;
    //LatLng mDefaultLocation = new LatLng(41,41);
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    public static int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    boolean mLocationPermissionGranted = false;
    MapView mMapView;
    private GoogleMap mMap;
    SupportMapFragment mapFragment;
    FusedLocationProviderClient mFusedLocationProviderClient;
    ImageButton btnAnclajes;
    ImageButton btnBicis;

    boolean dataRecieved = false;
    JSONArray jsonArray = null;
    private String respuesta = "";
    //String pagina = "https://www.zaragoza.es/sede/servicio/urbanismo-infraestructuras/estacion-bicicleta.json?rows=200";
    String pagina = "https://www.zaragoza.es/sede/servicio/urbanismo-infraestructuras/estacion-bicicleta.json?fl=id%2Ctitle%2Cestado%2CbicisDisponibles%2CanclajesDisponibles%2ClastUpdated%2Cgeometry&rf=html&srsname=wgs84&start=0&rows=200&distance=5000";
    ArrayList<Estacion> listadoEstaciones;
    //endregion

    public fMap() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_f_map, container, false);

        //Llamamos al hilo secundario para recibir todos los datos de las estaciones. Ponemos .get
        //para que el hilo principal se pause hasta que tengamos los datos de las estaciones, y
        //de esta forma poder colocarlos en el mapa.
        try {
            new JsonTask().execute(pagina).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

        mMapView = v.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
//        Cargamos el mapa en el momento.
        mMapView.onResume();
        mMapView.getMapAsync(this);

        btnAnclajes = v.findViewById(R.id.btnAnclajes);
        btnAnclajes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setMarkersAnclajes();
            }
        });

        btnBicis = v.findViewById(R.id.btnBicis);
        btnBicis.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setMarkersBicis();
            }
        });

        return v;
    }

    //region Métodos mapa Google
    /*Todos los métodos necesarios para mostrar el mapa con la localización del usuario y el botón
    de redireccinamiento a la situación actual.
     */

//    public void setGoogleMap(View v , Bundle savedInstanceState){
//        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
//
//        mMapView = v.findViewById(R.id.mapView);
//        mMapView.onCreate(savedInstanceState);
////        Cargamos el mapa en el momento.
//        mMapView.onResume();
//        mMapView.getMapAsync(this);
//    }

    public void onMapReady(GoogleMap map) {
        mMap = map;
        mMap.setInfoWindowAdapter(new CustomWindowInfoAdapter(getContext(), listadoEstaciones));


        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();

        setMarkersBicis();
    }

    //Ponemos en el mapa las diferentes estaciones
    public void setMarkersBicis (){
        //Borramos los marcadores que se hayan colocado para poner los nuevos
        mMap.clear();

        //Colocaremos los marcadores según sus coordenadas y pondremos un color al marcador indicando el número de bicicletas restantes.
        try{
            for(Estacion i : listadoEstaciones){
                String id;
                id = "" + i.getId();

                if(i.getEstado() != "0PN") {
                    if(i.getBicisDisponibles() <= 0){
                        mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(Double.parseDouble(i.getEstacionLat()),
                                        Double.parseDouble(i.getEstacionLong())))
                                .title(id)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marcadorbicirojo))
                                .snippet("Bicis disponibles: " + i.getBicisDisponibles())
                        );



                    }else if(i.getBicisDisponibles() <= 4){
                        mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(Double.parseDouble(i.getEstacionLat()),
                                        Double.parseDouble(i.getEstacionLong())))
                                .title(id)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marcadorbicinaranja))
                                .snippet("Bicis disponibles: " + i.getBicisDisponibles())

                        );

                    }else if (i.getBicisDisponibles() > 4) {
                        mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(Double.parseDouble(i.getEstacionLat()),
                                        Double.parseDouble(i.getEstacionLong())))
                                .title(id)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marcadorbiciverde))
                                .snippet("Bicis disponibles: " + i.getBicisDisponibles())

                        );
                    }

                }else{
                    mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(Double.parseDouble(i.getEstacionLat()),
                                    Double.parseDouble(i.getEstacionLong())))
                            .title(id)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marcadorfueraservicio))
                    .snippet("Estación fuera de servicio."));

                }


            }


        }catch (Exception ex){
            Log.e("Exception: %s", ex.getMessage());
        }

    }

    public void setMarkersAnclajes (){
        mMap.clear();
        //Creamos todos los marcadores en sus correspondientes coordenadas y los coloreamos según el número de anclajes
        try{
            for(Estacion i : listadoEstaciones){
                String id;
                id = "" + i.getId();

                if(i.getEstado() != "0PN") {
                    if(i.getAnclajesDisponibles() <= 0){
                        mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(Double.parseDouble(i.getEstacionLat()),
                                        Double.parseDouble(i.getEstacionLong())))
                                .title(id)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marcadoranclajerojo))
                        );



                    }else if(i.getAnclajesDisponibles() <= 4){
                        mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(Double.parseDouble(i.getEstacionLat()),
                                        Double.parseDouble(i.getEstacionLong())))
                                .title(id)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marcadoranclajenaranja))
                        );

                    }else if (i.getAnclajesDisponibles() > 4){
                        mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(Double.parseDouble(i.getEstacionLat()),
                                        Double.parseDouble(i.getEstacionLong())))
                                .title(id)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marcadoranclajeverde))
                        );
                    }

                }else{
                    mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(Double.parseDouble(i.getEstacionLat()),
                                    Double.parseDouble(i.getEstacionLong())))
                            .title(id)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marcadorfueraservicio))
                            .snippet("Estación fuera de servicio."));

                }


            }


        }catch (Exception ex){
            Log.e("Exception: %s", ex.getMessage());
        }

    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(getActivity(), new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = (Location) task.getResult();
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), 13));
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }

        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mLocationPermissionGranted = false;
        Log.d("MENSAJEEEEEEE", "SE HA LLAMADO CORRECTAMENTE AL CALLBACKKKKKK!!!!!!!!!!!!");
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();

    }
    //endregion




    private class JsonTask extends AsyncTask<String, String, String> {



        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... params) {
            //publishProgress("Creando conexión...");
            JSONArray jArray = null;
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {

                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                if(stream!=null){
                    //Con UTF-8 nos aseguramos que no haya problemas con los acentos.
                    reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    stream.close();

                    //Recibimos el json y lo pasamos a String
                    respuesta = sb.toString();
                    //Convertomos el strin a JsonObject, ya que viene con una cabecera con diferentes JSONARRAy
                    JSONObject jobj = new JSONObject(respuesta);
                    //Seleccionamos dentro del objeto de json el array que queremos, que se llama Result, en el cual vienen todas las estaciones
                    jsonArray = new JSONArray(jobj.getJSONArray("result").toString());
                    Log.i("log_tag", "Cadena JSon: " + jsonArray.toString());
                    listadoEstaciones = Estacion.getArrayListFromJSon(jsonArray);


                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
    }

}
}




