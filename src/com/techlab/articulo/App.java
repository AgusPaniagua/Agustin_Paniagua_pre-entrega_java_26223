package com.techlab.articulo;

import java.util.ArrayList;
import java.util.Scanner;

import com.techlab.articulo.model.Articulo;
import com.techlab.articulo.model.ArticuloAlimenticio;
import com.techlab.articulo.model.ArticuloElectronico;
import com.techlab.articulo.model.Categoria;

public class App {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        ArrayList<Articulo> articulos = new ArrayList<>();
        ArrayList<Categoria> categorias = new ArrayList<>();
        precargarCategorias(categorias);
        int opcion;

        do {
            System.out.println("===========================");
            System.out.println("|   SISTEMA DE ARTICULOS  |");
            System.out.println("===========================");
            System.out.println("1 - Ingresar articulo");
            System.out.println("2 - Listar articulos");
            System.out.println("3 - Consultar un articulo");
            System.out.println("4 - Modificar un articulo");
            System.out.println("5 - Eliminar un articulo");
            System.out.println("6 - Listar categorias");
            System.out.println("0 - Salir");
            System.out.println("===========================");
            opcion=leerEnteroNoNegativo(scanner, "Ingrese una opcion:");
            switch (opcion) {
                case 1:
                    ingresarArticulo(scanner, articulos, categorias);
                    break;
                case 2:
                    listarArticulos(articulos);
                    break;
                case 3:
                    consultarArticulo(scanner,articulos);
                    break;
                case 4:
                    modificarArticulo(scanner, articulos, categorias);
                    break;
                case 5:
                    eliminarArticulo(scanner, articulos);
                    break;
                case 6:
                    listarCategorias(categorias);
                    break;
                case 0:
                    System.out.println("gracias vuelva pronto");
                    break;
                default:
                    System.out.println("Opcion invalida");
                    break;
            }
        } while (opcion!=0);
        scanner.close();
    }

    public static void precargarCategorias(ArrayList<Categoria> categorias){
        categorias.add(new Categoria(1, "Electronica", "Productos tecnologicos y electronicos"));
        categorias.add(new Categoria(2, "Perifericos", "Accesorios para computadora"));
        categorias.add(new Categoria(3, "Alimentos", "Productos alimenticios"));
        categorias.add(new Categoria(4, "Limpieza", "Articulos de limpieza del hogar"));
    }

    //opcion 1
    public static void ingresarArticulo(Scanner scanner, ArrayList<Articulo> articulos, ArrayList<Categoria> categorias){
        int tipo;
        int codigo;
        String nombre;
        double precio;
        int garantia;
        int dias;
        do {
            System.out.println("Ingrese un articulo: ");
            System.out.println("--- INGRESAR ARTICULO ---");
            System.out.println("1 - Articulo electronico");
            System.out.println("2 - Articulo alimenticio");
            tipo = leerEnteroNoNegativo(scanner, "Seleccione el tipo de articulo: ");
            if(tipo !=1 && tipo !=2){
                System.out.println("Opcion invalida. Debe ingresar 1 o 2");
            }
        } while (tipo !=1 && tipo !=2);

        //verificacion del codigo de articulo
        codigo = leerEnteroNoNegativo(scanner, "Ingrese el codigo del articulo");
        for(Articulo a : articulos){
            if(codigo == a.getCodigo()){
                System.out.println("El código ya existe.");
                return ;
            }
        }

        //verificacion del nombre del articulo
         nombre=leerTextoNoVacio(scanner, "Ingrese el nombre del articulo");

        //verificacion del precio del articulo
        precio=leerDoubleNoNegativo(scanner, "Ingrese el precio del articulo");
              
        //categorias disponibles
        System.out.println("categorias disponibles:");
        for(Categoria c : categorias){
            System.out.println(c.getCodigo() + " - " + c.getNombre());
        }
        Categoria categoriaSeleccionada = pedirCategoriaExistente(scanner, categorias);
        
        //creacion del articulo

        Articulo articulo =null;
        if(tipo==1){
            garantia=leerEnteroNoNegativo(scanner, "Ingresa la garantia en meses del articulo");
            articulo = new ArticuloElectronico(codigo, nombre, precio, categoriaSeleccionada, garantia);
        }else {
            dias=leerEnteroNoNegativo(scanner, "Ingrese los dias para que el articulo venza");
            articulo = new ArticuloAlimenticio(codigo, nombre, precio, categoriaSeleccionada, dias);
        }
        articulos.add(articulo);
        System.out.println("El articulo se ha ingresado correctamente: " + articulo);
    }

    //opcion 2
    public static void listarArticulos(ArrayList<Articulo> articulos){
        if(articulos.isEmpty()){
            System.out.println("No hay articulos cargados");
        }else{
            for(Articulo a : articulos){
                System.out.println(a.toString());
            }
        }
    }

    //opcion 3
    public static void consultarArticulo(Scanner scanner, ArrayList<Articulo> articulos){
        int codigoABuscar;
        if(articulos.isEmpty()){
            System.out.println("No hay articulos cargados");
            return;
        }
        codigoABuscar = leerEnteroNoNegativo(scanner, "Ingrese el codigo del articulo a buscar");
        Articulo articulo = buscarArticuloPorCodigo(articulos, codigoABuscar);
        if(articulo==null){
            System.out.println("El articulo no existe");
            return;
        }
        
        System.out.println("Articulo encontrado:" + articulo.toString());
        System.out.println("Detalle especifico: "+ articulo.getDetalleEspecifico());
    }

    //opcion 4
    public static void modificarArticulo(Scanner scanner, ArrayList<Articulo> articulos, ArrayList<Categoria> categorias){
        if(articulos.isEmpty()){
            System.out.println("No hay articulos cargados");
            return;
        }
        int codigo=leerEnteroNoNegativo(scanner, "Ingrese el codigo del articulo a modificar");
        Articulo articulo = buscarArticuloPorCodigo(articulos, codigo);
        if(articulo==null){
            System.out.println("El articulo no existe");
            return;
        }
        String nuevoNombre = leerTextoNoVacio(scanner, "Ingrese el nuevo nombre");
        double nuevoPrecio = leerDoubleNoNegativo(scanner, "Ingrese el nuevo precio");
        listarCategorias(categorias);
        Categoria categoria = pedirCategoriaExistente(scanner, categorias);
        articulo.setNombre(nuevoNombre);
        articulo.setPrecio(nuevoPrecio);
        articulo.setCategoria(categoria);
        if(articulo instanceof ArticuloElectronico){
            int nuevaGarantia = leerEnteroNoNegativo(scanner, "Ingrese la nueva garantia");
            ArticuloElectronico electronico = (ArticuloElectronico) articulo;
            electronico.setGarantiaMeses(nuevaGarantia);
        }else if(articulo instanceof ArticuloAlimenticio){
            int nuevoDias = leerEnteroNoNegativo(scanner, "Ingrese la nueva cantidad de dias de vencimiento");
            ArticuloAlimenticio alimenticio = (ArticuloAlimenticio) articulo;
            alimenticio.setDiasParaVencimiento(nuevoDias);
        }
        System.out.println("Articulo modificado correctamente: "+ articulo);
    }

    //opcion 5
    public static void eliminarArticulo(Scanner scanner, ArrayList<Articulo> articulos){
        if(articulos.isEmpty()){
            System.out.println("No hay articulos cargados");
            return;
        }
        int codigo=leerEnteroNoNegativo(scanner, "Ingrese el codigo del articulo a eliminar");
        Articulo articulo = buscarArticuloPorCodigo(articulos, codigo);
        if(articulo==null){
            System.out.println("El articulo no exite");
            return;
        }

        articulos.remove(articulo);
        System.out.println("Articulo eliminado correctamente: "+articulo.toString());
        
    }

    //opcion 6
    public static void listarCategorias(ArrayList<Categoria> categorias){
        for(Categoria c : categorias){
            System.out.println(c.toString());
        }
    }

    //metodos auxiliares
    public static Articulo buscarArticuloPorCodigo(ArrayList<Articulo> articulos, int codigo){
        for(Articulo a : articulos){
            if(codigo==a.getCodigo()){
                return a;
            }
        }
        return null;
    }

    public static Categoria buscarCategoriaPorCodigo(ArrayList<Categoria> categorias, int codigo){
        for(Categoria c : categorias){
            if(codigo==c.getCodigo()){
                return c;
            }
        }
        return null;
    }

    public static Categoria pedirCategoriaExistente(Scanner scanner, ArrayList<Categoria> categorias){
        while (true) {
        int categoriaPedida = leerEnteroNoNegativo(scanner, "Escriba el codigo de la categoria a buscar");
        Categoria categoria = buscarCategoriaPorCodigo(categorias, categoriaPedida);
        if(categoria!=null){
            return categoria;
        }
         System.out.println("La categoria buscada no existe");
        }
    }

    public static int leerEntero(Scanner scanner, String mensaje){
        while (true) {
            System.out.println(mensaje);
            try{
                return Integer.parseInt(scanner.nextLine().trim());
            }catch(NumberFormatException e){
                System.out.println("Debe ingresar un numero entero valido");
            }
        }
    }

    public static int leerEnteroNoNegativo(Scanner scanner,String mensaje){
        int numero;
        do {
            numero=leerEntero(scanner, mensaje);
            if(numero<0){
            System.out.println("El numero no puede ser negativo, ingrese de nuevo");
            }
        } while (numero<0);
        return numero;
    }

    public static double leerDoubleNoNegativo(Scanner scanner, String mensaje){
        while (true) {
            System.out.println(mensaje);
            try{
                double numero = Double.parseDouble(scanner.nextLine().trim());
                if(Double.isNaN(numero)||Double.isInfinite(numero)){
                    System.out.println("Debe ingresar un numero finito valido");
                }else if(numero<0){
                    System.out.println("El numero no puede ser negativo");
                }else{
                    return numero;
                }
            }catch(NumberFormatException e){
                System.out.println("Debe ingresar un numero decimal valido");
            }
        }
    }

    public static String leerTextoNoVacio(Scanner scanner, String mensaje){
        while (true) {
            System.out.println(mensaje);
            String texto = scanner.nextLine().trim();
            if(texto.isEmpty()){
                System.out.println("El texto no puede estar vacio");
            }else{
                return texto;
            }
        }
    }

}