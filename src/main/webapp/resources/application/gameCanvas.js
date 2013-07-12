/* 
 * Motor de dibujo del canvas
 * Esteban
 */

window.addEventListener("load", function() {
    var canvas = document.getElementById('gameCanvas'),
            context = canvas.getContext('2d');
    var MousePosition = {x: 0, y: 0};

    // resize the canvas to fill browser window dynamically
    window.addEventListener('resize', resizeCanvas, false);
    canvas.addEventListener('mousemove', MoveMouse, false);

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

    function getMousePos(canvas, evt) {
        var rect = canvas.getBoundingClientRect();
        return {
            x: evt.clientX - rect.left,
            y: evt.clientY - rect.top
        };
    }

    function MoveMouse(evt) {
        MousePosition = getMousePos(canvas, evt);
        paint();
    }

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

        context.clearRect(0, 0, canvas.width, canvas.height);

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

        var DibujaLinea = function(color, posx, posy, width, height, mp) {
            context.fillStyle = colores[color];
            context.fillRect(posx, posy, width, height);
            if (color === 0 &&
                    posx < mp.x && mp.x < posx + width &&
                    posy < mp.y && mp.y < posy + height) {
                // Si el cursor está en la zona entonces se marca...
                context.fillStyle = "black";
                context.strokeStyle = "black";
                context.fillRect(posx, posy, width, height);
            }
        };

        var i = 0;
        context.fillStyle = "white";
        for (y = 0; y < filas; y++) {
            for (x = 0; x < columnas; x++) {
                var valor = lineData[i];
                if (lineData !== null && lineData.length > i) {
                    valor[0] = parseInt(valor[0]);
                    valor[1] = parseInt(valor[1]);
                    valor[2] = parseInt(valor[2]);
                    valor[3] = parseInt(valor[3]);
                } else {
                    valor = [0,0,0,0];
                }
                // Arriba:
                DibujaLinea(valor[0], PosX(x) + anchoBorde, PosY(y), anchoCuadro, altoBorde, MousePosition);
                // Derecha
                DibujaLinea(valor[1], PosX(x + 1), PosY(y) + altoBorde, anchoBorde, altoCuadro, MousePosition);
                // Abajo
                DibujaLinea(valor[2], PosX(x) + anchoBorde, PosY(y + 1), anchoCuadro, altoBorde, MousePosition);
                // Izquierda
                DibujaLinea(valor[3], PosX(x), PosY(y) + altoBorde, anchoBorde, altoCuadro, MousePosition);
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