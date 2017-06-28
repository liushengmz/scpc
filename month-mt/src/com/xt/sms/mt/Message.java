package com.xt.sms.mt;

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;

import org.apache.log4j.Logger;

public class Message {
	private static Logger myLogger = Logger.getLogger(Message.class);
	public void connectToServer(String host, int port, ConnDesc conn) throws IOException {
		Socket localSocket = null;
		try {
			localSocket = new Socket(host, port);
			localSocket.setSoTimeout(600000);
			myLogger.debug(localSocket.toString());
		} catch (IOException localIOException) {
			myLogger.error("connectToServer",localIOException);
			throw localIOException;
		}
		conn.sock = localSocket;
	}

	public void disconnectFromServer(ConnDesc conn) {
		try {
			if ((conn.sock != null) && (!conn.sock.isClosed())) {
				conn.sock.getOutputStream().close();
				conn.sock.getInputStream().close();
				conn.sock.close();
				conn.sock = null;
			}
		} catch (Exception e) {
			myLogger.error("disconnectFromServer",e);
			return;
		}
	}

	public void sendSmDeliver(ConnDesc conn, SmDeliver deliver) throws IOException {
		DataOutputStream out = null;
		try {
			out = new DataOutputStream(conn.sock.getOutputStream());
			byte[] buf = deliver.getBytes();
			int bodyLen = buf.length;
			byte[] header = new byte[8];
			ByteCode bc = new ByteCode(8);
			myLogger.debug(8 + bodyLen);
			bc.AddInt(8 + bodyLen);
			bc.AddInt(3);

			bc.AddBytes(buf);
			out.write(bc.getBytes());
			out.flush();
		} catch (IOException e) {
			myLogger.error("sendSmDeliver",e);
			out = null;
			throw e;
		} catch (Exception e) {
			myLogger.error("sendSmDeliver",e);
		}
	}

	public void sendSmSubmit(ConnDesc conn, SmSubmit sub) throws IOException {
		DataOutputStream out = null;
		try {
			out = new DataOutputStream(conn.sock.getOutputStream());
			byte[] buf = sub.getBytes();
			int bodyLen = buf.length;
			byte[] header = new byte[8];
			ByteCode bc = new ByteCode(8);

			bc.AddInt(8 + bodyLen);
			bc.AddInt(1);

			bc.AddBytes(buf);
			out.write(bc.getBytes());
			out.flush();
		} catch (IOException e) {
			myLogger.error("sendSmSubmit",e);
			out = null;
			throw e;
		} catch (Exception e) {
			myLogger.error("sendSmSubmit",e);
		}
	}

	protected boolean readHead(DataInputStream in, SmPack p) throws IOException {
		try {
			p.pkHead.pkLen = in.readInt();
			p.pkHead.pkCmd = in.readInt();
			myLogger.debug("readHead ...pkCmd:" + p.pkHead.pkCmd);
		} catch (IOException e) {
			myLogger.error("readHead",e);
			throw e;
		}
		return true;
	}

	protected void sendHeader(SmHeader sh, DataOutput out) throws IOException {
		try {
			ByteCode bc = new ByteCode(4);
			bc.AddInt32(sh.pkLen);
			bc.AddInt32(sh.pkCmd);
			out.write(bc.getBytes());
		} catch (IOException e) {
			myLogger.error("sendHeader",e);
			throw e;
		}
	}

