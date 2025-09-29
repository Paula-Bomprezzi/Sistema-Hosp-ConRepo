package Repositorio;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@SuperBuilder

public class InMemoryRepository <T> {

    //Atributos: Hasmap como BD e id autoincremental
    protected Map<Long, T> datos = new HashMap<>();
    protected AtomicLong id = new AtomicLong();

    //Métodos: Save (guardar datos)


    //SAVE
    public T save(T objeto) {
        //Tengo que asignarle un id a un objeto que ya existe --> debo usar reflection)
        try {
            Class<?> ClassObj = objeto.getClass();
            Method metodoID = ClassObj.getMethod("setId", Long.class);
            metodoID.invoke(objeto, id.getAndIncrement());

        } catch (Exception e) {
            e.printStackTrace();
        }
        ;
        datos.put(id.get(), objeto);
        System.out.println("Se guardó un objeto del tipo " + objeto.getClass().getName() + " Con id = " + id.get());
        return objeto; //devuelve el objeto ya con el id modificado
    }

    ;


    //BAJA
    public Optional<T> baja(Long id) {
        if (!datos.containsKey(id)) {
            System.out.println("No se encontro el objeto con id = " + id);
            return Optional.empty();
        }

        T objetoEliminado = datos.remove(id);
        if (objetoEliminado != null) {
            System.out.println("Dado de baja con id = " + id);
        } else {
            System.out.println("Objeto dado de baja con valor null");
        }
        return Optional.ofNullable(objetoEliminado);
    }

    //MODIFICACIÓN
    public Optional<T> modificacion(Long id, T modificar) {
        if (!datos.containsKey(id)) {
            System.out.println("No se encontró el objeto con id = " + id);
            return Optional.empty();
        }

        try {
            Method setIdMethod = modificar.getClass().getMethod("setId", Long.class);
            setIdMethod.invoke(modificar, id);
            datos.put(id, modificar);
            return Optional.of(modificar);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }




        public Optional<T> findById(Long id) {
            return Optional.ofNullable(datos.get(id));
        }



        public List<T> findAll() {
            return new ArrayList<>(datos.values());
        }

        public List<T> genericFindByField(String fieldName, Object value) {
            List<T> results = new ArrayList<>();
            try {
                for (T entity : datos.values()) {
                    Method getFieldMethod = entity.getClass().getMethod("get" + capitalize(fieldName));
                    Object fieldValue = getFieldMethod.invoke(entity);
                    if (fieldValue != null && fieldValue.equals(value)) {
                        results.add(entity);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return results;
        }

        private String capitalize(String str) {
            if (str == null || str.isEmpty()) {
                return str;
            }
            return str.substring(0, 1).toUpperCase() + str.substring(1);
        }

    }







