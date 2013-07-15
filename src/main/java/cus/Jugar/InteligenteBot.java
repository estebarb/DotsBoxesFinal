/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cus.Jugar;

import entities.Juegos;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import modelo.MJuego;

/**
 *
 * @author esteban
 */
class InteligenteBot implements IMachineBot {

    @Override
    public boolean Jugar(Juegos j) {
        //return (new RandomBot()).Jugar(j);

        MJuego juego = new MJuego();
        byte[] data = j.getTablero();

        int largo = data.length / 5;
        byte[] newdata = new byte[largo];
        for (int i = 0; i < largo; i += 5) {
            newdata[i] = 0;
            newdata[i] += data[i] == 0 ? 0 : 1;
            newdata[i] += data[i + 1] == 0 ? 0 : 2;
            newdata[i] += data[i + 2] == 0 ? 0 : 4;
            newdata[i] += data[i + 3] == 0 ? 0 : 8;
        }

        int filas = j.getFilas();
        int columnas = j.getColumnas();

        List<Integer> grado0 = existeGrado(0, newdata);
        List<Integer> grado1 = existeGrado(1, newdata);
        List<Integer> grado2 = existeGrado(2, newdata);
        List<Integer> grado3 = existeGrado(3, newdata);
        List<Integer> grado4 = existeGrado(4, newdata);
        int totalCuadros = filas * columnas;
        List<List<Integer>> cadenas = Cadenas(newdata, columnas, filas);
        List<Integer> lst;
        int box;

        // Paso 1: Se completan todas las cadenas de largo 1:
        for (List<Integer> tmp : cadenas) {
            if (tmp.size() == 1) {
                juego.Jugar(j, tmp.get(0), getFreeLine(newdata[tmp.get(0)]));
                return true;
            }
        }
        // Paso 2: Si solamente hay un cuadro de 3
        if (grado3.size() == 1) {
            // Paso 2a: si la cadena es mayor a 2
            // o son los últimos dos cuadros
            // o hay líneas libres
            // entonces se completa el cuadrado
            lst = cadenas.get(0);
            if (lst.size() > 2
                    || (totalCuadros - grado4.size() == 2)
                    || grado0.size() + grado1.size() > 0) {
                juego.Jugar(j, lst.get(0), getFreeLine(newdata[lst.get(0)]));
                return true;
            } else {
                // Paso 2b: Sino jugamos al final de lo que hubiera
                // sido la cadena:
                juego.Jugar(j,
                        lst.get(lst.size() - 1),
                        getLineFinalCadena(data,
                        columnas,
                        filas,
                        lst.get(0)));
                return true;
            }
        }
        // Paso 3: Si solamente hay dos cuadros de grado 3:
        if (grado3.size() == 2) {
            // Paso 3a: Si ambos están en la misma cadena
            if (cadenas.size() == 1) {
                // Paso 3a1: Si la cadena mide más de 4, 
                // o son los últimos 4 cuadros
                // o hay cuadros libres
                lst = cadenas.get(0);
                if (lst.size() > 4
                        || (totalCuadros - grado4.size() == 4)
                        || grado0.size() + grado1.size() > 0) {
                    juego.Jugar(j,
                            grado3.get(0),
                            getFreeLine(newdata[grado3.get(0)]));
                    return true;
                } else {
                    // Paso 3a2:
                    // Sino se juega en la mitad de las cadenas.
                    lst = cadenas.get(0);
                    juego.Jugar(j, lst.get(1), getSecondLine(newdata[lst.get(0)], newdata[lst.get(1)]));
                    return true;
                }
            } else {
                // Se completa un cuadro en la menor cadena...
                lst = cadenas.get(0).size() < cadenas.get(1).size() ? cadenas.get(0) : cadenas.get(1);
                juego.Jugar(j, lst.get(0), getFreeLine(newdata[lst.get(0)]));
                return true;
            }
        }
        // Paso 4: si hay cuadros de 3 tomarlos en la cadena menor...
        if (grado3.size() > 0) {
            lst = null;
            int tam = totalCuadros;
            for (int i = 0; i < cadenas.size(); i++) {
                if (cadenas.get(i).size() < tam) {
                    lst = cadenas.get(i);
                }
            }
            juego.Jugar(j, lst.get(0), getFreeLine(newdata[lst.get(0)]));
            return true;
        }
        // Paso 5: Si hay líneas libres juegue allí.
        Random rnd = new Random();
        if (grado0.size() > 0) {
            box = grado1.get(rnd.nextInt(grado1.size()));
            juego.Jugar(j, box, getMostFreeLine(newdata, box, rnd));
            return true;
        }
        if (grado1.size() > 0) {
            box = grado1.get(rnd.nextInt(grado1.size()));
            juego.Jugar(j, box, getMostFreeLine(newdata, box, rnd));
            return true;
        }
        // Paso 6: Tome la precadena menos mala.
        int menosMalo = 0;
        int lineaMenosMala = 1;
        int largoMenosMalo = totalCuadros+10;
        for (int i = 0; i < grado2.size(); i++) {
            for (int linea = 0; linea < 4; linea++) {
                byte[] datacop = (byte[]) newdata.clone();
                datacop[i] += getLinea2Bits(linea);
                lst = getCadena(datacop, columnas, filas, i);
                if(lst.size() < largoMenosMalo){
                    menosMalo = i;
                    lineaMenosMala = linea;
                }
            }
        }
        juego.Jugar(j, menosMalo, lineaMenosMala);
        return true;
    }

    private int getLinea2Bits(int linea) {
        switch (linea) {
            case 0:
                return 1;
            case 1:
                return 2;
            case 2:
                return 4;
            case 3:
                return 8;
            default:
                return 0;
        }
    }