	public void readPa(ConnDesc conn) {
		SmResult sr = null;
		try {
			sr = readResPack(conn);
			switch (sr.packCmd) {
			case 1:
				SmSubmitResult ssr = (SmSubmitResult) sr;
				myLogger.debug("----receiver vcp ------stat=" + SmSubmitResult.stat);
				myLogger.debug("----receiver vcp submit message------");
				smSendSubmitAck(conn, SmSubmitResult.stat);
				break;
			case 2:
				SmSubmitAckResult ssra = (SmSubmitAckResult) sr;
				myLogger.debug("--------" + ssra.stat);
				break;
			case 3:
				SmDeliverResult sdr = (SmDeliverResult) sr;
				myLogger.debug(" ------receiver platform-----stat =" + SmDeliverResult.stat);
				smSendDeliverAck(conn, SmDeliverResult.stat);
				break;
			case 4:
				SmDeliverAckResult sdar = (SmDeliverAckResult) sr;
				myLogger.debug("---------deliverAck-----:" + sdar.stat);
				break;
			default:
				myLogger.debug("---------Error packet-----------");
			}
		} catch (Exception e) {
			myLogger.error("readPa",e);
			e.printStackTrace();
			try {
				if (conn.sock != null)
					conn.sock.close();
			} catch (Exception e1) {
				myLogger.error("",e1);
				e1.printStackTrace();
			}
		} finally {
			try {
				if (conn.sock != null)
					conn.sock.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void smSendSubmitAck(ConnDesc conn, String ackCode) throws Exception {
		SmHeader ch = new SmHeader();
		try {
			DataOutputStream out = new DataOutputStream(conn.sock.getOutputStream());
			ch.pkCmd = 2;
			int len1 = ackCode.getBytes().length;
			ch.pkLen = (11 + len1);
			sendHeader(ch, out);
			ByteCode ack = new ByteCode(3);
			ack.AddByte((byte) 100);
			ack.AddInt16((short) (3 + len1));
			ack.addAsciiz(ackCode, len1);
			byte[] b = ack.getBytes();
			out.write(ack.getBytes());
		} catch (Exception e) {
			myLogger.error("smSendSubmitAck",e);
			throw e;
		}
	}

	public void smSendDeliverAck(ConnDesc conn, String ackCode) throws Exception {
		SmHeader ch = new SmHeader();
		try {
			DataOutputStream out = new DataOutputStream(conn.sock.getOutputStream());
			ch.pkCmd = 4;
			int len1 = ackCode.getBytes().length;
			ch.pkLen = (11 + len1);
			sendHeader(ch, out);

			ByteCode ack = new ByteCode(3);
			ack.AddByte((byte) 100);
			ack.AddInt16((short) (3 + len1));
			ack.addAsciiz(ackCode, len1);
			byte[] b = ack.getBytes();
			out.write(ack.getBytes());
		} catch (Exception e) {
			myLogger.error("smSendDeliverAck",e);
			throw e;
		}
	}

	public SmResult readResPack(ConnDesc conn) throws IOException, UnknownPackException {
		DataInputStream in = null;
		SmResult sr = new SmResult();
		SmPack pack = new SmPack();

		in = new DataInputStream(conn.sock.getInputStream());

		readHead(in, pack);
		myLogger.debug("totalLen:" + pack.pkHead.pkLen);
		byte[] packbuf = new byte[pack.pkHead.pkLen - 8];
		in.read(packbuf);

		switch (pack.pkHead.pkCmd) {
		case 1:
			myLogger.debug("------- Case 1 -------");
			SmSubmitResult ssr = new SmSubmitResult();
			try {
				ssr.readInBytes(packbuf);
				ssr.packCmd = 1;
				return ssr;
			} catch (Exception e) {
				myLogger.error("readResPack",e);
				throw new UnknownPackException();
			}
		case 2:
			myLogger.debug("-------case 2--------");
			SmSubmitAckResult ssra = new SmSubmitAckResult();
			try {
				ssra.readInBytes(packbuf);
				ssra.packCmd = 2;
				return ssra;
			} catch (Exception e) {
				myLogger.error("readResPack",e);
				throw new UnknownPackException();
			}
		case 3:
			myLogger.debug("-------Case 3 --------");
			SmDeliverResult sdr = new SmDeliverResult();
			try {
				sdr.readInBytes(packbuf);
				sdr.packCmd = 3;
				return sdr;
			} catch (Exception e) {
				myLogger.error("readResPack",e);
				throw new UnknownPackException();
			}
		case 4:
			myLogger.debug("-----case 4------");
			SmDeliverAckResult sdar = new SmDeliverAckResult();
			try {
				sdar.readInBytes(packbuf);
				sdar.packCmd = 4;
				return sdar;
			} catch (Exception e) {
				myLogger.error("readResPack",e);
				throw new UnknownPackException();
			}
		}
		UnknownPackException un = new UnknownPackException();
		throw un;
	}
}