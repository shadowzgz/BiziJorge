package com.seas.a10.bizijorge.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.seas.a10.bizijorge.R;
import com.seas.a10.bizijorge.data.sData;

/**
 * Fragmento con el que mostramos información varia
 */
public class fSobreNosotros extends Fragment {
    //region variables
    TextView tvLinkBizi;
    ImageView imgFace;
    ImageView imgTwitter;
    ImageView imgGmail;

    //endregion

    //region constructores
    public fSobreNosotros() {
        // Required empty public constructor
    }
    //endregion

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_f_sobre_nosotros, container, false);
        imgFace = v.findViewById(R.id.imgFacebook);
        imgTwitter = v.findViewById(R.id.imgTwitter);
        imgGmail = v.findViewById(R.id.imgGmail);
        tvLinkBizi = v.findViewById(R.id.tvLinkBizi);
        tvLinkBizi.setMovementMethod(LinkMovementMethod.getInstance());
        tvLinkBizi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                browserIntent.setData(Uri.parse("https://www.bizizaragoza.com/es/content/%C2%BFque-%C3%A9s"));
                startActivity(browserIntent);
            }
        });

        //al pulsar sobre el icono de gmail se abre gmail con un nuevo correo
        imgGmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGmail();
            }
        });

        imgTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "¡Funcionalidad operativa en la próxima actualización!",
                        Toast.LENGTH_SHORT).show();
            }
        });

        imgFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "¡Funcionalidad operativa en la próxima actualización!",
                        Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }

    //Permitimos elegir al usuario entre sus aplicaciones de mensajería
    public void openGmail(){

        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"bizizaragozaapp@gmail.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, "Asunto del mensaje");
        i.putExtra(Intent.EXTRA_TEXT   , "Escriba aqueí el motivo de su mensaje...");
        try {
            //startActivity(Intent.createChooser(i, "Mandar mensaje..."));
            startActivity(i);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getContext(), "No existe ningún cliente de mensajería instalado...",
                    Toast.LENGTH_SHORT).show();
        }

    }

}
