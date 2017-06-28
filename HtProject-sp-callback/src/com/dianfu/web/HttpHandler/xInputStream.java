package com.dianfu.web.HttpHandler;

import java.io.IOException;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;

public class xInputStream extends ServletInputStream {

	byte[][] _data;
	int p = 0, len = 0;
	final int MAX_COL = 4 * 1024;

	ServletInputStream _stm;

	public xInputStream(ServletInputStream stm) {
		_data = new byte[1024][];
		_stm = stm;
	}

	@Override
	public boolean isFinished() {
		return _stm.isFinished();
	}

	@Override
	public boolean isReady() {

		return _stm.isReady();
	}

	@Override
	public void setReadListener(ReadListener arg0) {
		_stm.setReadListener(arg0);
	}

	@Override
	public int read() throws IOException {
		int v;
		if (p < len) {
			v = readCache();
			return v;
		}

		v = _stm.read();
		if (v == -1)
			return v;

		int x = p / MAX_COL;
		int y = p % MAX_COL;
		p++;
		len++;
		if (_data.length <= x) {
			// System.out.println("overflow");
			return v; // 数据溢出
		}

		if (_data[x] == null)
			_data[x] = new byte[4 * 1024];
		_data[x][y] = (byte) v;
		// System.out.println("x=" + x + ",y=" + y + "=" + v);
		return v;
	}

	private int readCache() {
		int x = p / MAX_COL;
		int y = p % MAX_COL;
		int v = _data[x][y];
		// System.out.println("read cache x=" + x + ",y=" + y + "=" + v);
		p++;
		return v;
	}

	@Override
	public synchronized void reset() throws IOException {
		p = 0;
	}

	@Override
	public void close() throws IOException {
		System.out.println("closed");
		_stm.close();
		super.close();
	}
}
