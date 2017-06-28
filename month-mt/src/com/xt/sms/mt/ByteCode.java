package com.xt.sms.mt;

import java.io.PrintStream;

import org.apache.log4j.Logger;

public class ByteCode {
	private static Logger myLogger = Logger.getLogger(ByteCode.class);
	public int len;
	public int size;
	public byte[] bytes;

	protected ByteCode(int i) {
		this.bytes = new byte[i];
		this.size = i;
		this.len = 0;
	}

	protected byte[] getBytes() {
		byte[] a = new byte[this.len];
		for (int i = 0; i < this.len; i++) {
			a[i] = this.bytes[i];
		}
		return a;
	}

	protected void AddInt16(short b) {
		if (this.len + 2 > this.size) {
			increase(2);
		}
		this.bytes[this.len] = ((byte) (b >>> 8 & 0xFF));
		this.len += 1;
		this.bytes[this.len] = ((byte) (b & 0xFF));
		this.len += 1;
	}

	protected void AddShort(short b) {
		if (this.len + 2 > this.size) {
			increase(2);
		}
		this.bytes[this.len] = ((byte) (b >>> 8 & 0xFF));
		this.len += 1;
		this.bytes[this.len] = ((byte) (b & 0xFF));
		this.len += 1;
	}

	protected void AddInt32(int i) {
		if (this.len + 4 > this.size) {
			increase(4);
		}
		this.bytes[this.len] = ((byte) (i >>> 24 & 0xFF));
		this.len += 1;
		this.bytes[this.len] = ((byte) (i >>> 16 & 0xFF));
		this.len += 1;
		this.bytes[this.len] = ((byte) (i >>> 8 & 0xFF));
		this.len += 1;
		this.bytes[this.len] = ((byte) (i & 0xFF));
		this.len += 1;
	}

	protected void AddInt(int i) {
		if (this.len + 4 > this.size) {
			increase(4);
		}
		this.bytes[this.len] = ((byte) (i >>> 24 & 0xFF));
		this.len += 1;
		this.bytes[this.len] = ((byte) (i >>> 16 & 0xFF));
		this.len += 1;
		this.bytes[this.len] = ((byte) (i >>> 8 & 0xFF));
		this.len += 1;
		this.bytes[this.len] = ((byte) (i & 0xFF));
		this.len += 1;
	}

	protected int getLen() {
		return this.len;
	}

	protected int getSize() {
		return this.size;
	}

	protected ByteCode(byte[] a) {
		this.bytes = a;
		this.size = a.length;
		this.len = a.length;
	}

	protected void increase(int paramInt) {
		int i = this.size + paramInt;
		byte[] a = new byte[i];
		for (int j = 0; j < this.size; j++) {
			a[j] = this.bytes[j];
		}
		this.bytes = a;
		this.size = i;
	}

	protected void AddByte(byte b) {
		if (this.len + 1 > this.size) {
			increase(1);
		}
		this.bytes[this.len] = b;
		this.len += 1;
	}

	protected void AddInt8(byte b) {
		if (this.len + 1 > this.size) {
			increase(1);
		}
		this.bytes[this.len] = b;
		this.len += 1;
	}

	protected void AddInt8(short b) {
		AddInt8((byte) b);
	}

	protected void AddBytes(byte[] b) throws Exception {
		if (b == null) {
			throw new Exception("Byte[] is null");
		}
		if (this.len == 0) {
			this.bytes = new byte[b.length];
			for (int i = 0; i < b.length; i++) {
				this.bytes[i] = b[i];
			}
			this.len = b.length;
			this.size = this.len;
		} else {
			byte[] c = new byte[this.len + b.length];
			for (int i = 0; i < this.len; i++) {
				c[i] = this.bytes[i];
			}
			for (int j = 0; j < b.length; j++) {
				c[(this.len + j)] = b[j];
			}
			this.bytes = c;
			this.len += b.length;
			this.size = this.len;
		}
	}

	protected void addAsciiz(String s, int i) throws Exception {
		int j = s.getBytes().length;
		if (j > i) {
			throw new Exception("too long.string.");
		}
		if (this.len + i > this.size) {
			increase(i);
		}
		for (int k = 0; k < j; k++) {
			this.bytes[this.len] = ((byte) (s.charAt(j) & 0xFF));
			this.len += 1;
		}
		this.len += i - j;
	}

	protected void printBytes() {
		int j = 0;
		for (int i = 0; i < this.len; i++) {
			if (i % 10 == 0) {
				System.out.println('\n');
				j++;
				System.out.print("Line Number " + j + "  :");
			}
			System.out.print(' ');
			System.out.print(' ');
			System.out.print('[');
			System.out.print(Long.toString(this.bytes[i] & 0xFF, 16));
			System.out.print('|');
			System.out.print((char) this.bytes[i]);
			System.out.print(']');
		}
		System.out.print("\n-------------------------------------------------------------------");
	}
}