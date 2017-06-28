package com.xt.sms.mt;

import java.net.Socket;

public final class ConnDesc {
	public Socket sock;

	public ConnDesc() {
	}

	public ConnDesc(Socket paramSocket) {
		this.sock = paramSocket;
	}
}