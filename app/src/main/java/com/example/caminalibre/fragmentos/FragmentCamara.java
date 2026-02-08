package com.example.caminalibre.fragmentos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.caminalibre.Database.CreadorDB;
import com.example.caminalibre.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;

public class FragmentCamara extends Fragment {

    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private ImageCapture imageCapture;
    private Preview preview;
    private ProcessCameraProvider cameraProvider;
    private PreviewView viewFinder;
    private long idRutaActual;

    public FragmentCamara() {
    }

    public static FragmentCamara newInstance(long idRuta) {
        FragmentCamara fragment = new FragmentCamara();
        Bundle args = new Bundle();
        args.putLong("idRuta", idRuta);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idRutaActual = getArguments().getLong("idRuta");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_camara, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewFinder = view.findViewById(R.id.viewFinder);
        FloatingActionButton btnCapture = view.findViewById(R.id.image_capture_button);

        btnCapture.setOnClickListener(v -> tomarFoto());

        configurarCameraX();
    }

    private void configurarCameraX() {
        /**
         * Aquí le dices a Android: "Oye, voy a necesitar la cámara en algún momento,
         * ve preparándola". Es un proceso asíncrono (tarda un poco).
         * */
        cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext());
        cameraProviderFuture.addListener(() -> {
            try {
                /**
                 * Recogemos el objeto ProcessCameraProvider que nos ha devuelto CameraX
                 * */
                cameraProvider = cameraProviderFuture.get();
                enlazarCasosDeUso();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Error al iniciar cámara", Toast.LENGTH_SHORT).show();
            }
        }, ContextCompat.getMainExecutor(requireContext()));
    }

    private void enlazarCasosDeUso() {
        // 1. Configurar el Preview
        preview = new Preview.Builder().build();
        /// Conectas ese objeto con el viewFinder del XML.
        ///Aquí es cuando empiezas a ver tu cara o el paisaje en la pantalla.
        preview.setSurfaceProvider(viewFinder.getSurfaceProvider());

        // 2. Configurar la captura de imagen
        imageCapture = new ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY) // para tomar sin retardo
                .build();

        // 3. Seleccionar la cámara trasera
        CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;

        try {
            // 4. Vincular al ciclo de vida del Fragmento
            // limpiamos los casos de uso anteriores
            cameraProvider.unbindAll();

            /***
             *  La línea maestra. Aquí conectas todo (Vista Previa y Capturador)
             *  al ciclo de vida del fragmento. La cámara se enciende ahora y se apagará
             *  sola si sales del fragmento.
             */
            cameraProvider.bindToLifecycle(getViewLifecycleOwner(), cameraSelector, preview, imageCapture);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void tomarFoto() {
        if (imageCapture == null) return;

        // Crear el archivo de destino
        String nombre = "ruta_" + idRutaActual + "_" + System.currentTimeMillis() + ".jpg";
        File archivo = new File(requireContext().getFilesDir(), nombre);

        ImageCapture.OutputFileOptions outputOptions = new ImageCapture.OutputFileOptions.Builder(archivo).build();

        imageCapture.takePicture(outputOptions, ContextCompat.getMainExecutor(requireContext()),
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                        String pathFinal = archivo.getAbsolutePath();

                        // Guardar en la base de datos a través del DAO
                        CreadorDB.ejecutarhilo.execute(() -> {
                            CreadorDB.getDatabase(getContext()).getDAO().updateFoto(idRutaActual, pathFinal);

                            requireActivity().runOnUiThread(() -> {
                                Toast.makeText(getContext(), "Foto guardada con éxito", Toast.LENGTH_SHORT).show();
                                getParentFragmentManager().popBackStack();
                            });
                        });
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        exception.printStackTrace();
                        Toast.makeText(getContext(), "Error al guardar foto", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
