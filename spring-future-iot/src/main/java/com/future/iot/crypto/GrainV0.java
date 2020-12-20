package com.future.iot.crypto;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.binary.Base64;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GrainV0 {

    private Byte[] lfsr = new Byte[80];
    private Byte[] nfsr = new Byte[80];
    private final Byte[] temp_lfsr;
    private final Byte[] temp_nfsr;
    private final Byte[] filter;


    public GrainV0(String iv, String key) {
        this.temp_lfsr = new Byte[160];
        this.temp_nfsr = new Byte[160];
        this.filter = new Byte[160];
        init(iv, key);
    }


    private void init(String iv, String key) {
        // konversi string iv ke string biner
        String iv_result = this.stringToBinary(iv);
        // memasukkannya ke array lfsr
        for (int i = 0; i < iv_result.length(); i++) {
            lfsr[i] = Byte.parseByte(String.valueOf(iv_result.charAt(i)));
        }
        // mengisi index 64 sampai 80 dengan nilai 1
        for (int i = 64; i < 80; i++) {
            lfsr[i] = 1;
        }
        // konversi string key ke string biner
        String result2 = this.stringToBinary(key);
        // memasukkannya ke array nfsr
        for (int i = 0; i < result2.length(); i++) {
            this.nfsr[i] = Byte.parseByte(String.valueOf(result2.charAt(i)));
        }
    }


    public static String stringToBinary(String string) {
        String result = "";
        String tmpStr;
        char[] messChar = string.toCharArray();

        for (int i = 0; i < messChar.length; i++) {
            tmpStr = Integer.toBinaryString(messChar[i]);
            switch (tmpStr.length()) {
                case 1:
                    tmpStr = '0' + tmpStr;
                case 2:
                    tmpStr = '0' + tmpStr;
                case 3:
                    tmpStr = '0' + tmpStr;
                case 4:
                    tmpStr = '0' + tmpStr;
                case 5:
                    tmpStr = '0' + tmpStr;
                case 6:
                    tmpStr = '0' + tmpStr;
                case 7:
                    tmpStr = '0' + tmpStr;
            }
            result += tmpStr;
        }

        return result;
    }


    private String stringBinaryToHex(String string) {
        return new BigInteger(string, 2).toString(16);
    }


    private String hexToBinary(String hex) {
        StringBuilder binStrBuilder = new StringBuilder();
        int c = 1;

        for (int i = 0; i < hex.length() - 1; i += 2) {

            String output = hex.substring(i, (i + 2));

            int decimal = Integer.parseInt(output, 16);

            String binStr = Integer.toBinaryString(decimal);
            int len = binStr.length();
            StringBuilder sbf = new StringBuilder();
            if (len < 8) {
                for (int k = 0; k < (8 - len); k++) {
                    sbf.append("0");
                }
                sbf.append(binStr);
            } else {
                sbf.append(binStr);
            }
            c++;
            binStrBuilder.append(sbf.toString());
        }

        return binStrBuilder.toString();
    }


    private void lfsr() {
        Byte[] data = lfsr;
        for (int i = 0; i < 160; i++) {
           byte xor = (byte) (data[62] ^ data[51] ^ data[38] ^ data[23] ^ data[13] ^ data[0]);
            lfsr[i] = xor;
        }
    }

    private void nfsr() {
        Byte[] data = nfsr;
        for (int i = 0; i < 160; i++) {
            byte xor = (byte) (data[0] ^ data[62] ^ data[60]
                    ^ data[52] ^ data[45] ^ data[37]
                    ^ data[33] ^ data[28] ^ data[21]
                    ^ data[14] ^ data[19] ^ data[0]
                    ^ (data[63] & data[60]) ^ (data[37] & data[33])
                    ^ (data[15] & data[9]) ^ (data[60] & data[52] & data[45])
                    ^ (data[33] & data[28] & data[21])
                    ^ (data[63] & data[45] & data[28] & data[9])
                    ^ (data[60] & data[52] & data[37] & data[33])
                    ^ (data[63] & data[60] & data[21] & data[15])
                    ^ (data[63] & data[60] & data[52] & data[45] & data[37])
                    ^ (data[33] & data[28] & data[21] & data[15] & data[9])
                    ^ (data[52] & data[45] & data[37] & data[33] & data[28] & data[21]));
            nfsr[i] = xor;
        }

    }

    public String filter() {
        lfsr();
        nfsr();
        Byte x0 = lfsr[3];
        Byte x1 = lfsr[25];
        Byte x2 = lfsr[46];
        Byte x3 = lfsr[64];
        Byte x4 = nfsr[63];
        Byte f_h = (byte) (x1 ^ x4 ^ (x0 & x3) ^ (x2 & x3) ^ (x3 & x3) ^ (x0 & x1 & x2)
                ^ (x0 & x2 & x3) ^ (x0 & x2 & x4) ^ (x1 & x2 & x4) ^ (x2 & x3 & x4));

        String string_filter = "";
        for (int i = 0; i < 160; i++) {
            filter[i] = (byte) (temp_nfsr[i] ^ f_h);
            string_filter += Byte.toString(filter[i]);
        }

        return stringBinaryToHex(string_filter);
    }

    public String encrypt(String input) {
        String result = this.stringToBinary(input);
        String result_string = "";
        Byte[] result_array = new Byte[result.length()];
        Byte[] result_xor_array = new Byte[result.length()];

        for (int i = 0; i < result_array.length; i++) {
            result_array[i] = Byte.parseByte(String.valueOf(result.charAt(i)));
            result_xor_array[i] = (byte) (this.filter[i] ^ result_array[i]);
            result_string += result_xor_array[i];
        }
        return this.stringBinaryToHex(result_string);
    }

    public String decrypt(String cipher, String keystream) {
        String a = this.hexToBinary(cipher);
        Byte[] a_array = new Byte[a.length()];
        String b = this.hexToBinary(keystream);
        Byte[] b_array = new Byte[b.length()];
        String plain_binary = "";
        String plain = "";
        Byte[] hasil = new Byte[a.length()];

        for (int i = 0; i < a_array.length; i++) {
            a_array[i] = Byte.parseByte(String.valueOf(a.charAt(i)));
        }
        for (int i = 0; i < b_array.length; i++) {
            b_array[i] = Byte.parseByte(String.valueOf(b.charAt(i)));
        }
        for (int i = 0; i < a.length(); i++) {
            hasil[i] = (byte) (b_array[i] ^ a_array[i]);
            plain_binary += hasil[i];
        }
        for (int i = 0; i <= plain_binary.length() - 8; i += 8) {
            int k = Integer.parseInt(plain_binary.substring(i, i + 8), 2);
            plain += (char) k;
        }
        return plain;
    }

    public static void main(String[] args) throws DecoderException {
        GrainV0 grain = new GrainV0("aaaabcde", "1234567890");
        String keyStream = grain.filter();
        String cipher = grain.encrypt("hello");
        System.out.println(keyStream);
        String plain = grain.decrypt(cipher, keyStream);
        System.out.println(plain);
    }

}


