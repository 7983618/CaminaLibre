package com.example.caminalibre;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.example.caminalibre.modelo.Ruta;

import java.util.ArrayList;

public class ComparadorRutas extends DiffUtil.Callback {
    private final ArrayList<Ruta> viejasrutas;
    private final ArrayList<Ruta> nuevasrutas;

    public ComparadorRutas(ArrayList<Ruta> rutas, ArrayList<Ruta> nuevasrutas) {
        this.viejasrutas = rutas;
        this.nuevasrutas = nuevasrutas;
    }

    @Override
    public int getOldListSize() {
        return viejasrutas.size();
    }

    @Override
    public int getNewListSize() {
        return nuevasrutas.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        // Suponiendo que el nombre de la ruta es único. 
        // Si hubiera un ID, sería mejor usarlo aquí.
        String viejoNombre = viejasrutas.get(oldItemPosition).getNombreRuta();
        String nuevoNombre = nuevasrutas.get(newItemPosition).getNombreRuta();
        
        if (viejoNombre == null || nuevoNombre == null) return false;
        return viejoNombre.equals(nuevoNombre);
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Ruta vieja = viejasrutas.get(oldItemPosition);
        Ruta nueva = nuevasrutas.get(newItemPosition);

        // Comparación manual de todos los campos relevantes de la clase Ruta
        if (Float.compare(vieja.getDificultad(), nueva.getDificultad()) != 0) return false;
        if (Double.compare(vieja.getDistancia(), nueva.getDistancia()) != 0) return false;
        if (vieja.isFavorita() != nueva.isFavorita()) return false;
        if (Double.compare(vieja.getLatitud(), nueva.getLatitud()) != 0) return false;
        if (Double.compare(vieja.getLongitud(), nueva.getLongitud()) != 0) return false;
        
        if (vieja.getNombreRuta() != null ? !vieja.getNombreRuta().equals(nueva.getNombreRuta()) : nueva.getNombreRuta() != null)
            return false;
        if (vieja.getLocalizacion() != null ? !vieja.getLocalizacion().equals(nueva.getLocalizacion()) : nueva.getLocalizacion() != null)
            return false;
        if (vieja.getTipo() != nueva.getTipo()) return false;
        if (vieja.getDescripcion() != null ? !vieja.getDescripcion().equals(nueva.getDescripcion()) : nueva.getDescripcion() != null)
            return false;
            
        return vieja.getNotas() != null ? vieja.getNotas().equals(nueva.getNotas()) : nueva.getNotas() == null;
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
