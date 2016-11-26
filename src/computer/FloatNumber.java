/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package computer;

/**
 *
 * @author Administrator
 */
public class FloatNumber {

    // 1-bit
    private int sign;
    // 7-bit
    private int exponent;
    // 8-bit
    private int mantissa;

    // For convenience of caculation
    private int exponentValue;
    private int signedMantissa;

    public FloatNumber(int content) {
        this.sign = content >> 15 & 1;
        this.exponent = content >> 8 & 0x7f;
        this.mantissa = content & 0xff;

        this.exponentValue = FloatNumber.complementToDecimal(this.exponent, 7);
        this.signedMantissa = this.sign == 0 ? this.mantissa : -this.mantissa;

        this.normalize();
    }

    public FloatNumber(int exponent, int signedMantissa) {
        this.sign = signedMantissa < 0 ? 1 : 0;
        this.exponent = exponent;
        this.mantissa = Math.abs(signedMantissa);

        this.exponentValue = FloatNumber.complementToDecimal(this.exponent, 7);
        this.signedMantissa = signedMantissa;

        this.normalize();
    }

    /**
     * Made exclusively for mantissa (so the bit length is 8).
     *
     * @param complement
     * @return decimal
     */
    public static int complementToDecimal(int complement) {
        int length = 8;
        if ((complement >> (length - 1) & 1) == 1) {
            return -(complement - 1 ^ ((1 << length) - 1));
        } else {
            return complement;
        }
    }

    private static int complementToDecimal(int complement, int length) {
        if ((complement >> (length - 1) & 1) == 1) {
            return -(complement - 1 ^ ((1 << length) - 1));
        } else {
            return complement;
        }
    }

    private static int decimalToComplement(int decimal, int length) {
        if (decimal < 0) {
            return decimal & ((1 << length) - 1);
        } else {
            return decimal;
        }
    }

    private static void align(FloatNumber f1, FloatNumber f2) {
        if (f1.exponentValue == f2.exponentValue) {
            return;
        }

        FloatNumber bigger, smaller;
        if (f1.exponentValue > f2.exponentValue) {
            bigger = f1;
            smaller = f2;
        } else {
            bigger = f2;
            smaller = f1;
        }
        bigger.mantissa *= Math.pow(10, bigger.exponentValue - smaller.exponentValue);
        bigger.signedMantissa *= Math.pow(10, bigger.exponentValue - smaller.exponentValue);
        bigger.exponent = smaller.exponent;
        bigger.exponentValue = smaller.exponentValue;
    }

    public FloatNumber add(FloatNumber addend) {
        FloatNumber.align(this, addend);
        int newSignedMantissa = this.signedMantissa + addend.signedMantissa;
        FloatNumber result = new FloatNumber(this.exponent, newSignedMantissa);
        result.normalize();
        return result;
    }

    public FloatNumber sub(FloatNumber subtrahend) {
        FloatNumber.align(this, subtrahend);
        int newSignedMantissa = this.signedMantissa - subtrahend.signedMantissa;
        FloatNumber result = new FloatNumber(this.exponent, newSignedMantissa);
        result.normalize();
        return result;
    }

    private void normalize() {
        if (this.mantissa == 0) {
            this.sign = 0;
            this.exponent = 0;
            this.exponentValue = 0;
        } else {
            while (this.mantissa % 10 == 0 && this.exponentValue < 64) {
                ++this.exponentValue;
                this.mantissa /= 10;
            }
            this.exponent = FloatNumber.decimalToComplement(this.exponentValue, 7);
            this.signedMantissa = this.sign == 0 ? this.mantissa : -this.mantissa;
        }
    }

    public boolean isFlowed() {
        return this.mantissa >= 1 << 8 || this.exponentValue < -63 || this.exponentValue > 64;
    }

    public int getBits() {
        return this.sign << 15 | this.exponent << 8 | this.mantissa;
    }

    public int getExponentBits() {
        return this.exponent;
    }

    public int getMantissaBits() {
        return FloatNumber.decimalToComplement(this.signedMantissa, 8);
    }

    private double getValue() {
        return this.signedMantissa * Math.pow(10, this.exponentValue);
    }

    public int toInteger() {
        return (int) this.getValue();
    }
}
