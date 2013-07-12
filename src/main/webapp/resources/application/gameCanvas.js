/* 
 * Motor de dibujo del canvas
 * Esteban
 */

window.addEventListener("load", function() {
    var canvas = document.getElementById('gameCanvas'),
            context = canvas.getContext('2d');

    // resize the canvas to fill browser window dynamically
    window.addEventListener('resize', resizeCanvas, false);

    function resizeCanvas() {
        var ancho = document.getElementById("gameDiv").getBoundingClientRect().width;
        var alto = window.innerHeight - document.getElementById("gameDiv").getBoundingClientRect().top;
        canvas.width = ancho;
        canvas.height = Math.min(ancho, alto * 0.7);

        /**
         * Your drawings need to be inside this function otherwise they will be reset when 
         * you resize the browser window and the canvas goes will be cleared.
         */
        paint();
    }
    resizeCanvas();

    function paint() {
        var ancho = document.getElementById("gameDiv").getBoundingClientRect().width;
        var alto = window.innerHeight - document.getElementById("gameDiv").getBoundingClientRect().top;
        alto = Math.min(ancho, alto * 0.7);

        // Tamaño de la cuadrícula
        var sizeData = document.getElementById("sizeData").value.split(",");
        var columnas = parseInt(sizeData[0]);
        var filas = parseInt(sizeData[1]);

        // Más cálculo de dimensiones:
        var margen = alto * 0.1;
        var anchoReal = ancho * 0.8;
        var altoReal = alto * 0.8;
        var anchoCuadro = anchoReal * 0.9 / columnas;
        var altoCuadro = altoReal * 0.9 / filas;
        var anchoBorde = anchoReal * 0.1 / (columnas + 1);
        var altoBorde = altoReal * 0.1 / (filas + 1);

        var PosX = function(x) {
            return margen + x * (anchoCuadro + anchoBorde);
        };

        var PosY = function(y) {
            return margen + y * (altoCuadro + altoBorde);
        };

        // Primero dibuja los puntos:
        context.fillStyle = "black";
        for (y = 0; y <= filas; y++) {
            for (x = 0; x <= columnas; x++) {
                context.fillRect(PosX(x), PosY(y), anchoBorde, altoBorde);
            }
        }

        // Azul, Rojo, Verde, celeste, morado, cafe, verde claro, amarillo
        var colores = ["#FFFFFF", "#1448CC", "#CC3614", "#14CC45", "#00CDFF",
            "#B307FF", "#A36953", "#45FFB8", "#FFC100"];

        // Ahora dibuja las líneas que ya fueron puestas:
        var lineData = $.parseJSON(document.getElementById("lineData").value);
        var i = 0;
        context.fillStyle = "white";
        for (y = 0; y <= filas; y++) {
            for (x = 0; x <= columnas; x++) {
                if (lineData !== null && lineData.length > i) {
                    var valor = lineData[i];
                    valor[0] = parseInt(valor[0]);
                    valor[1] = parseInt(valor[1]);
                    valor[2] = parseInt(valor[2]);
                    valor[3] = parseInt(valor[3]);
                    // Arriba:
                    context.fillStyle = colores[valor[0]];
                    context.fillRect(PosX(x) + anchoBorde, PosY(y), anchoCuadro, altoBorde);
                    // Derecha
                    context.fillStyle = colores[valor[1]];
                    context.fillRect(PosX(x+1), PosY(y) + altoBorde, anchoBorde, altoCuadro);
                    // Abajo
                    context.fillStyle = colores[valor[2]];
                    context.fillRect(PosX(x) + anchoBorde, PosY(y+1), anchoCuadro, altoBorde);
                    // Izquierda
                    context.fillStyle = colores[valor[3]];
                    context.fillRect(PosX(x), PosY(y) + altoBorde, anchoBorde, altoCuadro);
                }
                i++;
            }
        }

        // Y dibuja las cajas rellenas
        var boxData = document.getElementById("boxData").value.split(",");
        i = 0;
        context.fillStyle = "white";
        for (y = 0; y <= filas; y++) {
            for (x = 0; x <= columnas; x++) {
                if (boxData !== null && boxData.length > i) {
                    context.fillStyle = colores[parseInt(boxData[i])];
                    context.fillRect(PosX(x) + anchoBorde, PosY(y) + altoBorde, anchoCuadro, altoCuadro);
                }
                i++;
            }
        }
    } // fin Paint
});