    private int getMostFreeLine(byte[] newdata, int box, Random rnd) {
        if (newdata[box] == 0) {
            return rnd.nextInt(4);
        }
        int salida = rnd.nextInt(4);
        int actual = getLine(newdata[box]);
        while (salida == actual) {
            salida = rnd.nextInt(4);
        }
        return salida;
    }

    private int getSecondLine(byte head, byte second) {
        int freeLine = getFreeLine(head);
        int secondUsed = second + (1 << ((freeLine + 2) % 4));
        return getFreeLine((byte) secondUsed);
    }

    private int getFreeLine(byte data) {
        switch (data) {
            case 15 - 1:
                return 0;
            case 15 - 2:
                return 1;
            case 15 - 4:
                return 2;
            case 15 - 8:
                return 3;
            default:
                return 0;
        }
    }

    private int getLine(byte data) {
        switch (data) {
            case 1:
                return 0;
            case 2:
                return 1;
            case 4:
                return 2;
            case 8:
                return 3;
            default:
                return 0;
        }
    }

    private int getGrado(byte[] data, int box) {
        byte gradoTable[] = {0, 1, 1, 2, 1, 2, 2, 3, 1, 2, 2, 3, 2, 3, 3, 4};
        return gradoTable[data[box]];
    }

    private int getDirecciones(byte[] data, int box, int columnas, int filas) {
        int salida = (data[box] ^ 0xff) & 0xf;
        int filaActual = box / columnas;
        int columnaActual = box % columnas;
        salida &= filaActual == 0 ? 14 : 0xf;
        salida &= filaActual == filas - 1 ? 11 : 0xf;
        salida &= columnaActual == 0 ? 0x7 : 0xf;
        salida &= columnaActual == columnas - 1 ? 13 : 0xf;
        return salida;
    }

    private List<Integer> existeGrado(int grado, byte[] data) {
        List<Integer> celdas = new ArrayList<>();
        for (int i = 0; i < data.length; i++) {
            if (getGrado(data, i) == grado) {
                celdas.add(i);
            }
        }
        return celdas;
    }

    private List<List<Integer>> Cadenas(byte[] data, int columnas, int filas) {
        List<List<Integer>> cadenas = new ArrayList<>();
        int numBoxes = columnas * filas;
        // Busca todas las cadenas
        for (int i = 0; i < numBoxes; i++) {
            if (getGrado(data, i) == 3) {
                cadenas.add(getCadena(data, columnas, filas, i));
            }
        }
        // Elimina las cadenas repetidas
        List<Integer> current, post;

        for (int i = 0; i < cadenas.size(); i++) {
            current = cadenas.get(i);
            for (int a = i + 1; a < cadenas.size(); a++) {
                post = cadenas.get(a);
                if (current.get(0).compareTo(post.get(post.size() - 1)) == 0) {
                    cadenas.remove(a);
                    a--;
                }
            }
        }
        return cadenas;
    }

    private List<Integer> getCadena(byte[] data, int columnas, int filas, int box) {
        List<Integer> cadena = new ArrayList<>();
        if (getGrado(data, box) == 3) {
            cadena.add(box);
            byte[] newdata = (byte[]) data.clone();

            // Explora caminos
            if ((getDirecciones(data, box, columnas, filas) & 1) != 0) {
                newdata[box] = 15;
                newdata[box - columnas] += 4;
                cadena.addAll(getCadena(newdata, columnas, filas, box - columnas));
            }

            if ((getDirecciones(data, box, columnas, filas) & 2) != 0) {
                newdata[box] = 15;
                newdata[box - columnas] += 8;
                cadena.addAll(getCadena(newdata, columnas, filas, box + 1));
            }

            if ((getDirecciones(data, box, columnas, filas) & 4) != 0) {
                newdata[box] = 15;
                newdata[box - columnas] += 1;
                cadena.addAll(getCadena(newdata, columnas, filas, box + columnas));
            }

            if ((getDirecciones(data, box, columnas, filas) & 8) != 0) {
                newdata[box] = 15;
                newdata[box - columnas] += 2;
                cadena.addAll(getCadena(newdata, columnas, filas, box - 1));
            }
        }
        return cadena;
    }

    private Integer getLineFinalCadena(byte[] data, int columnas, int filas, int box) {
        Integer last;
        if (getGrado(data, box) == 3) {
            byte[] newdata = (byte[]) data.clone();

            // Explora caminos
            if ((getDirecciones(data, box, columnas, filas) & 1) != 0) {
                newdata[box] = 15;
                newdata[box - columnas] += 4;
                last = getLineFinalCadena(newdata, columnas, filas, box - columnas);
                if (last == null) {
                    return 1;
                } else {
                    return last;
                }
            }

            if ((getDirecciones(data, box, columnas, filas) & 2) != 0) {
                newdata[box] = 15;
                newdata[box - columnas] += 8;
                last = getLineFinalCadena(newdata, columnas, filas, box + 1);
                if (last == null) {
                    return 2;
                } else {
                    return last;
                }
            }

            if ((getDirecciones(data, box, columnas, filas) & 4) != 0) {
                newdata[box] = 15;
                newdata[box - columnas] += 1;
                last = getLineFinalCadena(newdata, columnas, filas, box + columnas);
                if (last == null) {
                    return 4;
                } else {
                    return last;
                }
            }

            if ((getDirecciones(data, box, columnas, filas) & 8) != 0) {
                newdata[box] = 15;
                newdata[box - columnas] += 2;
                last = getLineFinalCadena(newdata, columnas, filas, box - 1);
                if (last == null) {
                    return 8;
                } else {
                    return last;
                }
            }


        }
        return null;
    }
}
