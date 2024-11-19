/*
Explicación del Algoritmo
1.Etapa de Relleno:
    Se convierte el mensaje en bytes.
    Se añade un bit 1 y luego ceros hasta que la longitud del mensaje (en bits) sea congruente a 448 mod 512.
    Se añade la longitud del mensaje original en bits, representado en 64 bits.

2.División en Bloques:
    Se divide el mensaje en bloques de 512 bits (64 bytes).

3.Inicialización de Variables:
    Se inicializan los valores hash H y las constantes K.

4.Lazo Principal:
    * Para cada bloque:
        Se divide el bloque en 16 palabras de 32 bits.
        Se extienden las 16 palabras a 64 palabras usando ciertas operaciones de bit.
        Se inicializan las variables de trabajo a, b, c, d, e, f, g, h con los valores actuales del hash.
        Se aplica la función de compresión a las 64 palabras.
        Se actualizan los valores hash con las variables de trabajo.

5.Concatenación y Formato:
        Se concatenan los valores finales de hash y se convierten a hexadecimal para obtener el hash final.

*/
