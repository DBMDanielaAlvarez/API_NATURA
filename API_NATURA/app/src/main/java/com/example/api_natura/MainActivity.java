package com.example.api_natura;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button btnGuardar;
    private Button btnBuscar;
    private Button btnEliminar;
    private Button btnActualizar;
    private Button btnLimpiar;

    private EditText etClave;
    private EditText etDescripcion;
    private EditText etModelo;
    private EditText etPrecioCatalogo;
    private EditText etPrecioVenta;
    private EditText etExistencia;

    private ListView lvProductos;

    private RequestQueue colaPeticiones;

    private JsonArrayRequest jsonArrayRequest;

    private ArrayList<String> origenDatos = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private String url = "http://192.168.0.113:3400/";
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnGuardar = findViewById(R.id.btnGuardar);
        btnBuscar = findViewById(R.id.btnBuscar);
        btnEliminar = findViewById(R.id.btnEliminar);
        btnActualizar = findViewById(R.id.btnActualizar);
        btnLimpiar = findViewById(R.id.btnLimpiar);

        etClave = findViewById(R.id.etClave);
        etDescripcion = findViewById(R.id.etDescripcion);
        etModelo = findViewById(R.id.etModelo);
        etPrecioCatalogo = findViewById(R.id.etPrecioCatalogo);
        etPrecioVenta = findViewById(R.id.etPrecioVenta);
        etExistencia = findViewById(R.id.etExistencia);

        lvProductos = findViewById(R.id.lvProductos);

        colaPeticiones = Volley.newRequestQueue(this);
        listarProductos();

        btnLimpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etClave.setText("");
                etDescripcion.setText("");
                etModelo.setText("");
                etPrecioCatalogo.setText("");
                etPrecioVenta.setText("");
                etExistencia.setText("");
            }
        });

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsonObjectRequest peticion = new JsonObjectRequest(
                        Request.Method.GET,
                        url + etClave.getText().toString(),
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                if (response.has("status"))
                                    Toast.makeText(MainActivity.this, "Not Found", Toast.LENGTH_SHORT).show();
                                else {
                                    try {
                                        etDescripcion.setText(response.getString("descripcion"));
                                        etModelo.setText(response.getString("modelo"));
                                        etPrecioCatalogo.setText(String.valueOf(response.getInt("preciocatalogo")));
                                        etPrecioVenta.setText(String.valueOf(response.getInt("precioventa")));
                                        etExistencia.setText(String.valueOf(response.getInt("existencia")));
                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                );
                colaPeticiones.add(peticion);
            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject producto = new JSONObject();
                try {
                    producto.put("clave", etClave.getText().toString());
                    producto.put("descripcion", etDescripcion.getText().toString());
                    producto.put("modelo", etModelo.getText().toString());
                    producto.put("preciocatalogo", Float.parseFloat(etPrecioCatalogo.getText().toString()));
                    producto.put("precioventa", Float.parseFloat(etPrecioVenta.getText().toString()));
                    producto.put("existencia", Integer.parseInt(etExistencia.getText().toString()));


                    etClave.setText("");
                    etDescripcion.setText("");
                    etModelo.setText("");
                    etPrecioCatalogo.setText("");
                    etPrecioVenta.setText("");
                    etExistencia.setText("");
                } catch (JSONException e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                        Request.Method.POST,
                        url + "insert/",
                        producto,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    if (response.getString("status").equals("Articulo insertado"))
                                        Toast.makeText(MainActivity.this, "Articulo insertado con Exito", Toast.LENGTH_SHORT).show();
                                    listarProductos();
                                } catch (JSONException e) {
                                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                );

                colaPeticiones.add(jsonObjectRequest);
            }
        });


        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etClave.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Primero busca un articulo", Toast.LENGTH_SHORT).show();
                } else {
                    JSONObject productos = new JSONObject();
                    try {
                        productos.put("clave", etClave.getText().toString());
                        if (!etDescripcion.getText().toString().isEmpty()) {
                            productos.put("descripcion", etDescripcion.getText().toString());
                        }

                        if (!etModelo.getText().toString().isEmpty()) {
                            productos.put("modelo", etModelo.getText().toString());
                        }

                        if (!etPrecioCatalogo.getText().toString().isEmpty()) {
                            productos.put("preciocatalogo", Float.parseFloat(etPrecioCatalogo.getText().toString()));
                        }

                        if (!etPrecioVenta.getText().toString().isEmpty()) {
                            productos.put("precioventa", Float.parseFloat(etPrecioVenta.getText().toString()));
                        }

                        if (!etExistencia.getText().toString().isEmpty()) {
                            productos.put("existencia", Float.parseFloat(etExistencia.getText().toString()));
                        }

                    } catch (JSONException e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    JsonObjectRequest actualizar = new JsonObjectRequest(
                            Request.Method.PUT,
                            url + "update/" + etClave.getText().toString(),
                            productos,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        if (response.getString("status").equals("Articulo Actualizado")) {
                                            Toast.makeText(MainActivity.this, "Articulo actualizado con EXITO!", Toast.LENGTH_SHORT).show();
                                            etClave.setText("");
                                            etDescripcion.setText("");
                                            etModelo.setText("");
                                            etPrecioCatalogo.setText("");
                                            etPrecioVenta.setText("");
                                            etExistencia.setText("");
                                            adapter.clear();
                                            lvProductos.setAdapter(adapter);
                                            listarProductos();
                                        } else if (response.getString("status").equals("Not Found")) {
                                            Toast.makeText(MainActivity.this, "Articulo no encontrado", Toast.LENGTH_SHORT).show();
                                        }

                                    } catch (JSONException e) {
                                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                    );
                    colaPeticiones.add(actualizar);
                }
            }
        });


        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etClave.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Ingrese la clave", Toast.LENGTH_SHORT).show();
                } else {
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                            Request.Method.DELETE,
                            url + "delete/" + etClave.getText().toString(),
                            null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        if (response.getString("status").equals("Articulo Eliminado")) {
                                            Toast.makeText(MainActivity.this, "Articulo Eliminado", Toast.LENGTH_SHORT).show();
                                            etClave.setText("");
                                            etDescripcion.setText("");
                                            etModelo.setText("");
                                            etPrecioCatalogo.setText("");
                                            etPrecioVenta.setText("");
                                            etExistencia.setText("");


                                            adapter.clear();
                                            lvProductos.setAdapter(adapter);
                                            listarProductos();
                                        } else if (response.getString("status").equals("Not Found")) {
                                            Toast.makeText(MainActivity.this, "Articulo no encontrado", Toast.LENGTH_SHORT).show();
                                        }

                                    } catch (JSONException e) {
                                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                    );
                    colaPeticiones.add(jsonObjectRequest);
                }
            }
        });
    }
    protected void listarProductos(){
        jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for(int i=0; i<response.length();i++){
                            try {
                                String clave=response.getJSONObject(i).getString("clave")+":";
                                String descripcion=response.getJSONObject(i).getString("descripcion") +":";
                                String modelo=response.getJSONObject(i).getString("modelo") +":";
                                origenDatos.add(clave+":"+descripcion+":"+modelo);
                            }catch (JSONException e){

                            }
                        }

                        adapter = new ArrayAdapter<>(MainActivity.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,origenDatos);
                        lvProductos.setAdapter(adapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT). show();
                    }
                }
        );
        colaPeticiones.add(jsonArrayRequest);
    }
}