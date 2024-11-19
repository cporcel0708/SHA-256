package org.example;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class AlgoritmoSHA256 {
    // 3. INICIALIZACION DE VARIABLES.
    // Variables de hash iniciales
    private static final int[] H = {
            0x6a09e667, 0xbb67ae85, 0x3c6ef372, 0xa54ff53a,
            0x510e527f, 0x9b05688c, 0x1f83d9ab, 0x5be0cd19
    };

    // Constantes k
    private static final int[] K = {
            0x428a2f98, 0x71374491, 0xb5c0fbcf, 0xe9b5dba5, 0x3956c25b,
            0x59f111f1, 0x923f82a4, 0xab1c5ed5, 0xd807aa98, 0x12835b01,
            0x243185be, 0x550c7dc3, 0x72be5d74, 0x80deb1fe, 0x9bdc06a7,
            0xc19bf174, 0xe49b69c1, 0xefbe4786, 0x0fc19dc6, 0x240ca1cc,
            0x2de92c6f, 0x4a7484aa, 0x5cb0a9dc, 0x76f988da, 0x983e5152,
            0xa831c66d, 0xb00327c8, 0xbf597fc7, 0xc6e00bf3, 0xd5a79147,
            0x06ca6351, 0x14292967, 0x27b70a85, 0x2e1b2138, 0x4d2c6dfc,
            0x53380d13, 0x650a7354, 0x766a0abb, 0x81c2c92e, 0x92722c85,
            0xa2bfe8a1, 0xa81a664b, 0xc24b8b70, 0xc76c51a3, 0xd192e819,
            0xd6990624, 0xf40e3585, 0x106aa070, 0x19a4c116, 0x1e376c08,
            0x2748774c, 0x34b0bcb5, 0x391c0cb3, 0x4ed8aa4a, 0x5b9cca4f,
            0x682e6ff3, 0x748f82ee, 0x78a5636f, 0x84c87814, 0x8cc70208,
            0x90befffa, 0xa4506ceb, 0xbef9a3f7, 0xc67178f2
    };

    public static String hash(String message) {
        byte[] messageBytes = message.getBytes();
        int originalLength = messageBytes.length * 8; // longitud en bits

        // 1.ETAPA DE RELLENO
        messageBytes = padMessage(messageBytes);

        // Variables de hash
        int[] hashVars = H.clone();

        // 2. DIVISION EN BLOQUES
        // Procesar en bloques de 512 bits
        int numBlocks = messageBytes.length / 64;
        for (int i = 0; i < numBlocks; i++) {
        // 4. LAZO PRINCIPAL
                int[] w = new int[64];
                int offset = i * 64;

                // Dividir en 16 palabras de 32 bits (w0 ... w15)
                for (int j = 0; j < 16; j++) {
                        w[j] = ByteBuffer.wrap(Arrays.copyOfRange(messageBytes, offset + j * 4, offset + (j + 1) * 4))
                                .order(ByteOrder.BIG_ENDIAN).getInt();
                }

                // Extender a 64 palabras (w16 ... w63)
                for (int j = 16; j < 64; j++) {
                        int s0 = Integer.rotateRight(w[j - 15], 7) ^ Integer.rotateRight(w[j - 15], 18) ^ (w[j - 15] >>> 3);
                        int s1 = Integer.rotateRight(w[j - 2], 17) ^ Integer.rotateRight(w[j - 2], 19) ^ (w[j - 2] >>> 10);
                        w[j] = w[j - 16] + s0 + w[j - 7] + s1;
                }

                // Inicializar variables de trabajo
                int a = hashVars[0];
                int b = hashVars[1];
                int c = hashVars[2];
                int d = hashVars[3];
                int e = hashVars[4];
                int f = hashVars[5];
                int g = hashVars[6];
                int h = hashVars[7];

                // Función de compresión
                for (int j = 0; j < 64; j++) {
                        int S1 = Integer.rotateRight(e, 6) ^ Integer.rotateRight(e, 11) ^ Integer.rotateRight(e, 25);
                        int ch = (e & f) ^ (~e & g);
                        int temp1 = h + S1 + ch + K[j] + w[j];
                        int S0 = Integer.rotateRight(a, 2) ^ Integer.rotateRight(a, 13) ^ Integer.rotateRight(a, 22);
                        int maj = (a & b) ^ (a & c) ^ (b & c);
                        int temp2 = S0 + maj;

                        h = g;
                        g = f;
                        f = e;
                        e = d + temp1;
                        d = c;
                        c = b;
                        b = a;
                        a = temp1 + temp2;
                }

                // Actualizar valores hash
                hashVars[0] += a;
                hashVars[1] += b;
                hashVars[2] += c;
                hashVars[3] += d;
                hashVars[4] += e;
                hashVars[5] += f;
                hashVars[6] += g;
                hashVars[7] += h;
        }

        // 5.CONCATENCIÓN Y FORMATO
        // Concatenar el hash final
        StringBuilder hash = new StringBuilder();
        for (int hVar : hashVars) {
            hash.append(String.format("%08x", hVar));
        }
        return hash.toString();
    }

    private static byte[] padMessage(byte[] messageBytes) {
        int originalLength = messageBytes.length * 8;
        int padLength = 448 - (originalLength % 512);
        if (padLength < 0) {
            padLength += 512;
        }
        int totalLength = originalLength + padLength + 64;

        ByteBuffer buffer = ByteBuffer.allocate(totalLength / 8).order(ByteOrder.BIG_ENDIAN);
        buffer.put(messageBytes);
        buffer.put((byte) 0x80); // Añadir bit '1'
        buffer.put(new byte[(padLength / 8) - 1]); // Añadir ceros
        buffer.putLong(originalLength); // Añadir longitud original en bits

        return buffer.array();
    }

    public static void main(String[] args) {
        String message = "Modelos y Simulación";
        String hash = hash(message);
        System.out.println("Frase inicial: " + message);
        System.out.println("Hash SHA-256: " + hash);
    }
}
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